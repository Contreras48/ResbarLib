
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
    public static List<Categoria> obtener(boolean producto) throws ErrorAplicacion{
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
            throw new ErrorAplicacion("ManejadorCategorias.Obtener()$Error: " + ex.getMessage());
        }finally{
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ManejadorProductos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                cn.cerrar();
            } catch (SQLException ex) {
                Logger.getLogger(ManejadorCategorias.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return categorias;
    }  
    
    /* Si se desea modificar el objeto “categoria” este actualizara en la base de datos cuando este ya este 
       modoficado, no se modificara el IdCategoria */
    public static void actualizar(Categoria c) throws ErrorAplicacion{
        if(!c.nombre.isEmpty() && c.nombre != null) {
            Conexion cn = new Conexion();
            try {
                String sql = "UPDATE Categoria SET nombre = '"+c.nombre+"' WHERE idCategoria = '"+c.idCategoria+"'";
                cn.UID(sql);
            } catch (ClassNotFoundException | SQLException ex) {
               throw new ErrorAplicacion("ManejadorCategorias.actualiazar()$Error: "+ ex.getMessage());
            }finally{
                try {
                    cn.cerrar();
                } catch (SQLException ex) {
                    throw new ErrorAplicacion("ManejadorCategorias.actualiazar()$Error: "+ ex.getMessage());
                }
            }
        }else{
            throw new ErrorAplicacion("ManejadorCategorias.actualizar()$El campo nombre no debe estar vacio");
        }
    }
    
     /* Agrega el objeto “categoria” a la base de datos. */
    public static void insertar(Categoria c) throws ErrorAplicacion{
        if(c.idCategoria > 0 && (!c.nombre.isEmpty() || c.nombre != null)){
            Conexion cn = new Conexion();
            String sql = "INSERT INTO Categoria(idCategoria, nombre) VALUES('"+c.idCategoria+"', '"+c.nombre+"')";
            try {
                cn.UID(sql);
            } catch (ClassNotFoundException | SQLException ex) {
                throw new ErrorAplicacion("ManejadorCategorias.insertar()$Error: "+ ex.getMessage());
            }finally{
                try {
                    cn.cerrar();
                } catch (SQLException ex) {
                    throw new ErrorAplicacion("ManejadorCategorias.insertar()$Error: "+ ex.getMessage());
                }
            }
            
        }else{
            throw new ErrorAplicacion("ManejadorCategorias.insertar()$los campos idCategoria, nombre son invalidos o estan vacios");
        }
    }
    
    /* Elimina el objeto “categoria” a la base de datos. */
    public static void eliminar(Categoria c) throws ErrorAplicacion{
        if(c.idCategoria != null && c.idCategoria > 0){
            Conexion cn = new Conexion();
            String sql = "DELETE FROM Categoria WHERE idCategoria = '"+c.idCategoria+"'";
            try {
                cn.UID(sql);
            } catch (ClassNotFoundException | SQLException ex) {
                throw new ErrorAplicacion("ManejadorCategorias.eliminar()$CError: "+ex.getMessage());
            }finally{
                try {
                    cn.cerrar();
                } catch (SQLException ex) {
                     throw new ErrorAplicacion("ManejadorCategorias.eliminar()$Error: "+ex.getMessage());
                }
            }
        }else{
            throw new ErrorAplicacion("ManejadorCategorias.eliminar()$Campo idCategoria vacio o es negativo");
        }
    }
    
    /* Obtiene el identificador de categoria, va la base de datos a obtener el ultimo ID de categoria y le suma 
       uno a dicho valor. */
    public static Integer obtenerId() throws ErrorAplicacion{
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
