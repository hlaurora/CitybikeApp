package alquileres.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Alquiler implements Serializable {

	private String idBicicleta;
	private LocalDateTime inicio;
	private LocalDateTime fin;

	public Alquiler(String idBicicleta, LocalDateTime fecha) {
		this.idBicicleta = idBicicleta;
		this.inicio = fecha;
	}

	public boolean isActivo() {
		return fin == null;
	}

	public int getTiempo(LocalDateTime momentoActual) {
		LocalDateTime fechaFin = fin != null ? fin : momentoActual;
		return (int) ChronoUnit.MINUTES.between(inicio, fechaFin);
	}

	public String getIdBicicleta() {
		return idBicicleta;
	}

	public void setIdBicicleta(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}

	public LocalDateTime getInicio() {
		return inicio;
	}

	public void setInicio(LocalDateTime inicio) {
		this.inicio = inicio;
	}

	public LocalDateTime getFin() {
		return fin;
	}

	public void setFin(LocalDateTime fin) {
		this.fin = fin;
	}

	@Override
	public String toString() {
		return "Alquiler [idBicicleta=" + idBicicleta + ", inicio=" + inicio + ", fin=" + fin + "]\n";
	}

}
