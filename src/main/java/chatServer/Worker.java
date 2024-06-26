package chatServer;

import chatProtocol.User;
import chatProtocol.Protocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

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
                            user.setEstado(false);
                            srv.updateEstado(user.getId(), user.isEstado());
                            srv.remove(user);
                            stop();
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
                        } catch (ClassNotFoundException ex) {} catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case Protocol.CONTACT:
                        try {
                            User user = service.checkContact((String)in.readObject());
                            user.setEstado(srv.checkEstado(user));
                            if(!user.equals(this.user)){
                                out.writeInt(Protocol.CONTACT_RESPONSE);
                                out.writeInt(Protocol.ERROR_NO_ERROR);
                                out.writeObject(user);
                                out.flush();
                            } else {
                                out.writeInt(Protocol.CONTACT_RESPONSE);
                                out.writeInt(Protocol.ERROR_CONTACT_EQUAL);
                                out.flush();
                            }
                        } catch (Exception ex){
                            out.writeInt(Protocol.CONTACT_RESPONSE);
                            out.writeInt(Protocol.ERROR_CONTACT);
                            out.flush();
                        }
                        break;
                    case Protocol.UNREADMESSAGES:
                        try {
                            List<Message> messages = service.noLeido((String)in.readObject());
                            if (!messages.isEmpty()) {
                                out.writeInt(Protocol.UNREADMESSAGES_RESPONSE);
                                out.writeInt(Protocol.ERROR_NO_ERROR);
                                out.writeObject(messages);
                                out.flush();
                            } else {
                                out.writeInt(Protocol.UNREADMESSAGES_RESPONSE);
                                out.writeInt(Protocol.ERROR_UNREAD_MESSAGES);
                                out.flush();
                            }
                        } catch (Exception ex){
                            out.writeInt(Protocol.UNREADMESSAGES_RESPONSE);
                            out.writeInt(Protocol.ERROR_UNREAD_MESSAGES);
                            out.flush();
                        }
                        break;
                    case Protocol.DELETEREADMESSAGES:
                        try {
                            service.borrarNoLeido((String)in.readObject());
                        }catch (Exception ex){}
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

    public void updateEstado(String id, boolean estado){
        try {
            out.writeInt(Protocol.UPDATE_STATUS);
            out.writeObject(id);
            out.writeObject(estado);
            out.flush();
        } catch (Exception ex){

        }
    }
}
