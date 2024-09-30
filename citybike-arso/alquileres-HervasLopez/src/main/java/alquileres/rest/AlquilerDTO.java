package alquileres.rest;

import java.time.LocalDateTime;

import com.google.gson.annotations.JsonAdapter;

import utils.LocalDateTimeJsonAdapter;

// Clase para el DTO de la clase Alquiler
public class AlquilerDTO {

	private String idBicicleta;
	@JsonAdapter(LocalDateTimeJsonAdapter.class)
	private LocalDateTime inicio;
	@JsonAdapter(LocalDateTimeJsonAdapter.class)
	private LocalDateTime fin;
	
	private String idUsuario;

	public AlquilerDTO(String idBicicleta, LocalDateTime inicio, LocalDateTime fin, String idUsuario) {
		this.idBicicleta = idBicicleta;
		this.inicio = inicio;
		if (fin != null)
			this.fin = fin;
		this.idUsuario = idUsuario;
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
	
	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

}
