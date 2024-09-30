package servicio;


/*
 * Excepción que representa un fallo en el sistema del servicio.
 * Al instanciarla, se establece la excepción interna que produce el error (causa).
 */

@SuppressWarnings("serial")
public class ServicioException extends Exception {

	public ServicioException(String msg, Throwable causa) {		
		super(msg, causa);
	}
	
	public ServicioException(String msg) {
		super(msg);		
	}
	
		
}
