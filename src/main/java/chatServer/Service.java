package chatServer;

import chatProtocol.Protocol;
import chatProtocol.User;
import chatProtocol.IService;
import chatProtocol.Message;
import chatServer.data.Data;
import chatServer.data.MensajeDao;
import chatServer.data.UsuarioDao;

import java.util.List;

public class Service implements IService{
    private Data data;
    private UsuarioDao usuarioDao;
    private MensajeDao mensajeDao;
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
       // User aux = usuarioDao.read(u.getId());
        //if(aux.getId().equals(u.getId())){
         //   int error = Protocol.ERROR_REGISTER;
          //  throw new Exception(""+ error);
        //}
        //else {
         //   u.setEstado(true);
            usuarioDao.create(u);
        //}
    }
    public User checkContact(String id) throws Exception {
        return usuarioDao.getUser(id);
    }

    public List<Message> unReadMessages(String receiver) throws Exception{
        return mensajeDao.unReadMessages(receiver);
    }

    public void deleteReadMessages(String receiver) throws Exception{
        mensajeDao.deleteReadMessages(receiver);
    }
}
