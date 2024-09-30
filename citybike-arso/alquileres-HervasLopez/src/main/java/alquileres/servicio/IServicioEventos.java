package alquileres.servicio;

import java.time.LocalDateTime;

public interface IServicioEventos {

	void bicicletaAlquilada(String idBici, LocalDateTime horaInicio) throws Exception;

	void bicicletaAlquilerConcluido(String idBici, LocalDateTime horaFin) throws Exception;
	
}
