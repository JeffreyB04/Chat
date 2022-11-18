package chatClient.logic;

import chatClient.presentation.Controller;
import chatProtocol.User;
import chatProtocol.Protocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import javax.swing.SwingUtilities;
import chatProtocol.IService;
import chatProtocol.Message;

// para contacto se manda la peticion, no hace read y no espera respuesta

public class ServiceProxy implements IService{
    private static IService theInstance;
    public static IService instance(){
        if (theInstance==null){
            theInstance=new ServiceProxy();
        }
        return theInstance;
    }
    ObjectInputStream in;
    ObjectOutputStream out;
    Controller controller;

    public ServiceProxy() {}

    public void setController(Controller controller) {
        this.controller = controller;
    }

    Socket skt;
    private void connect() throws Exception{
        skt = new Socket(Protocol.SERVER,Protocol.PORT);
        out = new ObjectOutputStream(skt.getOutputStream() );
        out.flush();
        in = new ObjectInputStream(skt.getInputStream());
    }

    private void disconnect() throws Exception{
        skt.shutdownOutput();
        skt.close();
    }

    public User login(User u) throws Exception{
        connect();
        try {
            out.writeInt(Protocol.LOGIN);
            out.writeObject(u);
            out.flush();
            int response = in.readInt();
            if (response==Protocol.ERROR_NO_ERROR){
                User u1=(User) in.readObject();
                this.start();
                return u1;
            }
            else {
                disconnect();
                throw new Exception("No remote user");
            }
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }
    }
    public void logout(User u) throws Exception{
        out.writeInt(Protocol.LOGOUT);
        out.writeObject(u);
        out.flush();
        this.stop();
        this.disconnect();
    }
    public void post(Message message){
        try {
            out.writeInt(Protocol.POST);
            out.writeObject(message);
            out.flush();
        } catch (IOException ex) {

        }
    }
    public void register(User u) throws Exception {
        connect();
        int response;
        try {
            out.writeInt(Protocol.REGISTER);
            out.writeObject(u);
            out.flush();
            disconnect();
            response = in.readInt();
            if (response==Protocol.ERROR_NO_ERROR){
                throw new Exception("Registration Error!");
            }
        } catch (IOException ex) {}
    }
    public User checkContact(User u) throws Exception {
        try {
            out.writeInt(Protocol.CONTACT);
            out.writeObject(u);
            out.flush();
        } catch (IOException ex) {
            throw new Exception("Error");
        }
        return null;
    }

    private void updateEstado( final String id, final boolean estado){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                controller.updateEstado(id, estado);
            }
        });
    }
    boolean continuar = true;
    public void start(){
        System.out.println("Client worker atendiendo peticiones...");
        Thread t = new Thread(new Runnable(){
            public void run(){
                listen();
            }
        });
        continuar = true;
        t.start();
    }
    public void stop(){
        continuar=false;
    }
    public void listen(){
        int method;
        while (continuar) {
            try {
                method = in.readInt();
                System.out.println("DELIVERY");
                System.out.println("Operacion: "+method);
                switch(method){
                    case Protocol.DELIVER:
                        try {
                            Message message=(Message)in.readObject();
                            deliver(message);
                        } catch (ClassNotFoundException ex) {}
                        break;
                    case Protocol.CONTACT:
                        try {
                            int serverAnswer = in.readInt();
                            if (serverAnswer == Protocol.ERROR_NO_ERROR) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            controller.addContact((User) in.readObject());
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                            } else if (serverAnswer == Protocol.ERROR_CONTACT) {}
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case Protocol.UPDATE_STATUS:
                        try {
                            String id = (String)in.readObject();
                            boolean estado = in.readBoolean();
                            updateEstado(id, estado);

                        } catch (Exception ex){}
                        break;
                }
                out.flush();
            } catch (IOException  ex) {
                continuar = false;
            }
        }
    }
    private void deliver( final Message message ){
        SwingUtilities.invokeLater(new Runnable(){
                                       public void run(){
                                           controller.deliver(message);
                                       }
                                   }
        );
    }
    private void addUnReadMessages(final List<Message> messages){
        SwingUtilities.invokeLater(new Runnable(){
                                       public void run(){
                                           controller.addUnReadMessages(messages);
                                       }
                                   }
        );
    }

    public List<Message> unReadMessages(String receiver) throws Exception{
        out.writeInt(Protocol.UNREADMESSAGES);
        out.writeObject(receiver);
        out.flush();
        return null;
    }

    public void deleteReadMessages(String receiver) throws Exception{
        out.writeInt(Protocol.DELETEREADMESSAGES);
        out.writeObject(receiver);
        out.flush();
    }
    public User checkContact(String username) throws Exception{
        out.writeInt(Protocol.CONTACT);
        out.writeObject(username);
        out.flush();
        return null;
    }
}
