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

// Clase para la entidad Alquiler en la persistencia JPA
@Entity
@Table(name = "alquiler")
public class AlquilerEntidad implements Identificable, Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	@Column(name = "id_bicicleta")
	private String idBicicleta;
	@Column(name = "fecha_inicio", columnDefinition = "TIMESTAMP")
	private LocalDateTime inicio;
	@Column(name = "fecha_fin", columnDefinition = "TIMESTAMP")
	private LocalDateTime fin;
	@ManyToOne
	@JoinColumn(name = "usuario_fk")
	private UsuarioEntidad usuario;

	public AlquilerEntidad() {
	}

	public AlquilerEntidad(String idBicicleta, LocalDateTime inicio, LocalDateTime fin,
			UsuarioEntidad usuario) {
		this.id = UUID.randomUUID().toString();
		this.idBicicleta = idBicicleta;
		this.inicio = inicio;
		this.fin = fin;
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

	public UsuarioEntidad getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioEntidad usuario) {
		this.usuario = usuario;
	}

}
