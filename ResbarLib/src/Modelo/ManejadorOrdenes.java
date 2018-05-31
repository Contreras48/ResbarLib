
package Modelo;

import Acceso.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mateo
 */
public class ManejadorOrdenes {
    
    /* va a la base de datos y filtra todas las ordenes cuyo campo Activa=TRUE, y devuelve un colección de 
       objetos Orden */
    public static List<Orden> ordenesActivas() throws ErrorAplicacion{
        Conexion cn = new Conexion();
        ResultSet rs = null;
        List<Orden> ordenes = new ArrayList<>();
        try {
            rs = cn.consultar("SELECT * FROM Orden WHERE activa = true");
            while(rs.next()){
                Orden o = new Orden();
                o.idOrden = rs.getInt(1);
                o.mesero = rs.getString(2);
                o.mesa = rs.getString(3);
                o.cliente = rs.getString(4);
                o.fecha = rs.getDate(5);
                o.comentario = rs.getString(6);
                o.total = rs.getDouble(7);
                o.activa = rs.getBoolean(8);
                List<DetalleOrden> detalles;
                try (ResultSet rs2 = cn.consultar("SELECT * FROM DetalleOrden WHERE idOrden = '"+o+"'")) {
                    detalles = new ArrayList<>();
                    while(rs2.next()){
                        DetalleOrden d = new DetalleOrden();
                        try (ResultSet rs3 = cn.consultar("SELECT * FROM Producto WHERE idProducto = '"+rs2.getInt(2)+"'")) {
                            while(rs3.next()){
                                Producto p = new Producto();
                                p.idProducto = rs3.getInt(1);
                                p.nombre = rs3.getString(2);
                                p.precio = rs3.getDouble(3);
                                try (ResultSet rs4 = cn.consultar("SELECT * FROM Categoria WHERE idCategoria = '" + rs3.getInt(4) + "'")) {
                                    while (rs4.next()) {
                                        Categoria c = new Categoria();
                                        c.idCategoria = rs4.getInt(1);
                                        c.nombre = rs4.getString(2);
                                        p.categoria = c;
                                    }
                                }
                                p.area = rs.getString(5).charAt(0);
                                d.producto = p;
                                d.cantidad = rs2.getDouble(3);
                            }
                        }
                        detalles.add(d);
                    }
                }
                o.detalle = detalles;
                ordenes.add(o);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            throw new ErrorAplicacion("ManejadorOrdenes.obtenerActivas()$Error: "+ex.getMessage());
        }finally{
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    throw new ErrorAplicacion("ManejadorOrdenes.obtenerActivas()$Error: "+ex.getMessage());
                }
            }
            try {
                cn.cerrar();
            } catch (SQLException ex) {
                throw new ErrorAplicacion("ManejadorOrdenes.obtenerActivas()$Error: "+ex.getMessage());
            }
        }
        return ordenes;
    }
    
    /* Recibe un entero que indica el ID de la orden y luego devuelve el objeto orden completo que corresponde */
    public static Orden Obtener(Integer idOrden) throws ErrorAplicacion{
        Conexion cn = new Conexion();
        ResultSet rs = null;
        Orden o = new Orden();
        if(idOrden > 0){
            try {
                rs = cn.consultar("SELECT * FROM Orden WHERE idOrden = '" + idOrden + "'");
                while (rs.next()) {
                    o.idOrden = rs.getInt(1);
                    o.mesero = rs.getString(2);
                    o.mesa = rs.getString(3);
                    o.cliente = rs.getString(4);
                    o.fecha = rs.getDate(5);
                    o.comentario = rs.getString(6);
                    o.total = rs.getDouble(7);
                    o.activa = rs.getBoolean(8);
                    List<DetalleOrden> detalles;
                    try (ResultSet rs2 = cn.consultar("SELECT * FROM DetalleOrden WHERE idOrden = '" + o.idOrden + "'")) {
                        detalles = new ArrayList<>();
                        while (rs2.next()) {
                            DetalleOrden d = new DetalleOrden();
                            try (ResultSet rs3 = cn.consultar("SELECT * FROM Producto WHERE idProducto = '" + rs2.getInt(2) + "'")) {
                                while (rs3.next()) {
                                    Producto p = new Producto();
                                    p.idProducto = rs3.getInt(1);
                                    p.nombre = rs3.getString(2);
                                    p.precio = rs3.getDouble(3);
                                    try (ResultSet rs4 = cn.consultar("SELECT * FROM Categoria WHERE idCategoria = '" + rs3.getInt(4) + "'")) {
                                        while (rs4.next()) {
                                            Categoria c = new Categoria();
                                            c.idCategoria = rs4.getInt(1);
                                            c.nombre = rs4.getString(2);
                                            p.categoria = c;
                                        }
                                    }
                                    p.area = rs.getString(5).charAt(0);
                                    d.producto = p;
                                    d.cantidad = rs2.getDouble(3);
                                }
                            }
                            detalles.add(d);
                        }
                    }
                    o.detalle = detalles;
                }
            } catch (SQLException | ClassNotFoundException ex) {
                throw new ErrorAplicacion("ManejadorOrdenes.obetener()$Error: " + ex.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        throw new ErrorAplicacion("ManejadorOrdenes.obetener()$Error: " + ex.getMessage());
                    }
                }
                try {
                    cn.cerrar();
                } catch (SQLException ex) {
                    throw new ErrorAplicacion("ManejadorOrdenes.obetener()$Error: " + ex.getMessage());
                }
            }
        }else{
            throw new ErrorAplicacion("ManejadorOrdenes.obetener()$No se permiten numeros menores a 1");
        }
        return o;
    }
    
    /* Toma un objeto orden que ya existe en la tabla Orden de la base de datos, luego verifica que el objeto 
       orden tenga productos, que su total sea mayor que cero, en la base de datos hace update de la tabla orden, 
       y para la tabla Detalle Orden, lo que se hace es que se eliminan las tuplas de dicha orden y luego se insertan de nuevo. */
    public static void actualizar(Orden o) throws ErrorAplicacion{
        if(o.detalle.size() > 0){
            o.calcularTotal();
            if(o.total > 0){
                Conexion cn = new Conexion();
                List<DetalleOrden> detalles = o.detalle;
                try {
                    cn.UID("DELETE FROM DetalleOrden WHERE idOrden = '"+o.idOrden+"'");
                    cn.UID("UPDATE Orden SET mesero = '"+o.mesero+"', mesa = '"+o.mesa+"', cliente = '"+o.cliente+"', fecha = '"+o.fecha+"', comentario = '"+o.comentario+"', total = '"+o.total+"', activa = '"+o.activa+"'");
                    for (int i = 0; i < detalles.size(); i++) {
                        cn.consultar("INSERT INTO DetalleOrden(idOrden, idProducto, cantidad) VALUES('"+o.idOrden+"', '" +detalles.get(i).producto.idProducto+"', '"+detalles.get(i).cantidad+"')");
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                        throw new ErrorAplicacion("ManejadorOrdenes.actualizar()$Error: "+ex.getMessage());
                }finally{
                    try {
                        cn.cerrar();
                    } catch (SQLException ex) {
                        throw new ErrorAplicacion("ManejadorOrdenes.actualizar()$Error: "+ex.getMessage());
                    }
                }
            }else{
                throw new ErrorAplicacion("ManejadorOrdenes.actualizar()$El total no puede ser menor o igual a cero");
            }
        }else{
            throw new ErrorAplicacion("ManejadorOrdenes.actualizar()$La orden no posee productos");
        }
    }
    
    /* Toma el string que tiene el criterio de búsqueda y va a la base de datos a buscar todas aquellas ordenes 
       que cumplan con dicho criterio ya sea en el mesero, mesa, cliente o comentario. Devuelve una colección de 
       órdenes que cumplen con dicho criterio sin duplicados */
    public static List<Orden> buscarActivas(String busqueda) throws ErrorAplicacion{
        Conexion cn = new Conexion();
        ResultSet rs = null;
        String sql = "SELECT * FROM Orden WHERE activa = true AND (mesa LIKE ? OR mesero LIKE ? OR cliente LIKE ? OR comentario LIKE ?)";
        List<Orden> ordenes = new ArrayList<>();
        if(busqueda != null || !busqueda.isEmpty()){
            try {
                rs = cn.buscarOrdenesActivas(sql, busqueda);
                while (rs.next()) {
                    Orden o = new Orden();
                    o.idOrden = rs.getInt(1);
                    o.mesero = rs.getString(2);
                    o.mesa = rs.getString(3);
                    o.cliente = rs.getString(4);
                    o.fecha = rs.getDate(5);
                    o.comentario = rs.getString(6);
                    o.total = rs.getDouble(7);
                    o.activa = rs.getBoolean(8);
                    List<DetalleOrden> detalles;
                    try (ResultSet rs2 = cn.consultar("SELECT * FROM DetalleOrden WHERE idOrden = '" + o.idOrden + "'")) {
                        detalles = new ArrayList<>();
                        while (rs2.next()) {
                            DetalleOrden d = new DetalleOrden();
                            try (ResultSet rs3 = cn.consultar("SELECT * FROM Producto WHERE idProducto = '" + rs2.getInt(2) + "'")) {
                                while (rs3.next()) {
                                    Producto p = new Producto();
                                    p.idProducto = rs3.getInt(1);
                                    p.nombre = rs3.getString(2);
                                    p.precio = rs3.getDouble(3);
                                    try (ResultSet rs4 = cn.consultar("SELECT * FROM Categoria WHERE idCategoria = '" + rs3.getInt(4) + "'")) {
                                        while (rs4.next()) {
                                            Categoria c = new Categoria();
                                            c.idCategoria = rs4.getInt(1);
                                            c.nombre = rs4.getString(2);
                                            p.categoria = c;
                                        }
                                    }
                                    p.area = rs.getString(5).charAt(0);
                                    d.producto = p;
                                    d.cantidad = rs2.getDouble(3);
                                }
                            }
                            detalles.add(d);
                        }
                    }
                    o.detalle = detalles;
                    ordenes.add(o);
                }
                cn.cerrar();
            } catch (SQLException | ClassNotFoundException ex) {
                throw new ErrorAplicacion("ManejadorOrdenes.buscarActivas()$Error: " + ex.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        throw new ErrorAplicacion("ManejadorOrdenes.buscarActivas()$Error: " + ex.getMessage());
                    }
                }
                try {
                    cn.cerrar();
                } catch (SQLException ex) {
                    throw new ErrorAplicacion("ManejadorOrdenes.buscarActivas()$Error: " + ex.getMessage());
                }
            }
        }else{
            throw new ErrorAplicacion("ManejadorOrdenes.buscarActivas()$Parametro nulo o vacio");
        }
        return ordenes;
    }
    
    /* Inserta el objeto orden en la base de datos, inserta una tupla en la tabla Orden y una o varias tuplas 
       en la tabla Detalle Orden, además verifica que la orden tenga al menos uno de los campos con valor:
       mesero, mesa o cliente, no permite insertar ordenes con un total de cero o negativo, o que NO posean ningún producto en su detalle */
    public static void insertar(Orden o) throws ErrorAplicacion{
        Conexion cn = new Conexion();
        String sql = "INSERT INTO Orden(idOrden, mesero, mesa, cliente, fecha, comentario, total , activa) VALUES('"+obtenerId()+"', '"+o.mesero+"', '"+o.mesa+"', '"+o.cliente+"', '"+o.fecha+"', '"+o.comentario+"', '"+o.total+"', '"+o.activa+"')";
        if((o.cliente != null || !o.cliente.isEmpty()) && (o.mesero != null || !o.mesero.isEmpty()) && (o.mesa != null || !o.mesa.isEmpty())){
            if(o.detalle.size() > 0){
                o.calcularTotal();
                if(o.total > 0){
                    try {
                        cn.UID(sql);
                        for (DetalleOrden detalle : o.detalle) {
                            cn.UID("INSERT INTO DetalleOrden(idOrden, idProducto, cantidad) VALUES('" + o.idOrden + "', '" + detalle.producto.idProducto + "', '" + detalle.cantidad + "')");
                        }
                    } catch (ClassNotFoundException | SQLException ex) {
                        throw new ErrorAplicacion("ManejadorOrdenes.insertar()$Error: " + ex.getMessage());
                    } finally {
                        try {
                            cn.cerrar();
                        } catch (SQLException ex) {
                            throw new ErrorAplicacion("ManejadorOrdenes.insertar()$Error: " + ex.getMessage());
                        }
                    } 
                }else{
                    throw new ErrorAplicacion("ManejadorOrdenes.insertar()$El total  no puede ser menor o igual a cero");
                }
            }else{
                throw new ErrorAplicacion("ManejadorOrdenes.insertar()$NO posee ningun producto");
            }
        }else{
            throw new ErrorAplicacion("ManejadorOrdenes.insertar()$Los campos Cliente, Mesero y Mesa no pueden estar vacios");
        }
    }
    
    /* Elimina dicha orden de la base de datos, eliminando sus detalles también */
    public static void eliminar(Orden o) throws ErrorAplicacion{
        Conexion cn = new Conexion();
        if(o.idOrden > 0){
            try {
                cn.UID("DELETE FROM DetalleOrden WHERE idOrden = '" + o.idOrden + "'");
                cn.UID("DELETE FROM Orden WHERE idOrden = '" + o.idOrden + "'");
            } catch (ClassNotFoundException | SQLException ex) {
                throw new ErrorAplicacion("ManejadorOrdenes.eliminar()$Error: " + ex.getMessage());
            } finally {
                try {
                    cn.cerrar();
                } catch (SQLException ex) {
                    throw new ErrorAplicacion("ManejadorOrdenes.eliminar()$Error: " + ex.getMessage());
                }
            }
        }else{
            throw new ErrorAplicacion("ManejadorOrdenes.eliminar()$No se admite id menore o iguales a cero");
        }      
    }
    
    /* Va a la base de datos y obtiene el ultimo Id de orden y le suma 1. */
    public static Integer obtenerId() throws ErrorAplicacion{
        Conexion cn = new Conexion();
        ResultSet rs = null;
        String sql = "SELECT MAX(idOrden) FROM Orden";
        int id = 0;
        try {
            rs = cn.consultar(sql);
            while(rs.next()){
                id = rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            throw new ErrorAplicacion("ManejadorOrdenes.obtenerId()$Error: "+ex.getMessage());
        }finally{
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                   throw new ErrorAplicacion("ManejadorOrdenes.obtenerId()$Error: "+ex.getMessage());
                }
            }
            try {
                cn.cerrar();
            } catch (SQLException ex) {
               throw new ErrorAplicacion("ManejadorOrdenes.obtenerId()$Error: "+ex.getMessage());
            }
        }

        return id + 1;
    }
    
    /* Obtiene todas las ventas realizadas para una fecha determinada, se filtra solo por día mes y año, las 
       ordenes devueltas tienen que tener el campo activa en FALSE, pues son ordenes que ya fueron cobradas. */
    public static List<Orden> obtenerVentas(Date fecha) throws ErrorAplicacion{
        Conexion cn = new Conexion();
        ResultSet rs = null;
        List<Orden> ordenes = new ArrayList<>();
        if(fecha != null){
            try {
                rs = cn.consultar("SELECT * FROM Orden WHERE activa = false AND fecha = '" + fecha + "'");
                while (rs.next()) {
                    Orden o = new Orden();
                    o.idOrden = rs.getInt(1);
                    o.mesero = rs.getString(2);
                    o.mesa = rs.getString(3);
                    o.cliente = rs.getString(4);
                    o.fecha = rs.getDate(5);
                    o.comentario = rs.getString(6);
                    o.total = rs.getDouble(7);
                    o.activa = rs.getBoolean(8);
                    ordenes.add(o);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                throw new ErrorAplicacion("ManejadorOrdenes.obtenerVentas()$Error: " + ex.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        throw new ErrorAplicacion("ManejadorOrdenes.obtenerVentas()$Error: " + ex.getMessage());
                    }
                }
                try {
                    cn.cerrar();
                } catch (SQLException ex) {
                    throw new ErrorAplicacion("ManejadorOrdenes.obtenerVentas()$Error: " + ex.getMessage());
                }
            }
        }else {
            throw new ErrorAplicacion("ManejadorOrdenes.obtenerVentas()Fecha invalida");
        }
        return ordenes;   
    }   
        
    /* Igual que el anterior pero filtrando por un rango de fechas. Importante en este método es que los objetos
       Orden NO tienen cargado el detalle de sus productos. */
    public static List<Orden> obtenerVentas(Date fechaDesde, Date fechaHasta) throws ErrorAplicacion{
        Conexion cn = new Conexion();
        ResultSet rs = null;
        List<Orden> ordenes = new ArrayList<>();
        if(fechaDesde != null && fechaHasta != null) {
            try {
                rs = cn.consultar("SELECT * FROM Orden WHERE activa = false AND fecha = '" + fechaDesde + "' BETWEEN '" + fechaHasta + "'");
                while (rs.next()) {
                    Orden o = new Orden();
                    o.idOrden = rs.getInt(1);
                    o.mesero = rs.getString(2);
                    o.mesa = rs.getString(3);
                    o.cliente = rs.getString(4);
                    o.fecha = rs.getDate(5);
                    o.comentario = rs.getString(6);
                    o.total = rs.getDouble(7);
                    o.activa = rs.getBoolean(8);
                    ordenes.add(o);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                throw new ErrorAplicacion("ManejadorOrdenes.obtenerVentas()$Error: " + ex.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        throw new ErrorAplicacion("ManejadorOrdenes.obtenerVentas()$Error: " + ex.getMessage());
                    }
                }
                try {
                    cn.cerrar();
                } catch (SQLException ex) {
                    throw new ErrorAplicacion("ManejadorOrdenes.obtenerVentas()$Error: " + ex.getMessage());
                }
            }
        }else {
            throw new ErrorAplicacion("ManejadorOrdenes.obtenerVentas()Fecha invalida");
        }
        return ordenes;
    }
}
