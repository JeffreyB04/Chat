/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatClient.presentation;

import chatProtocol.Message;
import chatProtocol.User;

import java.util.ArrayList;
import java.util.List;

public class Model extends java.util.Observable {
    User currentUser;
    User selected;
    List<Message> messages;
    String id = "null";
    List<User> contactsList;

    public Model() {
        currentUser = null;
        //messages= new ArrayList<>();
        contactsList= new ArrayList<>();
        id = "null";
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public User getSelected() {
        return selected;
    }

    public void setSelected(User selected) {
        this.selected = selected;
    }
    public List<User> getContactsList() {return contactsList;}

    public void setContactsList(List<User> contactsList) {
        this.contactsList = contactsList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public User getContact(String filtro){  //id al que lo manda
        for (User u : contactsList){
            if (u.getId().equals(filtro)){
                return u;
            }
        }
        return null;
    }
    public void addObserver(java.util.Observer o) {
        super.addObserver(o);
        this.commit(Model.USER+Model.CHAT);
    }

    public void commit(int properties){
        this.setChanged();
        this.notifyObservers(properties);
    }

    public static int USER=1;
    public static int CHAT=2;
}
