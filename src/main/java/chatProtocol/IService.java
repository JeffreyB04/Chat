package chatProtocol;

public interface IService {
    public User login(User u) throws Exception;
    public void logout(User u) throws Exception; 
    public void post(Message m);
    //public void checkContact(String id) throws Exception;
    public void checkContact(User u) throws Exception;
    public void register(User u) throws Exception;;
}
