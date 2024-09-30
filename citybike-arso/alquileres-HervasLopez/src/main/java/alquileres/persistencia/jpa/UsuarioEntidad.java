package alquileres.persistencia.jpa;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import repositorio.Identificable;

//Clase para la entidad Usuario en la persistencia JPA
@Entity
@Table(name = "usuario")
public class UsuarioEntidad implements Identificable, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReservaEntidad> reservas = new ArrayList<ReservaEntidad>();

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AlquilerEntidad> alquileres = new ArrayList<AlquilerEntidad>();

	public UsuarioEntidad() {
	}

	public UsuarioEntidad(String id, List<ReservaEntidad> reservas, List<AlquilerEntidad> alquileres) {
		super();
		this.id = id;
		this.reservas = reservas;
		this.alquileres = alquileres;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public List<ReservaEntidad> getReservas() {
		return reservas;
	}

	public void setReservas(List<ReservaEntidad> reservas) {
		this.reservas = reservas;
	}

	public List<AlquilerEntidad> getAlquileres() {
		return alquileres;
	}

	public void setAlquileres(List<AlquilerEntidad> alquileres) {
		this.alquileres = alquileres;
	}

	public String addReserva(String idBicicleta, LocalDateTime inicio, LocalDateTime fin) {
		if (this.reservas == null)
			reservas = new ArrayList<ReservaEntidad>();
		ReservaEntidad reserva = new ReservaEntidad(idBicicleta, inicio, fin, this);
		this.reservas.add(reserva);
		return reserva.getId();
	}

	public String addAlquiler(String idBicicleta, LocalDateTime inicio, LocalDateTime fin) {
		if (this.alquileres == null)
			alquileres = new ArrayList<AlquilerEntidad>();
		AlquilerEntidad alquiler = new AlquilerEntidad(idBicicleta, inicio, fin, this);
		this.alquileres.add(alquiler);
		return alquiler.getId();
	}

}
