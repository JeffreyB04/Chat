package chatServer.data;

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

    public Data() {
        users = new ArrayList<>();
        users.add(new User("001","001","Juan"));
        users.add(new User("002","002","Maria"));
        users.add(new User("003","003","Pedro"));
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
