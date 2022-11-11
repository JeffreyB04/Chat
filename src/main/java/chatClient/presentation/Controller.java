package chatClient.presentation;

import chatClient.logic.ServiceProxy;
import chatProtocol.Message;
import chatProtocol.User;

import java.io.IOException;
import java.util.ArrayList;

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
        User logged=ServiceProxy.instance().login(u); //solo los que esten ahi
        model.setCurrentUser(logged);
        model.commit(Model.USER);
    }

    public void post(String text,int row){
        String id = model.getUsers().get(row).getId();
        User u=null;

        Message message = new Message();
        message.setMessage(text);
        message.setSender(model.getCurrentUser());
        message.setReceiver(model.getSelected());
        ServiceProxy.instance().post(message);
        model.commit(Model.CHAT);
    }

    public void register(User u) throws Exception {
        ServiceProxy.instance().register(u);
        model.setCurrentUser(u);
        model.commit(Model.USER);
    }

    public void logout(){
        try {
            ServiceProxy.instance().logout(model.getCurrentUser());
            model.setMessages(new ArrayList<>());
            model.commit(Model.CHAT);
        } catch (Exception ex) {
        }
        model.setCurrentUser(null);
        model.commit(Model.USER+Model.CHAT);
    }
        
    public void deliver(Message message){
        model.messages.add(message);
        model.commit(Model.CHAT);       
    }

    public void obtieneSelected(int row) throws IOException {
        model.selected = model.getUsers().get(row);
        model.getUsers().set(row,model.selected);
    }

}
