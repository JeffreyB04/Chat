package chatProtocol;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlID;

import java.io.Serializable;
//@XmlAccessorType(XmlAccessType.FIELD)
public class Message implements Serializable{

    //tabla de mensajes guarda id de quien lo envia, de quien lo recibe y el mensaje como tal (foreing keys)
    //si el usuario no está logeado, tiene que recibir los mensajes de la tabla cuando entre

    User sender;
    String message;

    User receiver;

    public Message() {
    }

    public Message(User sedner,String message, User receiver) {
        this.sender = sedner;
        this.message = message;
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
    
}
