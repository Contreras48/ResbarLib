
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
public class ManejadorProductos {
    
    /* Realiza una petición a la base de datos y devuelve una colección de objetos productos que se 
       corresponden con el Identificador de categoría que se pasó como parámetro. */
    public static List<Producto> obtenerxCategoria(Integer idCategoria) throws ErrorAplicacion{
        Conexion cn = new Conexion();
        ResultSet rs = null;
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE idCategoria = '"+idCategoria+"'";
        if(idCategoria > 0){
            try {
                rs = cn.consultar(sql);
                while (rs.next()) {
                    Producto p = new Producto();
                    p.idProducto = rs.getInt(1);
                    p.nombre = rs.getString(2);
                    p.precio = rs.getDouble(3);
                    try (ResultSet rs2 = cn.consultar("SELECT * FROM Categoria WHERE idCategoria = '" + idCategoria + "'")) {
                        while (rs2.next()) {
                            Categoria c = new Categoria();
                            c.idCategoria = rs2.getInt(1);
                            c.nombre = rs2.getString(2);
                            p.categoria = c;
                        }
                    }
                    p.area = rs.getString(5).charAt(0);
                    productos.add(p);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                throw new ErrorAplicacion("ManejadorPrductos.obtenerxCategoria()$Error: " + ex.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        throw new ErrorAplicacion("ManejadorPrductos.obtenerxCategoria()$Error: " + ex.getMessage());
                    }
                }
                try {
                    cn.cerrar();
                } catch (SQLException ex) {
                    throw new ErrorAplicacion("ManejadorPrductos.obtenerxCategoria()$Error: " + ex.getMessage());
                }
            }
        }else{
            throw new ErrorAplicacion("ManejadorProductos.obtenerxCategorias()$NO se permiten numeros negativos");
        }
        return productos;
    }
    
    /* Toma la cadena pasada como parametro como criterio de búsqueda, para ir a la base de datos y buscar 
       todos los productos cuyo Id o nombre coincida con el criterio de búsqueda, luego devuelve la colección 
       de productos, sin devolver productos duplicados */
    public static List<Producto> buscar(String busqueda) throws ErrorAplicacion{
        Conexion cn = new Conexion();
        ResultSet rs = null;
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE idProducto LIKE ? OR nombre LIKE ?";
        if(!busqueda.isEmpty()){
            try {
                rs = cn.buscarProductos(sql, busqueda);
                while (rs.next()) {
                    Producto p = new Producto();
                    p.idProducto = rs.getInt(1);
                    p.nombre = rs.getString(2);
                    p.precio = rs.getDouble(3);
                    try (ResultSet rs2 = cn.consultar("SELECT * FROM Categoria WHERE idCategoria = '" + rs.getInt(4) + "'")) {
                        while (rs2.next()) {
                            Categoria c = new Categoria();
                            c.idCategoria = rs2.getInt(1);
                            c.nombre = rs.getString(2);
                            p.categoria = c;
                        }
                    }
                    p.area = rs.getString(5).charAt(0);
                    productos.add(p);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                    throw new ErrorAplicacion("ManejadorPrductos.buscar()$Error: " + ex.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        throw new ErrorAplicacion("ManejadorPrductos.buscar()$Error: " + ex.getMessage());
                    }
                }
                try {
                    cn.cerrar();
                } catch (SQLException ex) {
                    throw new ErrorAplicacion("ManejadorPrductos.buscar()$Error: " + ex.getMessage());
                }
            }
        }else{
            throw new ErrorAplicacion("ManejadorProductos.buscar()$El parametro de busqueda esta vacio o es null");
        }
        return productos;
    }
    
    /* Agrega el objeto “producto” a la base de datos. */
    public static void insertar(Producto p) throws ErrorAplicacion{
        if(p.idProducto > 0 && (!p.nombre.isEmpty() || p.nombre != null) && p.precio > 0 && p.categoria.idCategoria > 0 && (p.area == 'c' || p.area == 'b' )){
            Conexion cn = new Conexion();
            String sql = "INSERT INTO Producto(idProducto, nombre, precio, idCategoria, area) VALUES('"+p.idProducto+"', '"+p.nombre+"', '"+p.precio+"', '"+p.categoria.idCategoria+"', '"+p.area+"')";
            try {
                cn.UID(sql);
            } catch (ClassNotFoundException | SQLException ex) {
                throw new ErrorAplicacion("ManejadorProductos.insertar()$Error: " +ex.getMessage());  
            }
            try { 
                cn.cerrar();
            } catch (SQLException ex) {
                throw new ErrorAplicacion("ManejadorProductos.insertar()$Error: " +ex.getMessage());  
            }
        }else{
            throw new ErrorAplicacion("ManejadorProductos.insertar()$No se ha podido agregar a la base de datos, hay campos vacios o invalidos");  
        }
    }
    
