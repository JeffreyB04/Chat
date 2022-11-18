package chatClient.data;

import chatProtocol.Message;
import chatProtocol.User;
import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Data {
    private ArrayList<User> contacts;
    private ArrayList<Message> messages;

    public Data(){
        contacts = new ArrayList<>();
        messages = new ArrayList<>();
        contacts.add(new User("01","01","01"));
        contacts.add(new User("02","02","01"));
        contacts.add(new User("03","03","01"));
    }

    public ArrayList<User> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<User> contacts) {
        this.contacts = contacts;
    }

    public void addContact(User user){
        this.contacts.add(user);
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message){
        this.messages.add(message);
    }
}