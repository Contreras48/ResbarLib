
package Acceso;

import java.sql.Connection;
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

}
