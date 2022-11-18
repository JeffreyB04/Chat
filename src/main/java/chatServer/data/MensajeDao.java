package chatServer.data;
import chatProtocol.Message;
import chatProtocol.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
public class MensajeDao {
    Database db;
    UsuarioDao usuarioDao;

    public MensajeDao() {
        db = Database.instance();
        UsuarioDao usuarioDao;
    }

    public void create(Message m) throws Exception {  //crea cuando no esta logeado
        String sql = "insert into " +
                "Message " +
                "(sender, message, receiver) " +
                "values(?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getSender().getId());
        stm.setString(2, m.getMessage());
        stm.setString(3, m.getReceiver().getId());
        db.executeUpdate(stm);
    }

    public void delete(Message m) throws Exception { //borra cuando lo manda
        String sql = "delete " +
                "from Message " +
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getMessage());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Message NO EXISTE");
        }
    }

    public void insertMessage(Message m) throws Exception{
        String sql = "INSERT into " +
                "Message " +
                "(sender, message, receiver) " +
                "values(?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getSender().getId());
        stm.setString(2, m.getMessage());
        stm.setString(3, m.getReceiver().getId());
        db.executeUpdate(stm);
    }
    public List<Message> unReadMessages(String receiver) throws Exception {
        List<Message> unReadMessages = new ArrayList<>();
        String sql = "SELECT " +
                "* " +
                "from Message u " +
                "where u.receiver=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, receiver);
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            unReadMessages.add(from(rs, "u"));
        }
        return unReadMessages;
    }

    public void deleteReadMessages(String receiver) throws Exception {
        String sql = "DELETE " +
                "from Message u " +
                "where u.receiver=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, receiver);
        db.executeUpdate(stm);
    }

    public Message from(ResultSet rs, String alias) throws Exception {
        Message m = new Message();
        m.setSender(usuarioDao.getUser(rs.getString(alias + ".sender")));
        m.setMessage(rs.getString(alias + ".message"));
        m.setReceiver(usuarioDao.getUser(rs.getString(alias + ".receiver")));
        return m;
    }
}
