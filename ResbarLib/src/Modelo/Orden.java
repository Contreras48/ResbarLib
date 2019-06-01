package Modelo;

import java.util.ArrayList;
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
    public List<DetalleOrden> detalle = new ArrayList<>();

    /* Almacena el total de consumo de la orden, para ello recorre toda su colección DETALLE 
       multiplicando el precio unitario por la cantidad y luego sumándolo para al final actualizar 
       la propiedad Total de la orden con el valor correcto */
    public void calcularTotal() {
        total = 0;
        for (DetalleOrden detalle1 : detalle) {
            total = total + (detalle1.producto.precio * detalle1.cantidad);
        }
    }

    /* Permite agregar más productos a la orden, toma el objeto producto y la cantidad para construir 
       un objeto DetalleOrden, y luego ver si ese producto ya está agregado a la orden, si ya está 
       agregado a la orden, entonces solo se suma la cantidad, sino se agrega a la colección DETALLE
       de la orden y se invoca calcular total */
    public void agregarProducto(Producto p, double cant) throws ErrorAplicacion {
        DetalleOrden nuevoDetalle = new DetalleOrden();
        List<DetalleOrden> listaDetalle = new ArrayList<>();
        if(p.idProducto > 0 && p.precio > 0){
            if(cant > 0){
                if (!detalle.isEmpty()) {
                    int elementos = detalle.size();
                    for (int i = 0; i < elementos; i++) {
                        if (Objects.equals(detalle.get(i).producto.idProducto, p.idProducto)) {
                            detalle.get(i).cantidad += cant;
                            break;
                        } else {
                            if (i == (detalle.size() - 1)) {
                                nuevoDetalle.producto = p;
                                nuevoDetalle.cantidad = cant;
                                detalle.add(nuevoDetalle);
                            }
                        }
                    }
                } else {
                    nuevoDetalle.producto = p;
                    nuevoDetalle.cantidad = cant;
                    detalle.add(nuevoDetalle);
                }
            }else{
                throw new ErrorAplicacion("Orde.agregarProducto()$La cantidad no puede ser menor que 1");
            }
        }else{
            throw new ErrorAplicacion("Orde.agregarProducto()$El precio o el id son menores que 1");
        }
        calcularTotal();
    }

    /* Permite eliminar productos de una orden y actualiza el total de la orden. */
    public void EliminarProducto(Producto p, double cant) throws ErrorAplicacion {
        if (!detalle.isEmpty()) {
            int elementos = detalle.size();
            for (int i = 0; i < elementos; i++) {
                if (Objects.equals(p.idProducto, detalle.get(i).producto.idProducto)) {
                    if(cant > 0){
                        if(cant < detalle.get(i).cantidad){
                            detalle.get(i).cantidad -= cant;
                        }else if(cant == detalle.get(i).cantidad){
                            detalle.remove(i);
                        }else{
                            throw new ErrorAplicacion("orden.eliminarProducto$La cantidad introducida excede a la cantidad del prodcuto");
                        }
                        
                    }else{
                        throw new ErrorAplicacion("orden.eliminarProducto$La cantidad no puede ser menor a 1");
                    }
                }else{
                    if (detalle.size()-1 == i) {
                        throw new ErrorAplicacion("orden.eliminarProducto$El producto no existe en la orden");
                    }
                }
            }
        }else {
            throw new ErrorAplicacion("orden.eliminarProducto$No existen detalles en la orden");
        }
        
        calcularTotal();
    }

}
