package chatClient.logic;

import chatClient.data.Data;
import chatClient.data.XMLPersister;
import chatProtocol.Message;
import chatProtocol.User;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceData {
    private Data data;
    private static ServiceData theInstance;

    private ServiceData(){
        data = null;
    }

    public static ServiceData instance(){
        if(theInstance == null){
            theInstance = new ServiceData();
        }
        return theInstance;
    }

    public void load(String name){
        try {
            data = XMLPersister.instance().load("XML/" + name + ".xml");
        } catch (Exception ex){
            data = new Data();
        }
    }

    public void store(String name){
        try {
            XMLPersister.instance().store(data,"XML/" + name + ".xml");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<User> contacts(){
        return data.getContacts();
    }

    public void addMessage(Message message){
        data.addMessage(message);
    }

    public void addContact(User user){
        data.addContact(user);
    }

    public List<User> contactWith(String filter){
        return data.getContacts().stream()
                .filter(u->u.getId().contains(filter))
                .collect(Collectors.toList());
    }

    public List<Message> searchMessage(String filtro){
        return data.getMessages().stream()
                .filter(m->m.getReceiver().getId().equals(filtro) || m.getSender().getId().equals(filtro))
                .collect(Collectors.toList());
    }

    public void actualizarContacto(String codigo, boolean estado){
        for (User user: contacts()){
            if (user.getId().equals(codigo)){
                user.setEstado(estado);
            }
        }
    }
}
