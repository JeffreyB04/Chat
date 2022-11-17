package chatServer.data;

import chatProtocol.Message;
import chatProtocol.User;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Data {
    private List<User> users;
    private Data data;
    private List<Message> messages;

    public Data() {

        //no se si era asi lo del load en constructor
       /* data = new Data();
        try {
            data = XmlPersister.instance().load();
        } catch (Exception e) {
            data = new Data();
        }*/
        messages = new ArrayList<>();

        users = new ArrayList<>();
        //users.add(new User("001","001","Juan"));
        //users.add(new User("002","002","Maria"));
        //users.add(new User("003","003","Pedro"));
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}

//cargar lista de contactos y luego mandarla al model
//en el check hay que agregar al data
//los usuarios tienen localmente los contactos, no en servidor
