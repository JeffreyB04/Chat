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

    public void create(Message m) throws Exception {
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
}
