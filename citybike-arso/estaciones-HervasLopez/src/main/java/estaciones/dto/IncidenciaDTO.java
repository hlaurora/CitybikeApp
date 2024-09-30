package estaciones.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.bson.codecs.pojo.annotations.BsonIgnore;

import estaciones.modelo.Bicicleta;
import estaciones.modelo.EstadoIncidencia;

public class IncidenciaDTO implements Serializable {
	
	private String id;
	private LocalDate fechaCreacion;
	private String descripcion;
	private LocalDate fechaCierre;
	private String motivoCierre;
	private String operario;
	private EstadoIncidencia estado;
	private String bicicleta;
	
	public IncidenciaDTO(String id, String descripcion, LocalDate fechaCreacion, LocalDate fechaCierre,
							String motivoCierre, String operario, EstadoIncidencia estado, String bicicleta) {
		super();
		this.id = id;
		this.descripcion = descripcion;
		this.fechaCreacion = fechaCreacion;
		this.fechaCierre = fechaCierre;
		this.motivoCierre = motivoCierre;
		this.operario = operario;
		this.estado = estado;
		this.bicicleta = bicicleta;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EstadoIncidencia getEstado() {
		return estado;
	}

	public void setEstado(EstadoIncidencia estado) {
		this.estado = estado;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(LocalDate fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public String getMotivoCierre() {
		return motivoCierre;
	}

	public void setMotivoCierre(String motivoCierre) {
		this.motivoCierre = motivoCierre;
	}

	public String getOperario() {
		return operario;
	}

	public void setOperario(String operario) {
		this.operario = operario;
	}

	// Comprueba si una incidencia está abierta
	public boolean isAbierta() {
		// La incidencia estará abierta si su estado no es CANCELADA o RESUELTA
		if (this.estado == EstadoIncidencia.CANCELADA || this.estado == EstadoIncidencia.RESUELTA)
			return false;
		return true;
	}
	
	public String getBicicleta() {
		return bicicleta;
	}

	public void setBicicleta(String bicicleta) {
		this.bicicleta = bicicleta;
	}

}
