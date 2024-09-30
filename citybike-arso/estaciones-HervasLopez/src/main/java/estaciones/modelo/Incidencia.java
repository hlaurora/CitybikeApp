package estaciones.modelo;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.bson.codecs.pojo.annotations.BsonIgnore;

@Entity
@Table(name = "incidencia")
public class Incidencia {

	@Id
	private String id;
	@Column(name = "fecha_creacion", columnDefinition = "DATE")
	private LocalDate fechaCreacion;
	@Column(name = "descripcion")
	@Lob
	private String descripcion;
	@Column(name = "fecha_cierre", columnDefinition = "DATE")
	private LocalDate fechaCierre;
	@Column(name = "motivo_cierre")
	@Lob
	private String motivoCierre;
	@Column(name = "operario")
	private String operario;
	@Enumerated(EnumType.STRING)
	private EstadoIncidencia estado;
	@BsonIgnore
	@ManyToOne
	@JoinColumn(name = "bicicleta_fk")
	private Bicicleta bicicleta;

	public Incidencia() {

	}

	public Incidencia(String descripcion, Bicicleta bicicleta) {
		this.descripcion = descripcion;
		this.fechaCreacion = LocalDate.now();
		this.estado = EstadoIncidencia.PENDIENTE;
		// Generamos el id de la incidencia
		this.id = UUID.randomUUID().toString();
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

	public Bicicleta getBicicleta() {
		return bicicleta;
	}

	public void setBicicleta(Bicicleta bicicleta) {
		this.bicicleta = bicicleta;
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

	@Override
	public String toString() {
		return "Incidencia [id=" + id + ", fechaCreacion=" + fechaCreacion + ", descripcion=" + descripcion
				+ ", fechaCierre=" + fechaCierre + ", motivoCierre=" + motivoCierre + ", operario=" + operario
				+ ", estado=" + estado + "]";
	}

}
