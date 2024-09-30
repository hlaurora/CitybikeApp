package estaciones.eventos;

import java.util.Date;

// Evento para notificar si se ha alquilado una bici o se ha concluido un alquiler
public class EventoAlquileres {

	private String idBicicleta;
	private Date hora;

	public EventoAlquileres() {
	}

	public EventoAlquileres(String idBicicleta, Date hora) {
		super();
		this.idBicicleta = idBicicleta;
		this.hora = hora;
	}

	public String getIdBicicleta() {
		return idBicicleta;
	}

	public void setIdBicicleta(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

}
