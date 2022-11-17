package chatServer.data;

import chatProtocol.User;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<User> users;

    public Data() {
        users = new ArrayList<>();
        users.add(new User("01","01","Uno"));
        users.add(new User("02","02","Dos"));
        users.add(new User("03","03","Tres"));
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

//cargar lista de contactos y luego mandarla al model
