package chatServer;

import chatProtocol.Protocol;
import chatProtocol.User;
import chatProtocol.IService;
import chatProtocol.Message;
import chatServer.data.Data;
import chatServer.data.UsuarioDao;

public class Service implements IService{
    private Data data;
    private UsuarioDao usuarioDao;
    public Service() {
        data =  new Data();
        usuarioDao=new UsuarioDao();
    }
    public void post(Message m){
        // if wants to save messages, ex. recivier no logged on
    }
    public User login(User p) throws Exception{
        User aux = usuarioDao.read(p.getId());
        if (aux.getClave().equals(p.getClave())){
            p.setNombre(aux.getNombre());
            return p;
        }else {
            int error = Protocol.ERROR_LOGIN;
            throw new Exception(""+ error);
        }
    }

    public void logout(User p) throws Exception{
        //nothing to do
    }
    public void register(User u) throws Exception {
        User aux = usuarioDao.read(u.getId());
        if(aux.getId().equals(u.getId())){
            int error = Protocol.ERROR_REGISTER;
            throw new Exception(""+ error);
        }
        else {
            u.setEstado("ONLINE");
            usuarioDao.create(u);
        }
    }
    public User checkContact(User u) throws Exception {
        User aux=usuarioDao.read(u.getId());
        if(aux.getId().equals(u.getId())){
            return aux;
        }
        else {
            int error = Protocol.ERROR_CONTACT;
            return null;
        }
    }

}
