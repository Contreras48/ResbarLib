
package Modelo;

/**
 *
 * @author mateo
 */

/* Es una clase para el lanzamiento de excepciones personalizadas hacia la capa superior de interfaz. 
   Hereda de la clase Exception. Solo posee un constructor el cual recibe el mensaje de error, el 
   mensaje de error usara el separador Dólar, para indicar primero el objeto y método que desencadena 
   la excepción, y luego el propio mensaje, por ejemplo:“ManejadorOrdenes.ObtenerId()$No es posible
   conectarse a la base de datos” */

public class ErrorAplicacion extends Exception{
    
    /* En el mensaje se usa el carácter $ como separador, colocando primero el objeto y el método que
       produce la excepción y luego el mensaje de la misma. */
    public ErrorAplicacion(String msg) throws Exception{
        try {
            throw new Exception(msg);
        } catch (Exception e) {
                        throw new Exception(msg+e.getMessage());

        }
        
       
        
    }

}
