
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
public class ManejadorProductos {
    
    /* Realiza una petición a la base de datos y devuelve una colección de objetos productos que se 
       corresponden con el Identificador de categoría que se pasó como parámetro. */
    public static List<Producto> obtenerxCategoria(Integer idCategoria){
        Conexion cn = new Conexion();
        ResultSet rs;
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE idCategoria = '"+idCategoria+"'";
        try {
            rs = cn.consultar(sql);
            while(rs.next()){
                Producto p = new Producto();
                p.idProducto = rs.getInt(1);
                p.nombre = rs.getString(2);
                p.precio = rs.getDouble(3);
                ResultSet rs2 = cn.consultar("SELECT * FROM Categoria WHERE idCategoria = '"+idCategoria+"'");
                while(rs2.next()){
                    Categoria c = new Categoria();
                    c.idCategoria = rs2.getInt(1);
                    c.nombre = rs2.getString(2);
                    p.categoria = c;
                }
                p.area = rs.getString(5).charAt(0);
                productos.add(p);
            }
            cn.cerrar();
        } catch (SQLException | ClassNotFoundException ex) {
            try {
                throw new ErrorAplicacion("ManejadorPrductos.obtenerxCategoria()$Error: "+ex.getMessage());
            } catch (ErrorAplicacion ex1) {
                Logger.getLogger(ManejadorProductos.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return productos;
    }
    
    /* Toma la cadena pasada como parametro como criterio de búsqueda, para ir a la base de datos y buscar 
       todos los productos cuyo Id o nombre coincida con el criterio de búsqueda, luego devuelve la colección 
       de productos, sin devolver productos duplicados */
    public static List<Producto> buscar(String cadena){
        Conexion cn = new Conexion();
        ResultSet rs;
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE idCategoria LIKE '%"+cadena+"%' OR nombre LIKE '%"+cadena+"%'";
        try {
            rs = cn.consultar(sql);
            while(rs.next()){
                Producto p = new Producto();
                p.idProducto = rs.getInt(1);
                p.nombre = rs.getString(2);
                p.precio = rs.getDouble(3);
//                ResultSet rs2 = cn.consultar("SELECT * FROM Categoria WHERE idCategoria = '"+idCategoria+"'");
//                while(rs2.next()){
//                    Categoria c = new Categoria();
//                    c.setIdCategoria(rs2.getInt(1));
//                    c.setNombre(rs.getString(2));
//                    p.setCategoria(c);
//                }
                p.area = rs.getString(5).charAt(0);
                productos.add(p);
            }
            cn.cerrar();
        } catch (SQLException | ClassNotFoundException ex) {
            try {
                throw new ErrorAplicacion("ManejadorPrductos.buscar()$Error: "+ex.getMessage());
            } catch (ErrorAplicacion ex1) {
                Logger.getLogger(ManejadorProductos.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return productos;
    }
    
    /* Agrega el objeto “producto” a la base de datos. */
    public static void insertar(Producto p){
        Conexion cn = new Conexion();
        String sql = "INSERT INTO Producto(idProducto, nombre, precio, idCategoria, area) VALUES('"+p.idProducto+"', '"+p.nombre+"', '"+p.precio+"', '"+p.categoria.idCategoria+"', '"+p.area+"')";
        cn.UID(sql);
        cn.cerrar();
    }
    
    /* Si el objeto “producto” se desea modificar este actualizara en la base de datos cuando este ya este 
    modificado, no se modificara el ID del producto solo sus otros campos */
    public static void actualizar(Producto p){
        Conexion cn = new Conexion();
        String sql = "UPDATE Producto SET nombre = '"+p.nombre+"', precio = '"+p.precio+"', idCategoria = '"+p.categoria.idCategoria+"', area = '"+p.area+"' WHERE idProducto = '"+p.idProducto+"'";
        cn.UID(sql);
        cn.cerrar();
    }
    
    /* Elimina el objeto “producto” de la base de datos. */
    public static void eliminar(Producto p){
        Conexion cn = new Conexion();
        String sql = "DELETE FROM Producto WHERE idProducto = '"+p.idProducto+"'";
        cn.UID(sql);
        cn.cerrar();
    }
    
    /* Realiza una petición a la base de datos y devuelve un objeto producto que cuyo IDProducto coincide con 
       el valor del parámetro */
    public static Producto obtener(Integer idProducto){
        Conexion cn = new Conexion();
        ResultSet rs;
        Producto p = new Producto();
        String sql = "SELECT * FROM Producto WHERE idProducto = '"+idProducto+"'";
        try {
            rs = cn.consultar(sql);
            while(rs.next()){
                p.idProducto = rs.getInt(1);
                p.nombre = rs.getString(2);
                p.precio = rs.getDouble(3);
//                ResultSet rs2 = cn.consultar("SELECT * FROM Categoria WHERE idCategoria = '"+idCategoria+"'");
//                while(rs2.next()){
//                    Categoria c = new Categoria();
//                    c.setIdCategoria(rs2.getInt(1));
//                    c.setNombre(rs.getString(2));
//                    p.setCategoria(c);
//                }
                p.area = rs.getString(5).charAt(0);
            }
            cn.cerrar();
        } catch (SQLException | ClassNotFoundException ex) {
            try {
                throw new ErrorAplicacion("ManejadorPrductos.obtener()$Error: "+ex.getMessage());
            } catch (ErrorAplicacion ex1) {
                Logger.getLogger(ManejadorProductos.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return p;
    }
    
    /* Obtiene el identificador de producto, va la base de datos a obtener el ultimo ID deproducto y le suma 
       uno a dicho valor */
    public static Integer obtenerId(){
        Conexion cn = new Conexion();
        String sql = "SELECT MAX(idProducto) FROM Producto";
        int id = 0;
        try {
            ResultSet rs = cn.consultar(sql);
            while(rs.next()){
                id = rs.getInt(1);
            }
            cn.cerrar();
        } catch (SQLException | ClassNotFoundException ex) {
            try {
                throw new ErrorAplicacion("ManejadorCategorias.obtenerId()$Error: "+ex.getMessage());
            } catch (ErrorAplicacion ex1) {
                Logger.getLogger(ManejadorCategorias.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return id + 1;
    }
    
}
