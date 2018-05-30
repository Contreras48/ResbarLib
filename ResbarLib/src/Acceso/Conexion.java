
package Acceso;

import java.sql.Connection;
import java.sql.DriverManager;
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

    private static Connection cnx = null;
    private static Statement sttm = null;
    private static ResultSet rst = null;
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String db = "resbar";
    private final String url = "jdbc:mysql://localhost:3306/" + db;
    private final String usuario = "resbar";
    private final String contraseña = "Restaurante2018";

    public Connection conectar() {
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
        return cnx;
    }
    
    public void cerrar() {
        if (cnx != null) {
            try {
                cnx.close();
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
            System.exit(1);
        }
        return rst;
    }

}
