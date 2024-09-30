package alquileres.rabbitmq;

// Clase para el evento "bicicleta-desactivada" del microservicio estaciones
public class EventoBicicletaDesactivada {

	// Este evento incluye el id de la bicicleta
	private String idBicicleta;

	public EventoBicicletaDesactivada(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}

	public EventoBicicletaDesactivada() {
	}

	public String getIdBicicleta() {
		return idBicicleta;
	}

	public void setIdBicicleta(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}

	@Override
	public String toString() {
		return "EventoBicicletaDesactivada [idBicicleta=" + idBicicleta + "]";
	}

}
