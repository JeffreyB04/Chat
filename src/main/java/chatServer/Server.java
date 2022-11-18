
package chatServer;

import chatProtocol.IService;
import chatProtocol.Message;
import chatProtocol.Protocol;
import chatProtocol.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    ServerSocket srv;
    List<Worker> workers; //uno por cada usuario

    public Server() {
        try {
            srv = new ServerSocket(Protocol.PORT);
            workers =  Collections.synchronizedList(new ArrayList<Worker>());
            System.out.println("Servidor iniciado...");
        } catch (IOException ex) {
        }
    }

    public void run(){
        IService service = new Service();
        boolean continuar = true;
        ObjectInputStream in=null;
        ObjectOutputStream out=null;
        Socket skt=null;
        int method;
        while (continuar) {
            try {
                skt = srv.accept();
                in = new ObjectInputStream(skt.getInputStream());
                out = new ObjectOutputStream(skt.getOutputStream());
                System.out.println("Conexion Establecida...");
                method = in.readInt();
                switch (method) {
                    case Protocol.LOGIN:
                        try {
                            User user = service.login((User) in.readObject()); //el login de abajo no se usa?
                            user.setEstado(true);
                            out.writeInt(Protocol.ERROR_NO_ERROR);
                            out.writeObject(user);
                            out.flush();
                            Worker worker = new Worker(this, in, out, user, service);
                            workers.add(worker);
                            worker.start();

                            updateEstado(user.getId(), user.isEstado());

                } catch (Exception ex) {
                            out.writeInt(Protocol.ERROR_LOGIN);
                            out.flush();
                            skt.close();
                            System.out.println("Conexion cerrada...");
                        }
                        break;
                    case Protocol.REGISTER:
                        try {
                            service.register((User) in.readObject());

                            out.writeInt(Protocol.ERROR_NO_ERROR);
                            out.flush();
                        } catch (Exception ex) {
                            out.writeInt(Protocol.ERROR_REGISTER);
                            out.flush();
                            skt.close();
                            System.out.println("Conexion cerrada...");
                        }
                        break;
                    default:
                        out.writeInt(Protocol.ERROR_LOGIN);
                        out.flush();
                        out.close();
                        System.out.println("Conexion cerrada...");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void deliver(Message message){
        for(Worker wk:workers){
            //wk.deliver(message);
            if(message.getReceiver().equals(wk.user)){
                wk.deliver(message);
            }
            if(message.getSender().equals(wk.user)){
                wk.deliver(message);
            }
        }
    }

    public void remove(User u){
        for(Worker wk:workers) if(wk.user.equals(u)){workers.remove(wk);break;}
        System.out.println("Quedan: " + workers.size());
    }

    public void updateEstado(String id, boolean estado){
        for (Worker wk: workers){
            if(!wk.user.getId().equals(id)) {
                wk.updateEstado(id, estado);
            }
        }
    }
    public boolean checkStatus(User u){
        for (Worker wk: workers){
            if(wk.user.equals(u)){
                return true;
            }
        }
        return false;
    }

}