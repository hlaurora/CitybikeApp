package estaciones.servicio.test;

import java.util.List;

import estaciones.modelo.Estacion;
import estaciones.modelo.Incidencia;
import estaciones.servicio.IServicioEstaciones;
import estaciones.servicio.IServicioIncidencias;
import servicio.FactoriaServicios;

public class ProgramaServicioIncidencias {
	public static void main(String[] args) throws Exception {

		IServicioEstaciones servicioEstacion = FactoriaServicios.getServicio(IServicioEstaciones.class);
		IServicioIncidencias servicioIncidencia = FactoriaServicios.getServicio(IServicioIncidencias.class);
		double lat = 38.023694444444;
		double lng = -1.1741388888889;

		// Creamos una estación y damos de alta una bicicleta en esta
		String idEstacion1 = servicioEstacion.altaEstacion("nuevaEstacion", 6, "calle", lat, lng);
		Estacion estacion1 = servicioEstacion.getEstacion(idEstacion1);
		String idBici1 = servicioEstacion.altaBicicleta("modelo a", estacion1);

		///// Creación de incidencias /////

		// Creamos una nueva incidencia en la bicicleta 1
		String idIncidencia1 = servicioIncidencia.crear(idBici1, "Nueva incidencia");
		System.out.println(
				"\n---Obtenemos la bicicleta y comprobamos que tiene una incidencia y que se encuentra no disponible---\n");
		System.out.println(servicioEstacion.getBicicleta(idBici1));

		// Creamos otras bicicletas y una incidencia sobre cada una de ellas para realizar las siguientes
		// pruebas
		String idBici2 = servicioEstacion.altaBicicleta("modelo b", estacion1);
		String idIncidencia2 = servicioIncidencia.crear(idBici2, "Nueva incidencia 2");
		String idBici3 = servicioEstacion.altaBicicleta("modelo c", estacion1);
		String idIncidencia3 = servicioIncidencia.crear(idBici3, "Nueva incidencia 3");
		String idBici4 = servicioEstacion.altaBicicleta("modelo c", estacion1);
		String idIncidencia4 = servicioIncidencia.crear(idBici4, "Nueva incidencia4");

		///// Gestión de incidencias /////

		///// Cambio a estado cancelada /////
		Incidencia incidencia1 = servicioIncidencia.cancelarIncidencia(idBici1, idIncidencia1, "Todo correcto");
		System.out.println(
				"\n---Obtenemos la incidencia cancelada y mostramos la bicicleta para ver que vuelve a estar disponible---\n");
		System.out.println(incidencia1.toString());
		System.out.println(servicioEstacion.getBicicleta(idBici1).toString());

		///// Cambio a estado asignada /////
		Incidencia incidencia2 = servicioIncidencia.asignarIncidencia(idBici2, idIncidencia2, "Maria");
		System.out.println(
				"\n---Obtenemos la incidencia asignada y mostramos la bicicleta para ver que ya no está en la estación---\n");
		System.out.println(incidencia2.toString());
		System.out.println(servicioEstacion.getBicicleta(idBici2).toString());

		///// Cambio a estado resuelta - reparada /////
		servicioIncidencia.asignarIncidencia(idBici3, idIncidencia3, "Paco");
		Incidencia incidencia3 = servicioIncidencia.resolverIncidenciaReparada(idBici3, idIncidencia3, "reparada");
		System.out.println("\n---Obtenemos la incidencia resuelta---\n");
		System.out.println(incidencia3.toString());
		System.out.println(servicioEstacion.getBicicleta(idBici3).toString());

		///// Cambio a estado resuelta - no reparable /////
		servicioIncidencia.asignarIncidencia(idBici4, idIncidencia4, "Juana");
		Incidencia incidencia4 = servicioIncidencia.resolverIncidenciaNoReparable(idBici4, idIncidencia4,
				"no reparada");
		System.out.println(
				"\n---Obtenemos la incidencia resuelta y mostramos la bicicleta 2 para ver que esta se ha dado de baja---\n");
		System.out.println(incidencia4.toString());
		System.out.println(servicioEstacion.getBicicleta(idBici4).toString());

		///// Recuperación de incidencias abiertas /////
		List<Incidencia> abiertas = servicioIncidencia.recuperarAbiertas();
		System.out.println("\n---Obtenemos la lista de incidencias abiertas---\n");
		System.out.println(abiertas.toString());

	}

}
