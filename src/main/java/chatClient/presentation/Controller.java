package chatClient.presentation;

import chatClient.logic.ServiceData;
import chatClient.logic.ServiceProxy;
import chatProtocol.Message;
import chatProtocol.User;
import chatServer.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    View view;
    Model model;
    ServiceProxy localService;
    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        localService = (ServiceProxy)ServiceProxy.instance();
        localService.setController(this);
        view.setController(this);
        view.setModel(model);
    }
    public void login(User u) throws Exception{
        User logged=ServiceProxy.instance().login(u);

        ServiceData.instance().load(logged.getId());
        model.setContactsList(ServiceData.instance().contacts());

        model.setCurrentUser(logged);
        model.commit(Model.USER);
    }
    public void post(String text){
        Message message = new Message();
        message.setMessage(text);
        message.setSender(model.getCurrentUser());
        message.setReceiver(model.getSelected());
        ServiceProxy.instance().post(message);
        model.commit(Model.CHAT);
    }
    public void logout(){
        try {
            ServiceProxy.instance().logout(model.getCurrentUser());
            model.setMessages(new ArrayList<>());
            model.commit(Model.CHAT);
        } catch (Exception ex) {
        }

        ServiceData.instance().store(model.getCurrentUser().getId());

        model.setCurrentUser(null);
        model.commit(Model.USER+Model.CHAT);
    }
    public void register(User u)throws Exception{
        ServiceProxy.instance().register(u);
        model.setCurrentUser(u);
        model.commit(Model.USER);
    }
    public void deliver(Message message){
        model.messages.add(message);
        model.commit(Model.CHAT);
    }

    public void searchContacts(String filtro) throws Exception {
        model.setContactsList(ServiceData.instance().searchByName(filtro));
        model.commit(Model.USER);
    }
    public void updateContacts() throws Exception {
        model.setContactsList(ServiceData.instance().contacts());
        ServiceData.instance().store(model.getCurrentUser().getId());
        model.commit(Model.USER);
    }
    public void checkContact(String id)throws Exception{
        ServiceProxy.instance().checkContact(id);
    }
    public void addContact(User u)throws Exception{
        ServiceData.instance().addContact(u);
        ServiceData.instance().store(model.getCurrentUser().getId());
        model.setContactsList(ServiceData.instance().contacts());

        model.commit(Model.USER);
    }

    public void updateEstado(String id, boolean estado){
        ServiceData.instance().updateContact(id, estado);
        model.setContactsList(ServiceData.instance().contacts());
        model.commit(model.USER);
        ServiceData.instance().store(model.getCurrentUser().getId());
    }
    public void addUnReadMessages(List<Message> unReadMessages){
        for(Message m: unReadMessages){
            ServiceData.instance().addMessage(m);
        }
        model.commit(model.CHAT);
    }

}