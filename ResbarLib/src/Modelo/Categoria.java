
package Modelo;

import java.util.List;

/**
 *
 * @author mateo
 */

/**
 * La clase “Categoria” representa un objeto categoría con todas sus propiedades
 * establecidas, cada categoría contiene una colección de productos.
 */
public class Categoria {
    public Integer idCategoria;
    public String nombre;
    public List<Producto> productos; // colección de objetos de la clase producto

}
