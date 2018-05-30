
package Modelo;

import Acceso.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mateo
 */
public class ManejadorParametros {
    
    /* Va a la base de datos y obtiene todos los parametros que están en dicha tabla, devolviendo una colección 
       de objetos parametros. */
    public static List<Parametro> obtener(){
        Conexion cn = new Conexion();
        ResultSet rs = null;
        List<Parametro> parametros = new ArrayList<>();
        try {
            rs = cn.consultar("SELECT * FROM Parametro");
            while(rs.next()){
                Parametro p = new Parametro();
                p.idParametro = rs.getInt(1);
                p.nombre = rs.getString(2);
                p.valor = rs.getString(3);
                parametros.add(p);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            try {
                throw new ErrorAplicacion("ManejadorParametros.obtener()$Error: "+ex.getMessage());
            } catch (ErrorAplicacion ex1) {
                Logger.getLogger(ManejadorParametros.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }finally{
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ManejadorProductos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            cn.cerrar();
        }
        return parametros;
    }
    
    /* Actualiza el valor del parámetro en la base de datos, no se puede actualizar ni el ID, ni el nombre, solo 
       se puede modificar el campo valor */
    public void actualizar(Parametro p){
        Conexion cn = new Conexion();
        String sql = "UPDATE Parametro SET valor = '"+p.valor+"' WHERE idParametro = '"+p.idParametro+"'";
        cn.UID(sql);
        cn.cerrar();
    }
    
    /* Toma el IDparametro y busca en la base de datos una tupla que coincida con dicho ID, luego devuelve un 
       objeto parametro construido acorde a la tupla */
    public static Parametro obtener(int idParametro){
        Conexion cn = new Conexion();
        ResultSet rs = null;
        Parametro p = new Parametro();
        String sql = "SELECT * FROM Parametro WHERE idParametro = '"+idParametro+"'";
        try {
            rs = cn.consultar(sql);
            while(rs.next()){
                p.idParametro = rs.getInt(1);
                p.nombre = rs.getString(2);
                p.valor = rs.getString(3);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            try {
                throw new ErrorAplicacion("ManejadorParametros.obtener()$Error: "+ex.getMessage());
            } catch (ErrorAplicacion ex1) {
                Logger.getLogger(ManejadorProductos.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }finally{
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ManejadorProductos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            cn.cerrar();
        }
        return p;
    }
}
