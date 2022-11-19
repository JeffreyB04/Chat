package chatClient.presentation;

import chatClient.logic.ServiceData;
import chatClient.logic.ServiceProxy;
import chatProtocol.Message;
import chatProtocol.User;
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

        ServiceProxy.instance().noLeido(logged.getId());
        ServiceProxy.instance().borrarNoLeido(logged.getId());

        model.setContactsList(ServiceData.instance().contacts());
        model.setCurrentUser(logged);
        model.commit(Model.USER);
    }
    public void changeContact(String id) {
        model.setId(id);
        model.commit(model.CHAT);
    }
    public void post(String text, String id) {
        Message message = new Message();
        message.setMessage(text);
        message.setSender(model.getCurrentUser());
        //model.setId(id);
        // model.id = String.valueOf(model.getContact(id));
        id = model.getId();
        message.setReceiver(model.getContact(id));

        ServiceProxy.instance().post(message);
        ServiceData.instance().addMessage(message);
        model.commit(Model.CHAT);
        ServiceData.instance().store(model.getCurrentUser().getId());
    }
    public void logout(){
        try {
            ServiceProxy.instance().logout(model.getCurrentUser());
            model.commit(Model.CHAT);
        } catch (Exception ex) {
        }

        ServiceData.instance().store(model.getCurrentUser().getId());
        model.setId("null");
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
        ServiceData.instance().addMessage(message);
        ServiceData.instance().store(model.getCurrentUser().getId());
        model.commit(Model.CHAT);
    }

    public void buscarContactos(String filtro) throws Exception {
        model.setContactsList(ServiceData.instance().contactWith(filtro));
        model.commit(Model.USER);
    }
    public void actualizarContacttos() throws Exception {
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
    public User getContact(int row) {
        return model.contactsList.get(row);
    }


    public void updateEstado(String id, boolean estado){
        ServiceData.instance().actualizarContacto(id, estado);
        model.setContactsList(ServiceData.instance().contacts());
        model.commit(model.USER);
        ServiceData.instance().store(model.getCurrentUser().getId());
    }
    public void addNoleidos(List<Message> unReadMessages){
        for(Message m: unReadMessages){
            ServiceData.instance().addMessage(m);
        }
        model.commit(model.CHAT);
    }

    public void errorContact(String message){
        view.errorContact(message);
    }

}