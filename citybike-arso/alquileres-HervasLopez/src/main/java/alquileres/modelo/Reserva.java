package alquileres.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Reserva implements Serializable {

	private String idBicicleta;
	private LocalDateTime creada;
	private LocalDateTime caducidad;

	public Reserva(String idBicicleta, LocalDateTime fecha) {
		this.idBicicleta = idBicicleta;
		this.creada = fecha;
		this.caducidad = creada.plusMinutes(30);
	}

	// Si la fecha actual es después de la fecha de caducidad, la reserva está caducada
	public boolean isCaducada(LocalDateTime fecha) {
		return fecha.isAfter(caducidad);
	}
	
	// La reserva está activa si no está caducada
	public boolean isActiva(LocalDateTime fecha) {
		return !isCaducada(fecha);
	}

	public String getIdBicicleta() {
		return idBicicleta;
	}

	public void setIdBicicleta(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}

	public LocalDateTime getCreada() {
		return creada;
	}

	public void setCreada(LocalDateTime creada) {
		this.creada = creada;
	}

	public LocalDateTime getCaducidad() {
		return caducidad;
	}

	public void setCaducidad(LocalDateTime caducidad) {
		this.caducidad = caducidad;
	}

	@Override
	public String toString() {
		return "Reserva [idBicicleta=" + idBicicleta + ", creada=" + creada + ", caducidad=" + caducidad + "]";
	}

}
