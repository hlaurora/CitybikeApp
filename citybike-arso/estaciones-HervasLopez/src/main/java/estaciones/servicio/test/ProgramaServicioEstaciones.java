package estaciones.servicio.test;

import java.util.ArrayList;
import java.util.List;

import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.modelo.SitioTuristico;
import estaciones.servicio.IServicioEstaciones;
import estaciones.servicio.ISitiosTuristicos;
import estaciones.servicio.SitioTuristicoResumen;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;
import servicio.ServicioException;

/* Para probar la nueva funcionalidad del servicioEstaciones, relativa al
	alta y baja de bicicletas en las estaciones */
public class ProgramaServicioEstaciones {

	public static void main(String[] args) throws RepositorioException, EntidadNoEncontrada, ServicioException {

		ISitiosTuristicos servicioSitios = FactoriaServicios.getServicio(ISitiosTuristicos.class);
		IServicioEstaciones servicioEstacion = FactoriaServicios.getServicio(IServicioEstaciones.class);

		// Latitud y longitud de prueba (5 sitios Turisticos)
		// https://api.geonames.org/findNearbyWikipedia?lat=38.023694444444&lng=-1.1741388888889&username=aadd&lang=es
		double lat = 38.023694444444; // Otra prueba: 47
		double lng = -1.1741388888889; // Otra prueba: 9

		// Estación de prueba con esas coordenadas y con sitios turísticos
		String idEstacion1 = servicioEstacion.altaEstacion("estacion1", 2, "calle1", lat, lng);

		// Obtenemos el listado de resumenes de sitios turisticos cercanos a esta nueva
		// estacion
		List<SitioTuristicoResumen> infoSitios = servicioEstacion.getSitiosTuristicos(idEstacion1);

		// Obtenemos la información de estos sitios
		List<SitioTuristico> sitiosTuristicos = new ArrayList<SitioTuristico>();
		SitioTuristico s;
		for (SitioTuristicoResumen sr : infoSitios) {
			s = servicioSitios.getInfoSitioInteres(sr.getId());
			sitiosTuristicos.add(s);
		}
		// Asociamos la coleccion de sitios turisticos a la estación

		servicioEstacion.establecerSitiosTuristicos(idEstacion1, sitiosTuristicos);

		Estacion estacion1 = servicioEstacion.getEstacion(idEstacion1);

		///// Alta de una bicicleta /////

		// Damos de alta dos bicicletas en la estacion1

		String idBici1 = servicioEstacion.altaBicicleta("Modelo a", estacion1);
		String idBici2 = servicioEstacion.altaBicicleta("Modelo b", estacion1);
		System.out.println("\n---Obtenemos las bicicletas creadas en la estacion1---\n");
		// Imprime los ids de las bicicletas
		for (String b : servicioEstacion.getEstacion(idEstacion1).getBicicletas()) {
			System.out.println(b);
		}

		///// Estacionar una bicicleta ///// Retirar una bibicleta /////

		// Creamos una nueva estación
		String idEstacion2 = servicioEstacion.altaEstacion("estacion2", 2, "calle1", 50, 30);

		// Estaciona la bici1 en la Estacion2
		// Para ello la Retira de la Estacion1 (retirarBicicleta)
		servicioEstacion.estacionarBicicleta(idBici1, idEstacion2);

		System.out.println("\n---Obtenemos las bicicletas aparcadas en las dos estaciones---\n");
		System.out.println("Estación 1:");
		for (String b : servicioEstacion.getEstacion(idEstacion1).getBicicletas()) {
			System.out.println(b.toString());
		}
		System.out.println("Estación 2:");
		for (String b : servicioEstacion.getEstacion(idEstacion2).getBicicletas()) {
			System.out.println(b.toString());
		}

		///// Dar de baja una bibicleta /////
		// Damos de baja la bicicleta 2 , aparcada en la estación 2
		servicioEstacion.bajaBicicleta(idBici2, "Bici defectuosa");
		System.out.println("\n---Obtenemos las bicicletas aparcadas en la estación 2---\n");
		System.out.println(servicioEstacion.getEstacion(idEstacion1).getBicicletas().toString());

		///// Recuperar bicicletas estacionadas cerca de una posición /////

		// Establecemos las coordenadas siguientes que están a menos de 2km de distancia
		// de las coordenadas lat y lng en las que se encuentra la Estacion1
		double latCerca = 38.025;
		double lngCerca = -1.175;

		// Añadimos otra Bicicleta a la Estacion1
		servicioEstacion.altaBicicleta("Modelo b", estacion1);

		// Damos de alta otra estación que esté alejada de esas coordenadas
		String idEstacionLejana = servicioEstacion.altaEstacion("estacionLejana", 2, "calleLejana", 47, 9);
		Estacion estacionLejana = servicioEstacion.getEstacion(idEstacionLejana);
		// y le añadimos una bici
		servicioEstacion.altaBicicleta("Modelo a", estacionLejana);

		// Vemos que solo obtiene las bicis de la Estacion1
		System.out.println("\n---Recuperamos las bicicletas disponibles que se encuentran en las "
				+ "estaciones cercanas a las coordenadas latCerca, lngCerca---\n");
		for (Bicicleta b : servicioEstacion.getBicicletasCercanas(latCerca, lngCerca)) {
			System.out.println(b.toString());
		}

		///// Recuperar las estaciones con mayor número de sitios turísticos cerca /////

		// Creamos otras 2 estaciones con menos sitios turísticos cerca:
		// estacion1>turistica2>turistica3>estacion2>estacionLejana

		// 3 sitios
		// http://api.geonames.org/findNearbyWikipedia?lat=38.2857900&lng=-1.0741200&username=aadd&lang=es

		String idTuristica2 = servicioEstacion.altaEstacion("turistica2", 2, "calle2", 38.2857900, -1.0741200);

		// Obtenemos el listado de resumenes de sitios turisticos cercanos a esta
		// Estacion
		List<SitioTuristicoResumen> infoSitios2 = servicioEstacion.getSitiosTuristicos(idTuristica2);
		// Obtenemos la información de estos sitios
		List<SitioTuristico> sitiosTuristicos2 = new ArrayList<SitioTuristico>();
		for (SitioTuristicoResumen sr : infoSitios2) {
			s = servicioSitios.getInfoSitioInteres(sr.getId());
			sitiosTuristicos2.add(s);
		}

		// Asociamos la coleccion de sitios turisticos a la estación
		servicioEstacion.establecerSitiosTuristicos(idTuristica2, sitiosTuristicos2);

		// 1 sitio:
		// http://api.geonames.org/findNearbyWikipedia?lat=38.4426800&lng=-2.0975500&username=aadd&lang=es

		String idTuristica3 = servicioEstacion.altaEstacion("turistica3", 2, "calle3", 38.4426800, -2.0975500);

		// Obtenemos el listado de resumenes de sitios turisticos cercanos a esta
		// Estacion
		List<SitioTuristicoResumen> infoSitios3 = servicioEstacion.getSitiosTuristicos(idTuristica3);
		// Obtenemos la información de estos sitios
		List<SitioTuristico> sitiosTuristicos3 = new ArrayList<SitioTuristico>();
		for (SitioTuristicoResumen sr : infoSitios3) {
			s = servicioSitios.getInfoSitioInteres(sr.getId());
			sitiosTuristicos3.add(s);
		}

		// Asociamos la coleccion de sitios turisticos a la estación
		servicioEstacion.establecerSitiosTuristicos(idTuristica3, sitiosTuristicos3);

		System.out.println("\n---Obtenemos las estaciones ordenadas de mayor a menos número de sitios"
				+ " turísticos que tengan cerca---\n");
		for (Estacion e : servicioEstacion.getEstacionesTuristicas()) {
			System.out.println(e.toString());
		}

	}

}
