package alquileres.servicio;

import java.io.IOException;

import alquileres.modelo.Alquiler;
import alquileres.modelo.Reserva;
import alquileres.modelo.Usuario;
import alquileres.persistencia.jpa.UsuarioEntidad;
import alquileres.repositorio.RepositorioUsuarioAdHoc;
import alquileres.rest.AlquilerDTO;
import alquileres.rest.ReservaDTO;
import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.RepositorioException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import servicio.FactoriaServicios;
import servicio.ServicioException;

public class ServicioAlquileres implements IServicioAlquileres {

	private RepositorioUsuarioAdHoc repositorioUsuarios = FactoriaRepositorios.getRepositorio(UsuarioEntidad.class);
	private IServicioTiempo tiempo = FactoriaServicios.getServicio(IServicioTiempo.class);
	private IServicioEventos servicioEventos = FactoriaServicios.getServicio(IServicioEventos.class);

	private Retrofit retrofit = new Retrofit.Builder().baseUrl("http://estaciones:8070/")
			.addConverterFactory(JacksonConverterFactory.create()).build();
	private IServicioEstaciones servicioEstaciones = retrofit.create(IServicioEstaciones.class);

	@Override
	public void reservar(String idUsuario, String idBicicleta)
			throws ServicioException, EntidadNoEncontrada, RepositorioException {
		Usuario usuario = recuperarUsuario(idUsuario);
		// No está permitida la reserva si el usuario tiene una reservaActiva.
		if (usuario.reservaActiva(tiempo.now()) != null)
			throw new ServicioException("Error en reservar: El usuario ya tiene una reserva activa");

		// No está permitida la reserva si el usuario tiene un alquiler activo.
		if (usuario.alquilerActivo() != null)
			throw new ServicioException("Error en reservar: El usuario ya tiene un alquiler activo");
		// No está permitida la reserva si el usuario está bloqueado o superaTiempo.
		if (usuario.superaTiempo(tiempo.now()) || usuario.bloqueado(tiempo.now()))
			throw new ServicioException("Error en reservar: El usuario está bloqueado o supera el tiempo");
		
		try {
			// Al alquilar una bicicleta se produce el evento "bicicleta-alquilada"
			servicioEventos.bicicletaAlquilada(idBicicleta, tiempo.now());
		} catch (Exception e) {
			throw new ServicioException(e.getMessage());
		}
		
		usuario.reservar(idBicicleta, tiempo.now());
		repositorioUsuarios.update(usuario);
	}

	@Override
	public void confirmarReserva(String idUsuario) throws ServicioException, EntidadNoEncontrada, RepositorioException {
		Usuario usuario = recuperarUsuario(idUsuario);
		if (usuario.reservaActiva(tiempo.now()) == null)
			throw new ServicioException("Error en confirmarReserva: El usuario no tiene una reserva activa");
		try {
			// Al confirmar una reserva se produce el evento "bicicleta-alquilada"
			servicioEventos.bicicletaAlquilada(usuario.reservaActiva(tiempo.now()).getIdBicicleta(), tiempo.now());
		} catch (Exception e) {
			throw new ServicioException(e.getMessage());
		}
		usuario.confirmarReserva(tiempo.now());
		repositorioUsuarios.update(usuario);
	}

	@Override
	public void alquilar(String idUsuario, String idBicicleta)
			throws ServicioException, EntidadNoEncontrada, RepositorioException {
		Usuario usuario = recuperarUsuario(idUsuario);
		// No está permitida la reserva si el usuario tiene una reservaActiva.
		if (usuario.reservaActiva(tiempo.now()) != null)
			throw new ServicioException("Error en alquilar: El usuario ya tiene una reserva activa");

		// No está permitida la reserva si el usuario tiene un alquiler activo.
		if (usuario.alquilerActivo() != null)
			throw new ServicioException("Error en alquilar: El usuario ya tiene un alquiler activo");
		// No está permitida la reserva si el usuario está bloqueado o superaTiempo.
		if (usuario.superaTiempo(tiempo.now()) || usuario.bloqueado(tiempo.now()))
			throw new ServicioException("Error en alquilar: El usuario está bloqueado o supera el tiempo");

		try {
			// Al alquilar una bicicleta se produce el evento "bicicleta-alquilada"
			servicioEventos.bicicletaAlquilada(idBicicleta, tiempo.now());
		} catch (Exception e) {
			throw new ServicioException(e.getMessage());
		}
		usuario.alquilar(idBicicleta, tiempo.now());
		repositorioUsuarios.update(usuario);

	}

	@Override
	public Usuario historialUsuario(String idUsuario) throws RepositorioException {
		Usuario usuario = recuperarUsuario(idUsuario);
		return usuario;
	}
	