    /* Si el objeto “producto” se desea modificar este actualizara en la base de datos cuando este ya este 
    modificado, no se modificara el ID del producto solo sus otros campos */
    public static void actualizar(Producto p) throws ErrorAplicacion{
        Conexion cn = new Conexion();
        String sql = "UPDATE Producto SET nombre = '"+p.nombre+"', precio = '"+p.precio+"', idCategoria = '"+p.categoria.idCategoria+"', area = '"+p.area+"' WHERE idProducto = '"+p.idProducto+"'";
        
        if(p.idProducto > 0 && (!p.nombre.isEmpty() || p.nombre != null) && p.precio > 0 && p.categoria.idCategoria > 0 && (p.area == 'c' || p.area == 'b' )){
            try {
                cn.UID(sql);
            } catch (ClassNotFoundException | SQLException ex) {
                throw new ErrorAplicacion("ManejadorProductos.actualizar()$"+ex.getMessage());
            }finally{
                try {
                    cn.cerrar();
                } catch (SQLException ex) {
                   throw new ErrorAplicacion("ManejadorProductos.actualizar()$"+ex.getMessage());
                }
            }
           
        }else{
            throw new ErrorAplicacion("ManejadorProductos.insertar()$No se ha podido agregar a la base de datos, hay campos vacios o invalidos");
        }
    }
    
    /* Elimina el objeto “producto” de la base de datos. */
    public static void eliminar(Producto p) throws ErrorAplicacion{
        Conexion cn = new Conexion();
        String sql = "DELETE FROM Producto WHERE idProducto = '"+p.idProducto+"'";
        if(p.idProducto > 0){
            try {
                cn.UID(sql);
            } catch (ClassNotFoundException | SQLException ex) {
                throw new ErrorAplicacion("ManejadorProductos.eliminar()$"+ex.getMessage());
            }finally{
                try {
                    cn.cerrar();
                } catch (SQLException ex) {
                   throw new ErrorAplicacion("ManejadorProductos.eliminar()$Error: "+ex.getMessage());
                }
            }
        }else{
            throw new ErrorAplicacion("ManejadorProductos.eliminar()$No se permite id menores o iguales a cero");
        }
    }
    
    /* Realiza una petición a la base de datos y devuelve un objeto producto que cuyo IDProducto coincide con 
       el valor del parámetro */
    public static Producto obtener(Integer idProducto) throws ErrorAplicacion{
        Conexion cn = new Conexion();
        ResultSet rs = null;
        Producto p = new Producto();
        String sql = "SELECT * FROM Producto WHERE idProducto = '"+idProducto+"'";
        if(idProducto > 0){
            try {
                rs = cn.consultar(sql);
                while (rs.next()) {
                    p.idProducto = rs.getInt(1);
                    p.nombre = rs.getString(2);
                    p.precio = rs.getDouble(3);
                    try (ResultSet rs2 = cn.consultar("SELECT * FROM Categoria WHERE idCategoria = '" + rs.getInt(4) + "'")) {
                        while (rs2.next()) {
                            Categoria c = new Categoria();
                            c.idCategoria = rs2.getInt(1);
                            c.nombre = rs.getString(2);
                            p.categoria = c;
                        }
                    }
                    p.area = rs.getString(5).charAt(0);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                throw new ErrorAplicacion("ManejadorPrductos.obtener()$Error: " + ex.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        throw new ErrorAplicacion("ManejadorPrductos.obtener()$Error: " + ex.getMessage());
                    }
                }
                try {
                    cn.cerrar();
                } catch (SQLException ex) {
                    throw new ErrorAplicacion("ManejadorPrductos.obtener()$Error: " + ex.getMessage());
                }
            }
        }else{
            throw new ErrorAplicacion("ManejadorPrductos.obtener()$El id no puede ser menor o igual a cero");
        }
        return p;
    }
    
    /* Obtiene el identificador de producto, va la base de datos a obtener el ultimo ID deproducto y le suma 
       uno a dicho valor */
    public static Integer obtenerId() throws ErrorAplicacion{
        Conexion cn = new Conexion();
        ResultSet rs = null;
        String sql = "SELECT MAX(idProducto) FROM Producto";
        int id = 0;
        try {
            rs = cn.consultar(sql);
            while(rs.next()){
                id = rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException ex) {
                throw new ErrorAplicacion("ManejadorCategorias.obtenerId()$Error: "+ex.getMessage());
        }finally{
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    throw new ErrorAplicacion("ManejadorCategorias.obtenerId()$Error: "+ex.getMessage());
                }
            }
            try {
                cn.cerrar();
            } catch (SQLException ex) {
                throw new ErrorAplicacion("ManejadorCategorias.obtenerId()$Error: "+ex.getMessage());
            }
        }

        return id + 1;
    }
    
}
