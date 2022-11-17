package chatClient.presentation;

import chatClient.Application;
import chatProtocol.Message;
import chatProtocol.User;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
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

    //a la hora de buscar se busca localmente en el data

    public View() {
        loginPanel.setVisible(true);
        Application.window.getRootPane().setDefaultButton(login);
        bodyPanel.setVisible(false);

        DefaultCaret caret = (DefaultCaret) messages.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User u = new User(id.getText(), new String(clave.getPassword()), ""/*,"ONLINE"*/);
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
                if (e.getClickCount() >= 1){
                    int row = contactos.getSelectedRow();
                    model.setSelected(model.getContactsList().get(row));
                }
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
                String text = mensaje.getText();
                try {
                    controller.post(text);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                mensaje.setText("");
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
                    controller.checkContact(u);
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
                        controller.updateContacts();
                    }
                    else {
                        controller.searchContacts(contactFld.getText());
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
            contactos.setVisible(true);

            Application.window.getRootPane().setDefaultButton(post);
            if ((prop & Model.CHAT) == Model.CHAT) {
                this.messages.setText("");
                String text = "";
                for (Message m : model.getMessages()) {
                    if (m.getSender().equals(model.getCurrentUser())) {
                        text += ("Me:" + m.getMessage() + "\n");
                    }
                    else {
                        text += (m.getSender().getNombre() + ": " + m.getMessage() + "\n");
                    }
                }
                this.messages.setText(text);
            }
            //this.mensaje.setText("");
        }
        int[] cols = {TableModel.ESTADO, TableModel.NOMBRE};
        contactos.setModel(new TableModel(cols, model.getContactsList()));
        contactos.setRowHeight(30);

        panel.validate();
    }
}
