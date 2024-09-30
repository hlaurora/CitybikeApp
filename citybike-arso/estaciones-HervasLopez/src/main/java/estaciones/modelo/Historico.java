package estaciones.modelo;

import java.time.LocalDateTime;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

import repositorio.Identificable;

// Para llevar un seguimiento de todas las Estaciones por las que
// ha pasado una Bicicleta 
public class Historico implements Identificable {

	@BsonId
	@BsonRepresentation(BsonType.OBJECT_ID)
	private String id;
	@BsonProperty(value = "fecha_inicio")
	private LocalDateTime fechaInicio;
	@BsonProperty(value = "fecha_fin")
	private LocalDateTime fechaFin;
	private String bicicleta;
	private String estacion;

	public Historico() {

	}

	public Historico(String bicicleta, String estacion) {
		this.fechaInicio = LocalDateTime.now();
		this.bicicleta = bicicleta;
		this.estacion = estacion;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getBicicleta() {
		return bicicleta;
	}

	public void setBicicleta(String bicicleta) {
		this.bicicleta = bicicleta;
	}

	public String getEstacion() {
		return estacion;
	}

	public void setEstacion(String estacion) {
		this.estacion = estacion;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Historico [id=" + id + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", bicicleta="
				+ bicicleta + ", estacion=" + estacion + "]";
	}

}
