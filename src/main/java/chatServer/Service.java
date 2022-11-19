package chatServer;

import chatProtocol.Protocol;
import chatProtocol.User;
import chatProtocol.IService;
import chatProtocol.Message;
import chatServer.data.MensajeDao;
import chatServer.data.UsuarioDao;

import java.util.List;

public class Service implements IService{
    private UsuarioDao usuarioDao;
    private MensajeDao mensajeDao;
    public Service() {
        usuarioDao=new UsuarioDao();
        mensajeDao=new MensajeDao();
    }
    public void post(Message m) {
        try {
            mensajeDao.createM(m);
        } catch (Exception ex){}
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
            usuarioDao.create(u);
    }
    public User checkContact(String id) throws Exception {
        return usuarioDao.getUser(id);
    }

    public List<Message> noLeido(String receiver) throws Exception{
        return mensajeDao.noLeidos(receiver);
    }

    public void borrarNoLeido(String receiver) throws Exception{
        mensajeDao.borrarMensajes(receiver);
    }
}
