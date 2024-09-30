package alquileres.servicio.test;

import alquileres.servicio.IServicioAlquileres;
import alquileres.servicio.IServicioTiempo;
import servicio.FactoriaServicios;

public class ProgramaServicioAlquileresEj1 {

	public static void main(String[] args) throws Exception {

		IServicioAlquileres servicioAlquiler = FactoriaServicios.getServicio(IServicioAlquileres.class);
		IServicioTiempo tiempo = FactoriaServicios.getServicio(IServicioTiempo.class);
		
		String idBicicleta = "33f3bb7a-cb78-4e10-ba53-6ec5e1e0715b";
		String idBicicleta2 = "661905c5f58edc65ef8e9f95";
		String idBicicleta3 = "f07c50a6-21c8-40a7-bf78-8df610a9c61f";

		// Reservar
		//servicioAlquiler.reservar("U1", idBicicleta2);

		// Recuperamos el usuario y vemos las reservas
		//Usuario usuario = servicioAlquiler.getUsuario("U1");
		// Usuario usuario2 = servicioAlquiler.getUsuario("U2");

		//System.out.println(usuario.reservaActiva(tiempo.now()).getIdBicicleta());

		// Confirmar Reserva
		//servicioAlquiler.confirmarReserva("U1");
		
		servicioAlquiler.alquilar("U1", idBicicleta2);
		
		servicioAlquiler.dejarBicicleta("U1", "66190382aa9a800c7c25a4ec");
		
		// Imprimimos la reserva activa y vemos el alquiler
		//usuario = servicioAlquiler.getUsuario("U1");
		//System.out.println(usuario.reservaActiva(tiempo.now()));
		//System.out.println(usuario.alquilerActivo());

		// Alquilar
		// servicioAlquiler.alquilar("U1", idBicicleta2);
		// dará error porque tiene una reserva activa
		// si da error no funciona por eso lo comenté

		//servicioAlquiler.alquilar("U2", idBicicleta2);

		//Usuario usuario2 = servicioAlquiler.getUsuario("U2");

		// Dejar bicicleta
		//System.out.println("Alquiler activo:" + usuario2.alquilerActivo());
		//servicioAlquiler.dejarBicicleta("U2", "66158a10afea4f4fd7644dd6");

		//System.out.println("Alquiler activo:" + usuario2.alquilerActivo());
/*
		// Liberar bloqueo
		tiempo.setFixedClockAt(tiempo.now().minusDays(1));
		servicioAlquiler.reservar("U3", idBicicleta);
		tiempo.setFixedClockAt(tiempo.now().plusHours(1));
		servicioAlquiler.reservar("U3", idBicicleta2);
		tiempo.setFixedClockAt(tiempo.now().plusHours(1));
		servicioAlquiler.reservar("U3", idBicicleta3);
		tiempo.setFixedClockAt(tiempo.now().plusHours(1));
		Usuario usuario3 = servicioAlquiler.getUsuario("U3");
		System.out.println("Reservas caducadas:");
		System.out.println(usuario3.getReservasCaducadas(tiempo.now()));

		System.out.println(usuario3.bloqueado(tiempo.now()));
		// servicioAlquiler.liberarBloqueo(usuario3.getId());
		// System.out.println(usuario3.bloqueado(tiempo.now()));

		// Historial usuario
		Usuario historial = servicioAlquiler.historialUsuario("U1");
		System.out.println(historial.toString(tiempo.now()));
*/
	}

}
