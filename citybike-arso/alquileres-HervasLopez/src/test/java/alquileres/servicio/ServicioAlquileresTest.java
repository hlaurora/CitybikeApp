package alquileres.servicio;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import alquileres.modelo.Usuario;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;
import servicio.ServicioException;

class ServicioAlquileresTest {

	private static IServicioAlquileres servicio = FactoriaServicios.getServicio(IServicioAlquileres.class);
	private static IServicioTiempo tiempo = FactoriaServicios.getServicio(IServicioTiempo.class);

	@BeforeEach
	public void setUp() {
		// Antes de cada test, volvemos a poner el reloj sin cambios
		tiempo.resetSystemTime();
	}
/*
	// Método reservar

	// No se puede crear una reserva para un usuario si este ya tiene una activa.
	@Test
	public void testReservarReservaActiva() {
		String idUsuario = "usuario1";
		String idBicicleta = "bicicleta1";
		try {
			servicio.reservar(idUsuario, idBicicleta);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		assertThrows(ServicioException.class, () -> {
			servicio.reservar(idUsuario, "bicicleta2");
		});

	}

	// No se puede crear una reserva para un usuario si este ya tiene un alquiler
	// activo.
	@Test
	public void testReservarAlquilerActivo() {
		String idUsuario = "usuario2";
		String idBicicleta = "bicicleta1";
		try {
			servicio.alquilar(idUsuario, idBicicleta);
		} catch (ServicioException | EntidadNoEncontrada | RepositorioException e) {
			System.out.println(e.getMessage());
		}
		assertThrows(ServicioException.class, () -> {
			servicio.reservar(idUsuario, "bicicleta2");
		});
	}

	// No se puede crear una reserva para un usuario si este está bloqueado.
	@Test
	public void testReservarUsuarioBloqueado() {
		String idUsuario = "usuario3";
		String idBicicleta = "bicicleta1";
		try {
			servicio.reservar(idUsuario, idBicicleta);

			tiempo.setFixedClockAt(tiempo.now().plusHours(1));

			servicio.reservar(idUsuario, idBicicleta);

			tiempo.setFixedClockAt(tiempo.now().plusHours(1));

			servicio.reservar(idUsuario, idBicicleta);

			tiempo.setFixedClockAt(tiempo.now().plusHours(1));

		} catch (ServicioException | EntidadNoEncontrada | RepositorioException e) {
			System.out.println(e.getMessage());
		}
		assertThrows(ServicioException.class, () -> {
			servicio.reservar(idUsuario, "bicicleta2");
		});

	}

	// No se puede crear una reserva para un usuario si este supera el tiempo de
	// uso.
	@Test
	public void testReservarUsuarioSuperaTiempo() {
		String idUsuario = "usuario4";
		String idBicicleta = "bicicleta1";
		try {
			servicio.alquilar(idUsuario, idBicicleta);
			tiempo.setFixedClockAt(tiempo.now().plusHours(2));
			servicio.dejarBicicleta(idUsuario, "estacion1");
		} catch (ServicioException | EntidadNoEncontrada | RepositorioException e) {
			fail("Error durante la configuración de la prueba: " + e.getMessage());
		}
		assertThrows(ServicioException.class, () -> {
			servicio.reservar(idUsuario, "bicicleta2");
		});

	}

	// Si se reserva una bicicleta correctamente, el usuario tendrá una reserva
	// activa.
	@Test
	public void testReservarOk() {
		String idUsuario = "usuario5";
		String idBicicleta = "bicicleta1";
		try {
			servicio.reservar(idUsuario, idBicicleta);
		} catch (ServicioException | EntidadNoEncontrada | RepositorioException e) {
			System.out.println(e.getMessage());
		}
		assertNotNull(servicio.getUsuario(idUsuario).reservaActiva(tiempo.now()));
	}

	// Método confirmarReserva

	// No se puede confirmar una reserva para un usuario que no tiene ninguna
	// activa.
	@Test
	public void testConfirmarReservaSinActiva() {
		String idUsuario = "usuario6";
		assertThrows(ServicioException.class, () -> {
			servicio.confirmarReserva(idUsuario);
		});
	}

	// Si se confirma una reserva, el usuario ya no tendrá una reserva activa y
	// tendrá un alquiler activo.
	@Test
	public void testConfirmarReservaConActiva() {
		String idUsuario = "usuario7";
		String idBicicleta = "bicicleta1";
		try {
			servicio.reservar(idUsuario, idBicicleta);
			servicio.confirmarReserva(idUsuario);
		} catch (ServicioException | EntidadNoEncontrada | RepositorioException e) {
			System.out.println(e.getMessage());
		}
		assertNull(servicio.getUsuario(idUsuario).reservaActiva(tiempo.now()));
		assertNotNull(servicio.getUsuario(idUsuario).alquilerActivo());
	}

	// Método alquilar

	// No se puede crear un alquiler para un usuario si este tiene una reserva
	// activa.
	@Test
	public void testAlquilarReservaActiva() {
		String idUsuario = "usuario8";
		String idBicicleta = "bicicleta1";
		try {
			servicio.reservar(idUsuario, idBicicleta);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		assertThrows(ServicioException.class, () -> {
			servicio.alquilar(idUsuario, "bicicleta2");
		});

	}

	// No se puede crear un alquiler para un usuario si este ya tiene un alquiler
	// activo.
	@Test
	public void testAlquilarAlquilerActivo() {
		String idUsuario = "usuario9";
		String idBicicleta = "bicicleta1";
		try {
			servicio.alquilar(idUsuario, idBicicleta);
		} catch (ServicioException | EntidadNoEncontrada | RepositorioException e) {
			System.out.println(e.getMessage());
		}
		assertThrows(ServicioException.class, () -> {
			servicio.alquilar(idUsuario, "bicicleta2");
		});
	}

	// No se puede crear un alquiler para un usuario si este está bloqueado.
	@Test
	public void testAlquilarUsuarioBloqueado() {
		String idUsuario = "usuario10";
		String idBicicleta = "bicicleta1";
		try {
			servicio.reservar(idUsuario, idBicicleta);

			tiempo.setFixedClockAt(tiempo.now().plusHours(1));

			servicio.reservar(idUsuario, idBicicleta);

			tiempo.setFixedClockAt(tiempo.now().plusHours(1));

			servicio.reservar(idUsuario, idBicicleta);

			tiempo.setFixedClockAt(tiempo.now().plusHours(1));

		} catch (ServicioException | EntidadNoEncontrada | RepositorioException e) {
			System.out.println(e.getMessage());
		}
		assertThrows(ServicioException.class, () -> {
			servicio.alquilar(idUsuario, "bicicleta2");
		});

	}

	// No se puede crear un alquiler para un usuario si este supera el tiempo de
	// uso.
	@Test
	public void testAlquilarUsuarioSuperaTiempo() {
		String idUsuario = "usuario11";
		String idBicicleta = "bicicleta1";
		try {
			servicio.alquilar(idUsuario, idBicicleta);
			tiempo.setFixedClockAt(tiempo.now().plusHours(2));
			servicio.dejarBicicleta(idUsuario, "estacion1");
		} catch (ServicioException | EntidadNoEncontrada | RepositorioException e) {
			System.out.println(e.getMessage());
		}
		assertThrows(ServicioException.class, () -> {
			servicio.alquilar(idUsuario, "bicicleta2");
		});

	}

	// Si se alquila una bicicleta correctamente, el usuario tendrá un alquiler
	// activo.
	@Test
	public void testAlquilarOk() {
		String idUsuario = "usuario12";
		String idBicicleta = "bicicleta1";
		try {
			servicio.alquilar(idUsuario, idBicicleta);
		} catch (ServicioException | EntidadNoEncontrada | RepositorioException e) {
			fail("Error durante la configuración de la prueba: " + e.getMessage());
		}
		assertNotNull(servicio.getUsuario(idUsuario).alquilerActivo());
	}

	// Método historialUsuario
	@Test
	public void testHistorialUsuario() {
		String idUsuario = "usuario13";
		String idBicicleta = "bicicleta2";
		try {
			servicio.alquilar(idUsuario, idBicicleta);

			Usuario usuario = servicio.historialUsuario(idUsuario);
			assertNotNull(usuario, "El usuario no debería ser nulo");
			assertEquals(1, usuario.getAlquileres().size(), "El usuario debería tener un alquiler en su historial");
			assertEquals(0, usuario.getReservas().size(), "El usuario no debería tener una reserva en su historial");

		} catch (ServicioException | EntidadNoEncontrada | RepositorioException e) {
			fail("Error durante la configuración de la prueba: " + e.getMessage());
		}

	}

	// Método dejar bicicleta

	// El usuario no puede dejar una bicicleta si no tiene un alquiler activo
	@Test
	public void testDejarBicicletaSinAlquilerActivo() {
		String idUsuario = "usuario14";
		try {
			servicio.reservar(idUsuario, "bici");
		} catch (ServicioException | EntidadNoEncontrada | RepositorioException e) {
			fail("Error durante la configuración de la prueba: " + e.getMessage());
		}
		assertThrows(ServicioException.class, () -> {
			servicio.dejarBicicleta(idUsuario, "estacion");
		});
	}

	@Test
	public void testDejarBicicletaOk() {
		String idUsuario = "usuario15";
		String idBicicleta = "bicicleta";
		try {
			servicio.alquilar(idUsuario, idBicicleta);
			servicio.dejarBicicleta(idUsuario, idBicicleta);
			assertNull(servicio.historialUsuario(idUsuario).alquilerActivo());
		} catch (ServicioException | RepositorioException | EntidadNoEncontrada e) {
			fail("Error durante la configuración de la prueba: " + e.getMessage());
		}
	}

	// Método liberarBloqueo
	@Test
	public void liberarBloqueoOk() {
		String idUsuario = "usuario16";
		String idBicicleta = "bicicleta";
		try {
			servicio.reservar(idUsuario, idBicicleta);
			tiempo.setFixedClockAt(tiempo.now().plusHours(1));
			servicio.reservar(idUsuario, idBicicleta);
			tiempo.setFixedClockAt(tiempo.now().plusHours(1));
			servicio.reservar(idUsuario, idBicicleta);
			tiempo.setFixedClockAt(tiempo.now().plusHours(1));
			servicio.liberarBloqueo(idUsuario);
			assertFalse(servicio.historialUsuario(idUsuario).bloqueado(tiempo.now()));
		} catch (ServicioException | EntidadNoEncontrada | RepositorioException e) {
			fail("Error durante la configuración de la prueba: " + e.getMessage());
		}
	}
*/

}
