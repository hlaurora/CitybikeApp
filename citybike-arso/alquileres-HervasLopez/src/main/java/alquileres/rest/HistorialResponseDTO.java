package alquileres.rest;

import java.util.ArrayList;
import java.util.List;

import alquileres.modelo.Alquiler;
import alquileres.modelo.Reserva;

public class HistorialResponseDTO {

	private String idUsuario;
	private List<ReservaDTO> reservas;
	private List<AlquilerDTO> alquileres;
	private boolean bloqueado;
	private int tiempoUsoHoy;
	private int tiempoUsoSemana;

	public HistorialResponseDTO(String idUsuario, boolean bloqueado, int tiempoUsoHoy, int tiempoUsoSemana) {
		this.idUsuario = idUsuario;
		this.reservas = new ArrayList<>();
		this.alquileres = new ArrayList<>();
		this.bloqueado = bloqueado;
		this.tiempoUsoHoy = tiempoUsoHoy;
		this.tiempoUsoSemana = tiempoUsoSemana;
	}

	public void setReservas(List<Reserva> reservasEntity) {
		if (reservas==null) {
			reservas = new ArrayList<>();
		}
		for (Reserva r : reservasEntity) {
			reservas.add(reservaToDTO(r));
		}
	}
	
	public void setAlquileres(List<Alquiler> alquileresEntity) {
		if (alquileres==null) {
			alquileres = new ArrayList<>();
		}
		for (Alquiler a : alquileresEntity) {
			alquileres.add(alquilerToDTO(a));
		}
	}

	private ReservaDTO reservaToDTO(Reserva reserva) {
		return new ReservaDTO(reserva.getIdBicicleta(), reserva.getCreada(), reserva.getCaducidad(), this.idUsuario);
	}

	private AlquilerDTO alquilerToDTO(Alquiler alquiler) {
		return new AlquilerDTO(alquiler.getIdBicicleta(), alquiler.getInicio(), alquiler.getFin(), this.idUsuario);
	}

}
