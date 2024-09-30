package alquileres.rabbitmq;

import java.util.Date;

// Clase para los eventos que se producen en el microservicio alquileres
public class EventoAlquileres {

	// Estos eventos incluyen el id de la bicicleta y un date
	private String idBicicleta;
	private Date hora;

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
