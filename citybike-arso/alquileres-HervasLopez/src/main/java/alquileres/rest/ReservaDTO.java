package alquileres.rest;

import java.time.LocalDateTime;

import com.google.gson.annotations.JsonAdapter;

import utils.LocalDateTimeJsonAdapter;

//Clase para el DTO de la clase Reserva
public class ReservaDTO {

	private String idBicicleta;
	@JsonAdapter(LocalDateTimeJsonAdapter.class)
	private LocalDateTime creada;
	@JsonAdapter(LocalDateTimeJsonAdapter.class)
	private LocalDateTime caducidad;
	
	private String idUsuario;

	public ReservaDTO(String idBicicleta, LocalDateTime creada, LocalDateTime caducidad, String idUsuario) {
		this.idBicicleta = idBicicleta;
		this.creada = creada;
		this.caducidad = caducidad;
		this.idUsuario = idUsuario;
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

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	

}
