package chatServer;

import chatProtocol.User;
import chatProtocol.IService;
import chatProtocol.Message;
import chatServer.data.Data;
import chatServer.data.Database;
import chatServer.data.UsuarioDao;

public class Service implements IService{

    private Data data;
    private UsuarioDao usuarioDao;
    
    public Service() {
        data =  new Data();
        usuarioDao = new UsuarioDao();
    }
    
    public void post(Message m){
        // if wants to save messages, ex. recivier no logged on
    }
    
    public User login(User p) throws Exception{
        //for(User u:data.getUsers()) if(p.equals(u)) return u;
        //throw new Exception("User does not exist");
        usuarioDao.create(p);
        return p;
        //p.setNombre(p.getId()); return p;
    } 

    public void logout(User p) throws Exception{
        //nothing to do
    }    
}
