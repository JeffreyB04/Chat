package chatClient.presentation;

import chatClient.Application;
import chatClient.logic.ServiceData;
import chatProtocol.Message;
import chatProtocol.User;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observer;

public class View implements Observer {
    private JPanel panel;
    private JPanel loginPanel;
    private JPanel bodyPanel;
    private JTextField id;
    private JPasswordField clave;
    private JButton login;
    private JButton finish;
    private JTextPane messages;
    private JTextField mensaje;
    private JButton post;
    private JButton logout;
    private JButton register;
    private JTable contactos;
    private JTextField contactFld;
    private JButton buscarButton;
    private JTextField contactoFld;
    private JButton contactoButton;

    Model model;
    Controller controller;
    public View() {
        loginPanel.setVisible(true);
        Application.window.getRootPane().setDefaultButton(login);
        bodyPanel.setVisible(false);

        DefaultCaret caret = (DefaultCaret) messages.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User u = new User(id.getText(), new String(clave.getPassword()), "");
                id.setBackground(Color.white);
                clave.setBackground(Color.white);
                try {
                    controller.login(u);
                    id.setText("");
                    clave.setText("");
                } catch (Exception ex) {
                    id.setBackground(Color.orange);
                    clave.setBackground(Color.orange);
                }
            }
        });

        contactos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                 super.mouseClicked(e);
                //if (e.getClickCount() == 1) {
                    contactoFld.setText(controller.getContact(contactos.getSelectedRow()).getId());
                    contactoFld.setVisible(true);
                    controller.changeContact(contactoFld.getText());
                //}
            }
        });


        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.logout();
            }
        });
        finish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        post.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (id.getText() != ""){
                    String text = mensaje.getText();
                    mensaje.setText("");
                        controller.post(text, id.getText());
                } else {
                    JOptionPane.showMessageDialog(panel, "Select a contact","ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField nombre=new JTextField("");
                Object[] fields={"Nombre: ", nombre};
                int option=JOptionPane.showConfirmDialog(panel, fields, id.getText(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if(option==JOptionPane.OK_OPTION){
                    try{
                        controller.register(new User(id.getText(), new String(clave.getPassword()), nombre.getText()/*,"ONLINE"*/));
                    }
                    catch (Exception ex){
                        JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        contactoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contactoFld.setBackground(Color.white);
                try {
                    User u = new User(contactoFld.getText(), " "," ");
                    controller.checkContact(u.getId());
                    contactoFld.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(contactFld.getText().isEmpty()){
                        controller.actualizarContacttos();
                    }
                    else {
                        controller.buscarContactos(contactFld.getText());
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void setModel(Model model) {
        this.model = model;
        model.addObserver(this);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
    public void errorContact(String message){
        JOptionPane.showMessageDialog(panel, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    public JPanel getPanel() {
        return panel;
    }

    String backStyle = "margin:0px; background-color:#e6e6e6;";
    String senderStyle = "background-color:#c2f0c2;margin-left:30px; margin-right:5px;margin-top:3px; padding:2px; border-radius: 25px;";
    String receiverStyle = "background-color:white; margin-left:5px; margin-right:30px; margin-top:3px; padding:2px;";

    public void update(java.util.Observable updatedModel, Object properties) {

        int prop = (int) properties;
        if (model.getCurrentUser() == null) {
            Application.window.setTitle("CHAT");
            loginPanel.setVisible(true);
            Application.window.getRootPane().setDefaultButton(login);
            bodyPanel.setVisible(false);
        } else {
            Application.window.setTitle(model.getCurrentUser().getNombre().toUpperCase());
            loginPanel.setVisible(false);
            bodyPanel.setVisible(true);
            Application.window.getRootPane().setDefaultButton(post);
            if ((prop & Model.CHAT) == Model.CHAT) {
                this.messages.setText("");
                String html = "<div style="+backStyle+">";
                for (Message m : ServiceData.instance().searchMessage(model.getCurrentUser().getId())) {
                    if (m.getSender().equals(model.getCurrentUser()) && m.getReceiver().equals(model.getContact(model.id))) {
                        html += ("<div style=" + senderStyle + ">" + "Me: " + m.getMessage() + "</div>");
                    }
                    if (m.getReceiver().equals(model.getCurrentUser()) && m.getSender().equals(model.getContact(model.id))) {
                        html += ("<div style=" + receiverStyle + ">" + m.getSender().getNombre() + ": " + m.getMessage() + "</div>");
                    }
                }
                html += "</div>";
                this.messages.setText(html);
            }
        }

        int[] cols = {TableModel.ESTADO, TableModel.NOMBRE};
        contactos.setModel(new TableModel(cols, model.getContactsList()));
        contactos.setRowHeight(30);

        panel.validate();
    }
}
