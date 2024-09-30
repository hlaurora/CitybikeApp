package estaciones.eventos;

// Evento para notificar cuando se da de baja una Bicicleta
public class EventoBicicletaDesactivada {

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
