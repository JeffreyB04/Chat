package chatServer.data;

import chatProtocol.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {
    Database db;

    public UsuarioDao() {
        db = Database.instance();
    }

    public void create(User e) throws Exception {
        String sql = "insert into " +
                "User " +
                "(id, clave, nombre, estado) " +
                "values(?,?,?)";                    //ellas tiene 3 ?, pero son 4 parametros
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getId());
        stm.setString(2, e.getClave());
        stm.setString(3, e.getNombre());
        //stm.setString(4, e.getEstado());
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

    public void update(User e) throws Exception {
        String sql = "update " +
                "User " +
                "set clave=?, nombre=?  " +
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getClave());
        stm.setString(2, e.getNombre());
        stm.setString(3, e.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("User NO EXISTE");
        }

    }

    public void delete(User e) throws Exception {
        String sql = "delete " +
                "from User " +
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("User NO EXISTE");
        }
    }

    public List<User> findById(String id) throws Exception {
        List<User> resultado = new ArrayList<User>();
        String sql = "select * " +
                "from " +
                "user s " +
                "where s.id like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + id + "%");
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            resultado.add(from(rs, "s"));
        }
        return resultado;
        //return resultado.stream().filter(s -> s.getReferencia().contains(referencia)).collect(Collectors.toList());
    }
    public User from(ResultSet rs, String alias) throws Exception {
        User e = new User(); //constructor
        e.setId(rs.getString(alias + ".id"));
        e.setClave(rs.getString(alias + ".clave"));
        e.setNombre(rs.getString(alias + ".nombre"));
        //e.setEstado(rs.getString(alias + ".estado"));
        return e;
    }



}