package chatProtocol;

import java.util.List;

public interface IService {
    public User login(User u) throws Exception;
    public void logout(User u) throws Exception;
    public void post(Message m);
    public void register(User u) throws Exception;
    public User checkContact(String id) throws Exception;
    public List<Message> noLeido(String receiver) throws Exception;
    public void borrarNoLeido(String receiver) throws Exception;
}
