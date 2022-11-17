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
    List<Message> messages;

    List<User> users;
    List<User> allContacts;

    User selected;


    public Model() {
       currentUser = null;
       messages= new ArrayList<>();
        allContacts= new ArrayList<>();
       //users =new ArrayList<>();
       this.setUsers(users =new ArrayList<>()); //lee desde la base, pero no funciona porque tiene que leer desde XML
       //users.add(new User("001","001","Juan"));
       //users.add(new User("002","002","Maria"));
       //users.add(new User("003","003","Pedro"));
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getSelected() {
        return selected;
    }

    public void setSelected(User selected) {
        this.selected = selected;
    }

    public List<User> getAllContacts() {
        return allContacts;
    }

    public void setAllContacts(List<User> allContacts) {
        this.allContacts = allContacts;
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
