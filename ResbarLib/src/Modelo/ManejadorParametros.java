
package Modelo;

import Acceso.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mateo
 */
public class ManejadorParametros {
    
    /* Va a la base de datos y obtiene todos los parametros que están en dicha tabla, devolviendo una colección 
       de objetos parametros. */
    public static List<Parametro> obtener() throws ErrorAplicacion{
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
                throw new ErrorAplicacion("ManejadorParametros.obtener()$Error: "+ex.getMessage());
        }finally{
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    throw new ErrorAplicacion("ManejadorParametros.obtener()$Error: "+ex.getMessage());
                }
            }
            try {
                cn.cerrar();
            } catch (SQLException ex) {
                throw new ErrorAplicacion("ManejadorParametros.obtener()$Error: "+ex.getMessage());
            }
        }
        return parametros;
    }
    
    /* Actualiza el valor del parámetro en la base de datos, no se puede actualizar ni el ID, ni el nombre, solo 
       se puede modificar el campo valor */
    public static void actualizar(Parametro p) throws ErrorAplicacion{
        Conexion cn = new Conexion();
        String sql = "UPDATE Parametro SET valor = '"+p.valor+"' WHERE idParametro = '"+p.idParametro+"'";
        if (!p.valor.isEmpty() && p.valor != null && p.idParametro>0 && p.nombre.isEmpty() && p.nombre==null ) {
            try {
                cn.UID(sql);
            } catch (ClassNotFoundException | SQLException ex) {
                throw new ErrorAplicacion("ManejadorParametros.actualizar()$Error: " + ex.getMessage());
            } finally {
                try {
                    cn.cerrar();
                } catch (SQLException ex) {
                    throw new ErrorAplicacion("ManejadorParametros.actualizar()$Error: " + ex.getMessage());
                }
            }
        }else{
            if(p.idParametro<=0){
                throw new ErrorAplicacion("ManejadorCategorias.actualizar()$El campo Id Categoria debe de ser mayor a 0");
            }else{
                throw new ErrorAplicacion("ManejadorCategorias.actualizar()$El campo nombre o valor no deben estar vacio");
            }
        }
   
    }
    
    /* Toma el IDparametro y busca en la base de datos una tupla que coincida con dicho ID, luego devuelve un 
       objeto parametro construido acorde a la tupla */
    public static Parametro obtener(int idParametro) throws ErrorAplicacion {
        Conexion cn = new Conexion();
        ResultSet rs = null;
        Parametro p = new Parametro();
        String sql = "SELECT * FROM Parametro WHERE idParametro = '" + idParametro + "'";
        if (idParametro > 0) {
            try {
                rs = cn.consultar(sql);
                while (rs.next()) {
                    p.idParametro = rs.getInt(1);
                    p.nombre = rs.getString(2);
                    p.valor = rs.getString(3);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                throw new ErrorAplicacion("ManejadorParametros.obtener()$Error: " + ex.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        throw new ErrorAplicacion("ManejadorParametros.obtener()$Error: " + ex.getMessage());
                    }
                }
                try {
                    cn.cerrar();
                } catch (SQLException ex) {
                    throw new ErrorAplicacion("ManejadorParametros.obtener()$Error: " + ex.getMessage());
                }
            }
        } else {
            throw new ErrorAplicacion("ManejadorParametros.obtener()$El id no puede ser menor o igual a cero");
        }
        return p;
    }
}
