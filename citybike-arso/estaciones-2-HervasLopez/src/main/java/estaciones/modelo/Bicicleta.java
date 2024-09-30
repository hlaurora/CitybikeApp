package estaciones.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import repositorio.Identificable;

@Document(collection = "bicicletas")
public class Bicicleta implements Identificable {

	@Id
	private String id;
	private String modelo;
	private LocalDate fechaAlta;
	private LocalDate fechaBaja;
	private String motivoBaja;
	private boolean disponible;
	private String idEstacionActual;
	private ArrayList<Incidencia> incidencias = new ArrayList<Incidencia>();

	public Bicicleta() {

	}

	public Bicicleta(String modelo) {
		this.modelo = modelo;
		this.fechaAlta = LocalDate.now();
		this.disponible = true;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;

	}

	public LocalDate getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(LocalDate fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public String getMotivoBaja() {
		return motivoBaja;
	}

	public void setMotivoBaja(String motivoBaja) {
		this.motivoBaja = motivoBaja;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public String getIdEstacionActual() {
		return idEstacionActual;
	}

	public void setIdEstacionActual(String idEstacionActual) {
		this.idEstacionActual = idEstacionActual;
	}

	// Añade una nueva Incidencia a la Bicicleta
	public String addIncidencia(String descripcion) {
		Incidencia incidencia = new Incidencia(descripcion, this);
		incidencias.add(incidencia);
		// Cambia la Bicicleta a no disponible
		this.disponible = false;
		return incidencia.getId();
	}

	// Obtiene la lista de incidencias abiertas recorriendo la lista de incidencias
	// de la bicicleta y añadiendo las abiertas, comprobando si lo están mediante el
	// método isAbierta de Incidencia
	public List<Incidencia> getIncidenciasAbiertas() {
		List<Incidencia> abiertas = new ArrayList<>();
		for (Incidencia incidencia : this.incidencias) {
			if (incidencia.isAbierta()) {
				abiertas.add(incidencia);
			}
		}
		return abiertas;
	}

	// Obtiene una incidencia de la lista de incidencias de la bicicleta mediante su
	// id
	public Incidencia getIncidencia(String idIncidencia) {
		for (Incidencia incidencia : this.incidencias) {
			if (incidencia.getId().equals(idIncidencia))
				return incidencia;
		}
		return null;
	}

	// Método para cambiar el estado de una incidencia, recibiendo el id de la
	// incidencia y el estado que al que se va a cambiar
	public Incidencia cambiarEstadoIncidencia(String idIncidencia, EstadoIncidencia estado, String datos) {
		// Obtiene la incidencia
		Incidencia incidencia = getIncidencia(idIncidencia);
		if (incidencia == null)
			return null;
		// Según el estado objetivo de la incidencia, realiza unos cambios u otros
		switch (estado) {
		case CANCELADA:
			// Si se quiere cancelar la incidencia, primero se comprueba que el estado de
			// esta sea PENDIENTE
			if (incidencia.getEstado() == EstadoIncidencia.PENDIENTE) {
				// Se cambia el estado a CANCELADA y se establece la fecha de cierre y el motivo
				// del cierre de la incidencia
				incidencia.setEstado(estado);
				incidencia.setFechaCierre(LocalDate.now());
				incidencia.setMotivoCierre(datos);
				// La bicicleta vuelve a estar disponible
				this.disponible = true;
			} else
				return null;
			break;
		case ASIGNADA:
			// Si se quiere asignar la incidencia, primero se comprueba que el estado de
			// esta sea PENDIENTE
			if (incidencia.getEstado() == EstadoIncidencia.PENDIENTE) {
				// Se cambia el estado a ASIGNADA y se le asigna el operario
				incidencia.setEstado(estado);
				incidencia.setOperario(datos);
			} else
				return null;
			break;
		case RESUELTA:
			// Si se quiere resolver la incidencia, primero se comprueba que el estado de
			// esta sea ASIGNADA
			if (incidencia.getEstado() == EstadoIncidencia.ASIGNADA) {
				// Se cambia el estado a RESUELTA y se establece la fecha de cierre y el motivo
				// de cierre de la incidencia
				incidencia.setEstado(estado);
				incidencia.setFechaCierre(LocalDate.now());
				incidencia.setMotivoCierre(datos);
				// La incidencia vuelve a estar disponible
				this.disponible = true;
			} else
				return null;
			break;
		default:
			break;
		}
		return incidencia;
	}

	// Método para dar de baja a la bicicleta
	public void darBaja(String motivo) {
		// Se establecen el motivo y la fecha de baja
		this.motivoBaja = motivo;
		this.fechaBaja = LocalDate.now();
		// La bicicleta deja de estar disponible
		this.disponible = false;
	}

	@Override
	public String toString() {
		return "Bicicleta [id=" + id + ", modelo=" + modelo + ", fechaAlta=" + fechaAlta + ", fechaBaja=" + fechaBaja
				+ ", motivoBaja=" + motivoBaja + ", disponible=" + disponible + ", idEstacionActual=" + idEstacionActual
				+ ", incidencias=" + incidencias + "]";
	}

}
