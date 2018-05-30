
package Acceso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public void conectar() {
        if (cnx == null) {
            try {
                Class.forName(JDBC_DRIVER);
                cnx = DriverManager.getConnection(url, usuario, contraseña);
                System.out.println("Conexion establecida!!");
            } catch (SQLException ex) {
                try {
                    throw new SQLException(ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    
    public void cerrar() {
        if (cnx != null) {
            try {
                cnx.close();
                cnx = null;
            } catch (SQLException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Conexion cerrarda");
        }
    }
    
    //método para Update, Insert, Delete
    public void UID(String sql) {
        try {
            conectar();
            sttm = cnx.createStatement();
            sttm.executeUpdate(sql); //statement
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }

    //Método para Consultar
    public ResultSet consultar(String sql) throws SQLException, ClassNotFoundException {
        try {
            conectar();
            sttm = cnx.createStatement();
            rst = sttm.executeQuery(sql);  //resultset
        }  catch (SQLException e) {
            System.out.println();
            //System.exit(1);
        }
        return rst;
    }
    
    public ResultSet buscarProductos(String sql, String busqueda){
        try {
            conectar();
            psttm = cnx.prepareStatement(sql);
            psttm.setString(1, "%" + busqueda + "%");
            psttm.setString(2, "%" + busqueda + "%");
            rst = psttm.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rst;
    }
    
    public ResultSet buscarOrdenesActivas(String sql, String busqueda){
        try {
            conectar();
            psttm = cnx.prepareStatement(sql);
            psttm.setString(1, "%" + busqueda + "%");
            psttm.setString(2, "%" + busqueda + "%");
            psttm.setString(3, "%" + busqueda + "%");
            psttm.setString(4, "%" + busqueda + "%");
            rst = psttm.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rst;
    }

}
