
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
public class ManejadorCategorias extends Conexion{
    
    /* Realiza una petición a la base de datos y devuelve una colección de categorías, si el valor del parametro 
       es TRUE cargara todos los productos que están en esa categoría, si el valor del parametro es FALSE la 
       propiedad “productos” de cada categoría será NULL */
    public static List<Categoria> obtener(boolean producto){
        Conexion cn = new Conexion();
        String sql = "SELECT * FROM Categoria";
        ResultSet rs = null;
        List<Categoria> categorias = new ArrayList<>();
        try {
            rs = cn.consultar(sql);
            while(rs.next()){
                Categoria c = new Categoria();
                c.idCategoria = rs.getInt("idCategoria");
                c.nombre = rs.getString("nombre");
                if(producto == true){
                    List<Producto> productos = new ArrayList<>();
                    try (ResultSet rs2 = cn.consultar("SELECT * FROM Producto WHERE idCategoria = '"+c.idCategoria+"'")) {
                        while(rs2.next()){
                            Producto p = new Producto();
                            p.idProducto = rs2.getInt(1);
                            p.nombre = rs2.getString(2);
                            p.precio = rs2.getDouble(3);
                            p.categoria = c;
                            p.area = rs2.getString(5).charAt(0);
                            productos.add(p);
                        }
                    }
                    c.productos = productos;
                }
                categorias.add(c);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            try {
                throw new ErrorAplicacion("ManejadorCategorias.Obtener()$Error: " + ex.getMessage());
            } catch (ErrorAplicacion ex1) {
                Logger.getLogger(ManejadorCategorias.class.getName()).log(Level.SEVERE, null, ex1);
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
        return categorias;
    }  
    
    /* Si se desea modificar el objeto “categoria” este actualizara en la base de datos cuando este ya este 
       modoficado, no se modificara el IdCategoria */
    public static void actualizar(Categoria c){
        if(!c.nombre.isEmpty()){
            Conexion cn = new Conexion();
            String sql = "UPDATE Categoria SET nombre = '"+c.nombre+"' WHERE idCategoria = '"+c.idCategoria+"'";
            cn.UID(sql);
            cn.cerrar();
        }else{
            try {
                throw new ErrorAplicacion("ManejadorCategorias.actualizar()$El campo nombre no debe estar vacio");
            } catch (ErrorAplicacion ex) {
                Logger.getLogger(ManejadorCategorias.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
     /* Agrega el objeto “categoria” a la base de datos. */
    public static void insertar(Categoria c){
        if(c.idCategoria != null && !c.nombre.isEmpty()){
            Conexion cn = new Conexion();
            String sql = "INSERT INTO Categoria(idCategoria, nombre) VALUES('"+c.idCategoria+"', '"+c.nombre+"')";
            cn.UID(sql);
            cn.cerrar();
        }else{
            try {
                throw new ErrorAplicacion("ManejadorCategorias.insertar()$los campos idCategoria y nombre no pueden ir vacios");
            } catch (ErrorAplicacion ex) {
                Logger.getLogger(ManejadorCategorias.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /* Elimina el objeto “categoria” a la base de datos. */
    public static void eliminar(Categoria c){
        if(c.idCategoria != null){
            Conexion cn = new Conexion();
            String sql = "DELETE FROM Categoria WHERE idCategoria = '"+c.idCategoria+"'";
            cn.UID(sql);
            cn.cerrar();
        }else{
            try {
                throw new ErrorAplicacion("ManejadorCategorias.eliminar()$Campo idCategoria vacio");
            } catch (ErrorAplicacion ex) {
                Logger.getLogger(ManejadorCategorias.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /* Obtiene el identificador de categoria, va la base de datos a obtener el ultimo ID de categoria y le suma 
       uno a dicho valor. */
    public static Integer obtenerId(){
        Conexion cn = new Conexion();
        ResultSet rs = null;
        String sql = "SELECT MAX(idCategoria) FROM Categoria";
        int id = 0;
        try {
            rs = cn.consultar(sql);
            while(rs.next()){
                id = rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            try {
                throw new ErrorAplicacion("ManejadorCategorias.obtenerId()$Error: "+ex.getMessage());
            } catch (ErrorAplicacion ex1) {
                Logger.getLogger(ManejadorCategorias.class.getName()).log(Level.SEVERE, null, ex1);
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

        return id + 1;
    }
    
}
