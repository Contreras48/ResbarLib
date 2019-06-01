
package Acceso;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author mateo
 */
public class Conexion {

    private Connection cnx = null;
    private Statement sttm = null;
    private PreparedStatement psttm = null;
    private ResultSet rst = null;
    private String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private String db = "resbar";
    private String url = "jdbc:mysql://localhost:3306/" + db;
    private String usuario = "resbar";
    private String contraseña = "Restaurante2018";

    public void conectar() throws ClassNotFoundException, SQLException {
        if (cnx == null) {
            Class.forName(JDBC_DRIVER);
            cnx = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("Conexion establecida!!");
        }
    }
    
    public void cerrar() throws SQLException {
        if (cnx != null) {
            cnx.close();
            cnx = null;
            System.out.println("Conexion cerrarda");
        }
    }
    
    //método para Update, Insert, Delete
    public void UID(String sql) throws ClassNotFoundException, SQLException {
        conectar();
        sttm = cnx.createStatement();
        sttm.executeUpdate(sql);   
    }

    //Método para Consultar
    public ResultSet consultar(String sql) throws SQLException, ClassNotFoundException {
        conectar();
        sttm = cnx.createStatement();
        rst = sttm.executeQuery(sql);
        return rst;
    }
    
    public ResultSet buscarProductos(String sql, String busqueda) throws ClassNotFoundException, SQLException{
        conectar();
        psttm = cnx.prepareStatement(sql);
        psttm.setString(1, "%" + busqueda + "%");
        psttm.setString(2, "%" + busqueda + "%");
        rst = psttm.executeQuery();
        return rst;
    }
    
    public ResultSet buscarOrdenesActivas(String sql, String busqueda) throws ClassNotFoundException, SQLException{
        conectar();
        psttm = cnx.prepareStatement(sql);
        psttm.setString(1, "%" + busqueda + "%");
        psttm.setString(2, "%" + busqueda + "%");
        psttm.setString(3, "%" + busqueda + "%");
        psttm.setString(4, "%" + busqueda + "%");
        rst = psttm.executeQuery();
        return rst;
    }
    
    public void actualizarOrden(String sql,int id, String mesero, String mesa, String cliente, Date fecha, String comentario, double total, boolean activa) throws ClassNotFoundException, SQLException{
        conectar();
        psttm = cnx.prepareStatement(sql);
        psttm.setString(1, mesero);
        psttm.setString(2, mesa);
        psttm.setString(3, cliente);
        psttm.setDate(4, fecha);
        psttm.setString(5, comentario);
        psttm.setDouble(6, total);
        psttm.setBoolean(7, activa);
        psttm.setInt(8, id);
        psttm.executeUpdate();
        psttm.close();
    }
    
    public void agregarOrden(String sql,int id, String mesero, String mesa, String cliente, Date fecha, String comentario, double total, boolean activa) throws ClassNotFoundException, SQLException{
        conectar();
        psttm = cnx.prepareStatement(sql);
        psttm.setInt(1, id);
        psttm.setString(2, mesero);
        psttm.setString(3, mesa);
        psttm.setString(4, cliente);
        psttm.setDate(5, fecha);
        psttm.setString(6, comentario);
        psttm.setDouble(7, total);
        psttm.setBoolean(8, activa);
        psttm.executeUpdate();
        psttm.close();
    }

}
