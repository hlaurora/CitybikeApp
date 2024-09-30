package alquileres.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import repositorio.Identificable;

public class Usuario implements Identificable, Serializable {

	private String id;
	private List<Reserva> reservas;
	private List<Alquiler> alquileres;

	public Usuario(String id) {
		this.id = id;
		this.reservas = new ArrayList<Reserva>();
		this.alquileres = new ArrayList<Alquiler>();
	}

	public void addReserva(Reserva reserva) {
		if (reservas == null)
			reservas = new ArrayList<>();
		this.reservas.add(reserva);
	}

	public void addAlquiler(Alquiler alquiler) {
		if (alquileres == null) {
			alquileres = new ArrayList<>();
		}
		this.alquileres.add(alquiler);
	}

	public int getReservasCaducadas(LocalDateTime tiempoActual) {
		return (int) reservas.stream().filter(reserva -> reserva.isCaducada(tiempoActual)).count();
	}

	public int tiempoUsoHoy(LocalDateTime tiempo) {
		LocalDateTime hoy = tiempo.withHour(0).withMinute(0).withSecond(0).withNano(0);
		return this.alquileres.stream().filter(alquiler -> alquiler.getInicio().isAfter(hoy))
				.mapToInt(alquiler -> alquiler.getTiempo(tiempo)).sum();
	}

	public int tiempoUsoSemana(LocalDateTime tiempo) {
		LocalDateTime semanaPasada = tiempo.withHour(0).withMinute(0).withSecond(0).withNano(0).minusDays(7);
		return alquileres.stream().filter(alquiler -> alquiler.getInicio().isAfter(semanaPasada))
				.mapToInt(alquiler -> alquiler.getTiempo(tiempo)).sum();
	}

	// El usuario supera el tiempo de uso si el tiempo de uso de hoy es mayor de 60
	// minutos o si el tiempo de uso de la semana es mayor de 160 minutos
	public boolean superaTiempo(LocalDateTime tiempo) {
		if (tiempoUsoHoy(tiempo) >= 60 || tiempoUsoSemana(tiempo) >= 160)
			return true;
		return false;
	}

	public Reserva reservaActiva(LocalDateTime tiempo) {
		if (reservas != null && !reservas.isEmpty()) {
			for (int i = 0; i < reservas.size(); i++) {
				// Reserva ultima = reservas.get(reservas.size() - 1);

				if (reservas.get(i).isActiva(tiempo))
					return reservas.get(i);
			}
		}
		return null;
	}

	public Alquiler alquilerActivo() {
		if (!alquileres.isEmpty()) {
			for (int i = 0; i < alquileres.size(); i++) {
				if (alquileres.get(i).isActivo())
					return alquileres.get(i);
			}
		}
		/*if (!alquileres.isEmpty()) {
			Alquiler ultimo = alquileres.get(alquileres.size() - 1);
			if (ultimo.isActivo())
				return ultimo;
		}*/
		return null;
	}

	public void dejarBicicleta(LocalDateTime tiempo) {
		alquilerActivo().setFin(tiempo);

	}
	
	public String cancelarReserva(LocalDateTime tiempo) {
		
		Reserva activa = this.reservaActiva(tiempo);

		String id = activa.getIdBicicleta();
		this.reservas.remove(activa);
		return id;
	}

	// El usuario está bloqueado si el número de reservas caducadas es igual o
	// superior a 3
	public boolean bloqueado(LocalDateTime tiempo) {
		return getReservasCaducadas(tiempo) >= 3;
	}

	// Al confirmar la reserva activa, esta se elimina y se crea un nuevo alquiler
	// sobre la bicicleta de la reserva
	public void confirmarReserva(LocalDateTime tiempo) {
		Reserva activa = this.reservaActiva(tiempo);
		this.alquilar(activa.getIdBicicleta(), tiempo);
		this.reservas.remove(activa);
	}

	public void reservar(String idBici, LocalDateTime tiempo) {
		if (reservas == null)
			reservas = new ArrayList<Reserva>();
		Reserva reserva = new Reserva(idBici, tiempo);
		this.addReserva(reserva);
	}

	public void alquilar(String idBici, LocalDateTime tiempo) {
		Alquiler alquiler = new Alquiler(idBici, tiempo);
		this.addAlquiler(alquiler);
	}

	// Al liberar el bloqueo de un usuario se eliminan las reservas que tiene que
	// están caducadas
	public void liberarBloqueo(LocalDateTime tiempo) {
		this.reservas.removeIf(reserva -> reserva.isCaducada(tiempo));
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	public List<Alquiler> getAlquileres() {
		return alquileres;
	}

	public void setAlquileres(List<Alquiler> alquileres) {
		this.alquileres = alquileres;
	}

	public void eliminarReservaActiva(LocalDateTime tiempo) {
		if (this.reservaActiva(tiempo) != null)
			this.reservas.remove(this.reservaActiva(tiempo));
	}

	public String toString(LocalDateTime tiempo) {
		return "Usuario [id=" + id + ", reservas=" + reservas + ", alquileres=" + alquileres + ", bloqueado="
				+ bloqueado(tiempo) + ", tiempo de uso de hoy=" + tiempoUsoHoy(tiempo)
				+ " minutos, tiempo de uso de la semana=" + tiempoUsoSemana(tiempo) + " minutos]";
	}

}
