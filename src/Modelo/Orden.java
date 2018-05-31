package Modelo;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mateo
 */
public class Orden {

    /* Para que un objeto orden sea válido, debe poseer valor en al menos uno
       de sus campos: mesero, mesa o cliente */
    public Integer idOrden;
    public String mesero;
    public String mesa;
    public String cliente;
    public Date fecha;
    public String comentario;
    public double total;
    public boolean activa;
    public List<DetalleOrden> detalle;

    /* Almacena el total de consumo de la orden, para ello recorre toda su colección DETALLE 
       multiplicando el precio unitario por la cantidad y luego sumándolo para al final actualizar 
       la propiedad Total de la orden con el valor correcto */
    public void calcularTotal()throws ErrorAplicacion, Exception {
        for (DetalleOrden detalle1 : detalle) {
            total = total + (detalle1.producto.precio * detalle1.cantidad);
        }
    }

    /* Permite agregar más productos a la orden, toma el objeto producto y la cantidad para construir 
       un objeto DetalleOrden, y luego ver si ese producto ya está agregado a la orden, si ya está 
       agregado a la orden, entonces solo se suma la cantidad, sino se agrega a la colección DETALLE
       de la orden y se invoca calcular total */
    public void agregarProducto(Producto p, double cantidad)throws ErrorAplicacion, Exception {
        DetalleOrden nuevoDetalle = new DetalleOrden();
        nuevoDetalle.producto = p;
        nuevoDetalle.cantidad = cantidad;
        boolean existe = false;
        int posicion = 0;

        //Comprobando el producto ya esta agregado en la orden
        for (DetalleOrden detalle1 : detalle) {
            if (detalle1.producto.equals(p)) {
                existe = true;
                break;
            }
            posicion++;
        }

        if (existe) {
            if (cantidad > 0) {
                detalle.get(posicion).cantidad = detalle.get(posicion).cantidad + cantidad;
            } else {
                try {
                    throw new ErrorAplicacion("orden.agregarProducto$La cantidad debe ser mayor a cero");
                } catch (ErrorAplicacion ex) {
                    Logger.getLogger(Orden.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {
            if (p.precio > 0) {
                if (cantidad > 0) {
                    DetalleOrden d = new DetalleOrden();
                    d.cantidad = cantidad;
                    d.producto = p;
                    detalle.add(d);
                } else {
                    try {
                        throw new ErrorAplicacion("orden.agregarProducto$La cantidad debe ser mayor a cero");
                    } catch (ErrorAplicacion ex) {
                        Logger.getLogger(Orden.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                try {
                    throw new ErrorAplicacion("orden.agregarProducto$El precio debe ser mayor a cero");
                } catch (ErrorAplicacion ex) {
                    Logger.getLogger(Orden.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        calcularTotal();
    }

    /* Permite eliminar productos de una orden y actualiza el total de la orden. */
    public void EliminarProducto(Producto p, double cantidad)throws ErrorAplicacion, Exception {
        DetalleOrden detalleOrden = new DetalleOrden();
        detalleOrden.producto = p;
        detalleOrden.cantidad = cantidad;

        if (detalle.size() > 0) {
            for (int i = 0; i < detalle.size(); i++) {
                if (Objects.equals(p.idProducto, detalle.get(i).producto.idProducto)) {
                    detalle.remove(i);
                }else{
                    if (Objects.equals(detalle.size(), i+1)) {
                        try {
                            throw new ErrorAplicacion("orden.eliminarProducto$El producto no existe en la orden");
                        } catch (ErrorAplicacion ex) {
                            Logger.getLogger(Orden.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }else {
            try {
                throw new ErrorAplicacion("orden.eliminarProducto$No existen detalles en la orden");
            } catch (ErrorAplicacion ex) {
                Logger.getLogger(Orden.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