	//Añadida para daweb
	@Override
	public ReservaDTO getReservaActiva(String idUsuario) throws RepositorioException, ServicioException {
		Usuario usuario = recuperarUsuario(idUsuario);
		Reserva activa = usuario.reservaActiva(tiempo.now());

		// El usuario debe tener un alquiler activo
		if (activa == null)
			throw new ServicioException("Error: El usuario no tiene ningúna reserva activa");
		return new ReservaDTO(activa.getIdBicicleta(), activa.getCreada(), activa.getCaducidad(), idUsuario);
	}
	
	//Añadida para daweb
	@Override
	public AlquilerDTO getAlquilerActivo(String idUsuario) throws RepositorioException, ServicioException {
		Usuario usuario = recuperarUsuario(idUsuario);
		Alquiler activo = usuario.alquilerActivo();

		// El usuario debe tener un alquiler activo
		if (activo == null)
			throw new ServicioException("Error: El usuario no tiene ningún alquiler activo");
		return new AlquilerDTO(activo.getIdBicicleta(), activo.getInicio(), activo.getFin(), idUsuario);
	}
	
	@Override
	public void cancelarReserva(String idUsuario) throws RepositorioException, ServicioException, EntidadNoEncontrada {
		Usuario usuario = recuperarUsuario(idUsuario);
		// El usuario debe tener un alquiler activo
		if (usuario.reservaActiva(tiempo.now()) == null)
			throw new ServicioException("Error en dejar bicicleta: El usuario no tiene ninguna reserva activa");
		
		String idBicicleta = usuario.cancelarReserva(tiempo.now());
		repositorioUsuarios.update(usuario);
		try {
			// Al dejar una bicicleta se produce el evento "bicicleta-alquiler-concluido"
			servicioEventos.bicicletaAlquilerConcluido(idBicicleta, tiempo.now());
		} catch (Exception e) {
			throw new ServicioException(e.getMessage());
		}

	}

	@Override
	public void dejarBicicleta(String idUsuario, String idEstacion)
			throws ServicioException, EntidadNoEncontrada, RepositorioException {
		Usuario usuario = recuperarUsuario(idUsuario);
		// El usuario debe tener un alquiler activo
		if (usuario.alquilerActivo() == null)
			throw new ServicioException("Error en dejar bicicleta: El usuario no tiene ningún alquiler activo");

		// No puede dejar la bici si no hay hueco en la estacion
		Response<EstacionResumen> resultado;
		try {
			// Se comprueba si la estación tiene hueco con el servicio estaciones con retrofit
			resultado = servicioEstaciones.tieneHueco(idEstacion).execute();
			if (!resultado.body().isHuecosLibres())
				throw new ServicioException("Error en dejar bicicleta: No hay hueco disponible en la estacion");

			// Se situa la bicicleta haciendo uso del servicio estaciones con retrofit
			servicioEstaciones.situarBicicleta(idEstacion, usuario.alquilerActivo().getIdBicicleta()).execute();

			try {
				// Al dejar una bicicleta se produce el evento "bicicleta-alquiler-concluido"
				servicioEventos.bicicletaAlquilerConcluido(usuario.alquilerActivo().getIdBicicleta(), tiempo.now());
			} catch (Exception e) {
				throw new ServicioException(e.getMessage());
			}
			usuario.dejarBicicleta(tiempo.now());
			repositorioUsuarios.update(usuario);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void liberarBloqueo(String idUsuario) throws RepositorioException, EntidadNoEncontrada {
		Usuario usuario = recuperarUsuario(idUsuario);
		usuario.liberarBloqueo(tiempo.now());
		repositorioUsuarios.update(usuario);

	}

	// Cuando se recibe el evento "bicicleta-desactivada" se busca en los usuarios
	// si alguno tiene una reserva activa con esa bicicleta y se elimina la reserva
	@Override
	public void tratamientoBicicletaDesactivada(String idBicicleta) throws EntidadNoEncontrada, RepositorioException {
		for (Usuario usuario : repositorioUsuarios.obtenerUsuarios())
			if (usuario.reservaActiva(tiempo.now()) != null
					&& usuario.reservaActiva(tiempo.now()).getIdBicicleta().equals(idBicicleta)) {
				usuario.eliminarReservaActiva(tiempo.now());
				repositorioUsuarios.update(usuario);
			}

	}

	private Usuario recuperarUsuario(String idUsuario) throws RepositorioException {
		try {
			return repositorioUsuarios.obtenerPorId(idUsuario);
		} catch (Exception e) {
			Usuario usuario = new Usuario(idUsuario);
			repositorioUsuarios.add(usuario);

			return usuario;
		}
	}

	@Override
	public Usuario getUsuario(String idUsuario) {
		try {
			return repositorioUsuarios.obtenerPorId(idUsuario);
		} catch (Exception e) {
			System.out.println("No existe el usuario");
			return null;
		}
	}

}
