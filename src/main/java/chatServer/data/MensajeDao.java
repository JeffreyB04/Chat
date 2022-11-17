package chatServer.data;
import chatProtocol.Message;
import chatProtocol.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
public class MensajeDao {
    Database db;

    public MensajeDao() {
        db = Database.instance();
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
}
