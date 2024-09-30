package alquileres.persistencia.jpa;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import repositorio.Identificable;

//Clase para la entidad Reserva en la persistencia JPA
@Entity
@Table(name = "reserva")
public class ReservaEntidad implements Identificable, Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	@Column(name = "id_bicicleta")
	private String idBicicleta;
	@Column(name = "fecha_creada", columnDefinition = "TIMESTAMP")
	private LocalDateTime creada;
	@Column(name = "fecha_caducidad", columnDefinition = "TIMESTAMP")
	private LocalDateTime caducidad;
	@ManyToOne
	@JoinColumn(name = "usuario_fk")
	private UsuarioEntidad usuario;

	public ReservaEntidad() {
	}

	public ReservaEntidad(String idBicicleta, LocalDateTime creada, LocalDateTime caducidad, UsuarioEntidad usuario) {
		super();
		this.id = UUID.randomUUID().toString();
		this.idBicicleta = idBicicleta;
		this.creada = creada;
		this.caducidad = caducidad;
		this.usuario = usuario;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
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

	public UsuarioEntidad getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioEntidad usuario) {
		this.usuario = usuario;
	}

}
