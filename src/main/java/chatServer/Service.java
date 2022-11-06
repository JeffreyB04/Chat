package chatServer;

import chatProtocol.User;
import chatProtocol.IService;
import chatProtocol.Message;
import chatServer.data.Data;
import chatServer.data.UsuarioDao;
import chatServer.data.XmlPersister;

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
    public User register(User u) throws Exception {
        usuarioDao.create(u);
        return u;
    }

    public User login(User p) throws Exception {
        usuarioDao.read(p.getId());
        p.getClave();
        return p;
    }

    //for(User u:data.getUsers()) if(p.equals(u)) return u;
    //throw new Exception("User does not exist");
    //usuarioDao.create(p);
    //return p;
    //p.setNombre(p.getId()); return p;


    public void logout(User p) throws Exception {
        //nothing to do
    }

    // @Override
    // public void register(Message m) {
    // }

    public void store() {
        try {
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}


