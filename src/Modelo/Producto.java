
package Modelo;

/**
 *
 * @author mateo
 */

public class Producto {
    public Integer idProducto;
    public String nombre;
    public double precio;
    //esta propiedad tendrá un objeto categoría, pero dicho objeto categoría No tendrá cargados los productos.
    public Categoria categoria;
    //area tendrá un valor C si el producto debe prepararse en cocina y B si el producto debe prepararse en el área de bebidas.
    public char area;
}
