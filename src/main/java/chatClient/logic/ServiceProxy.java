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
        int serverAnswer;
        connect();
        out.writeInt(Protocol.REGISTER);
        out.writeObject(u);
        out.flush();
        serverAnswer=in.readInt();
        if (serverAnswer==Protocol.ERROR_REGISTER){
            throw new Exception("Error al registrar");
        }
        disconnect();
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
                    case Protocol.CONTACT_RESPONSE:
                        try {
                            int error = in.readInt();
                            if (error == Protocol.ERROR_NO_ERROR){
                                User user= (User)in.readObject();
                                addContact(user);
                            } else if (error == Protocol.ERROR_CONTACT) {
                                controller.errorContact("id no existe");
                            } else if (error == Protocol.ERROR_CONTACT_EQUAL){
                                controller.errorContact(" id ");
                            }
                        } catch (Exception ex){}
                        break;
                    case Protocol.UNREADMESSAGES_RESPONSE:
                        try {
                            int error = in.readInt();
                            if (error == Protocol.ERROR_NO_ERROR){
                                List<Message> messages = (List<Message>)in.readObject();
                                addNoleido(messages);
                            }
                        }catch (Exception ex){}
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
    private void addNoleido(final List<Message> messages){
        SwingUtilities.invokeLater(new Runnable(){
                                       public void run(){
                                           controller.addNoleidos(messages);
                                       }
                                   }
        );
    }

    public List<Message> noLeido(String receiver) throws Exception{
        out.writeInt(Protocol.UNREADMESSAGES);
        out.writeObject(receiver);
        out.flush();
        return null;
    }

    public void borrarNoLeido(String receiver) throws Exception{
        out.writeInt(Protocol.DELETEREADMESSAGES);
        out.writeObject(receiver);
        out.flush();
    }
    public User checkContact(String id) throws Exception{
        out.writeInt(Protocol.CONTACT);
        out.writeObject(id);
        out.flush();
        return null;
    }
    private void addContact(final User user){
        SwingUtilities.invokeLater(new Runnable(){
                                       public void run(){
                                           try {
                                               controller.addContact(user);
                                           } catch (Exception e) {
                                               throw new RuntimeException(e);
                                           }
                                       }
                                   }
        );
    }
}
