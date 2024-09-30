package servicio;

// Excepción ServicioAlquileresException
@SuppressWarnings("serial")
public class ServicioAlquileresException extends Exception {

	public ServicioAlquileresException(String msg, Throwable causa) {
		super(msg, causa);
	}

	public ServicioAlquileresException(String msg) {
		super(msg);
	}

}