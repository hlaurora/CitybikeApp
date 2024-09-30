package alquileres.servicio;

import alquileres.modelo.Usuario;
import alquileres.rest.AlquilerDTO;
import alquileres.rest.ReservaDTO;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.ServicioException;

public interface IServicioAlquileres {

	void reservar(String idUsuario, String idBicicleta)
			throws ServicioException, EntidadNoEncontrada, RepositorioException;

	void confirmarReserva(String idUsuario) throws ServicioException, EntidadNoEncontrada, RepositorioException;

	void alquilar(String idUsuario, String idBicicleta)
			throws ServicioException, EntidadNoEncontrada, RepositorioException;

	Usuario historialUsuario(String idUsuario) throws RepositorioException;

	void dejarBicicleta(String idUsuario, String idEstacion)
			throws ServicioException, EntidadNoEncontrada, RepositorioException;

	void liberarBloqueo(String idUsuario) throws RepositorioException, EntidadNoEncontrada;

	Usuario getUsuario(String idUsuario);

	void tratamientoBicicletaDesactivada(String idBicicleta) throws EntidadNoEncontrada, RepositorioException;
	
	//DAWEB
	ReservaDTO getReservaActiva(String idUsuario) throws RepositorioException, ServicioException;
	AlquilerDTO getAlquilerActivo(String idUsuario) throws RepositorioException, ServicioException;
	void cancelarReserva(String idUsuario) throws RepositorioException, ServicioException, EntidadNoEncontrada;
}
