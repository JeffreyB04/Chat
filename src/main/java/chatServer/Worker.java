package chatServer;

import chatProtocol.User;
import chatProtocol.Protocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import chatProtocol.IService;
import chatProtocol.Message;

public class Worker {  //empieza cuando se logea
    Server srv;
    ObjectInputStream in;
    ObjectOutputStream out;
    IService service;
    User user;

    public Worker(Server srv, ObjectInputStream in, ObjectOutputStream out, User user, IService service) {
        this.srv=srv;
        this.in=in;
        this.out=out;
        this.user=user;
        this.service=service;
    }

    boolean continuar;
    public void start(){
        try {
            System.out.println("Worker atendiendo peticiones...");
            Thread t = new Thread(new Runnable(){
                public void run(){
                    listen();
                }
            });
            continuar = true;
            t.start();
        } catch (Exception ex) {
        }
    }

    public void stop(){
        continuar=false;
        System.out.println("Conexion cerrada...");
    }

    public void listen(){
        int method;
        while (continuar) {
            try {
                method = in.readInt();
                System.out.println("Operacion: "+method);
                switch(method){
                    //case Protocol.LOGIN: done on accept
                    case Protocol.LOGOUT:
                        try {
                            user.setEstado("OFFLINE");
                            srv.updateEstado(user.getId(), user.getEstado());
                            srv.remove(user);
                            stop();
                            //service.logout(user); //nothing to do
                        } catch (Exception ex) {}
                        break;
                    case Protocol.POST:
                        Message message=null;
                        try {
                            message = (Message)in.readObject();
                            message.setSender(user);
                            srv.deliver(message);
                            //service.post(message); // if wants to save messages, ex. recivier no logged on
                            System.out.println(user.getNombre()+": "+message.getMessage());
                        } catch (ClassNotFoundException ex) {}
                        break;
                    case Protocol.CONTACT:
                        try {
                            User user = (User) in.readObject();
                            User value = service.checkContact(user);

                            value.setEstado(srv.checkStatus(value));

                            out.writeInt(Protocol.CONTACT_RESPONSE);
                            if (value != null){
                                out.writeInt(Protocol.ERROR_NO_ERROR);
                                out.writeObject(value);
                                out.flush();
                            }else{
                                out.writeInt(Protocol.ERROR_CONTACT);
                                out.flush();
                            }
                            user.setId(user.getId());
                        } catch (ClassNotFoundException e) {} catch (Exception e){
                            throw new RuntimeException();
                        }
                        break;
                }
                out.flush();
            } catch (IOException  ex) {
                System.out.println(ex);
                continuar = false;
            }
        }
    }

    public void deliver(Message message){
        try {
            out.writeInt(Protocol.DELIVER);
            out.writeObject(message);
            out.flush();
        } catch (IOException ex) {
        }
    }

    public void updateEstado(String id, String estado){
        try {
            out.writeInt(Protocol.USER_ESTADO);
            out.writeObject(id);
            out.writeObject(estado);
            out.flush();
        } catch (Exception ex){

        }
    }
}
