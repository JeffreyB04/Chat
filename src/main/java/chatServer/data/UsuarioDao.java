package chatServer.data;

import chatProtocol.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDao {
    Database db;

    public UsuarioDao() {
        db = Database.instance();
    }

    public void create(User e) throws Exception {
        String sql = "insert into " +
                "User " +
                "(id, clave, nombre) " +
                "values(?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getId());
        stm.setString(2, e.getClave());
        stm.setString(3, e.getNombre());
        db.executeUpdate(stm);
    }

    public User read(String id) throws Exception {
        String sql = "select " +
                "* " +
                "from  User s " +
                "where s.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "s");
        } else {
            throw new Exception("User NO EXISTE");
        }
    }


    public User from(ResultSet rs, String alias) throws Exception {
        User e = new User(); //constructor
        e.setId(rs.getString(alias + ".id"));
        e.setClave(rs.getString(alias + ".clave"));
        e.setNombre(rs.getString(alias + ".nombre"));
        return e;
    }
    public User getUser(String id) throws Exception {
        String sql = "SELECT " +
                "* " +
                "from  User u " +
                "where u.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "u");
        } else {
            throw new Exception("USUARIO NO EXISTE");
        }
    }


}