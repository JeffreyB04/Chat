package chatServer;

import chatProtocol.Protocol;
import chatProtocol.User;
import chatProtocol.IService;
import chatProtocol.Message;
import chatServer.data.Data;
import chatServer.data.UsuarioDao;
import chatServer.data.XmlPersister;

import java.util.List;

public class Service implements IService {

    private Data data;
    private UsuarioDao usuarioDao;

    private static Service theInstance;
    public static Service instance(){
        if (theInstance==null){
            theInstance=new Service();
        }
        return theInstance;
    }

    public Service() {
        data = new Data();
        usuarioDao = new UsuarioDao();
        try {
            data = XmlPersister.instance().load();
        } catch (Exception e) {
            data = new Data();
        }
    }

    public void post(Message m) {
        // if wants to save messages, ex. recivier no logged on
    }

    @Override
    public void register(User u) throws Exception {
        usuarioDao.create(u);
    }

    public User login(User p) throws Exception {
        usuarioDao.read(p.getId());
        p.getClave();
        return p;
    }

    public void logout(User p) throws Exception {
        //nothing to do
    }

    public User checkContact(User u) throws Exception {
        //usuarioDao.findById(u.getId());
      User aux=usuarioDao.read(u.getId());
        if(aux.getId().equals(u.getId())){
            return aux;
        }else {
            int error = Protocol.ERROR_CONTACT;
            return null;
        }
    }

    public List<User> UserSearch(String filtro) throws Exception {
        return  usuarioDao.findById(filtro);
    }

    public void store() {
        try {
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}


