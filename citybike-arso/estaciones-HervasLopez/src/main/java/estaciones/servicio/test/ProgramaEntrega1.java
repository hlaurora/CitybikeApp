package estaciones.servicio.test;

import java.util.ArrayList;
import java.util.List;

import estaciones.modelo.SitioTuristico;
import estaciones.servicio.IServicioEstaciones;
import estaciones.servicio.ISitiosTuristicos;
import estaciones.servicio.SitioTuristicoResumen;
import servicio.FactoriaServicios;

public class ProgramaEntrega1 {

	public static void main(String[] args) throws Exception {

		// Obtenemos el servicio de Sitios Turisticos y el de Estaciones
		ISitiosTuristicos servicioSitios = FactoriaServicios.getServicio(ISitiosTuristicos.class);
		IServicioEstaciones servicioEstacion = FactoriaServicios.getServicio(IServicioEstaciones.class);

		// Latitud y longitud de prueba
		double lat = 38.023694444444; // Otra prueba: 47
		double lng = -1.1741388888889; // Otra prueba: 9

		// Damos de alta una nueva Estacion
		String idNueva = servicioEstacion.altaEstacion("nuevaEstacion", 3, "calle", lat, lng);
		// Obtenemos el listado de resumenes de sitios turisticos cercanos a esta nueva
		// estacion
		List<SitioTuristicoResumen> infoSitios = servicioEstacion.getSitiosTuristicos(idNueva);
		
		// Obtenemos la información de estos sitios
		List<SitioTuristico> sitiosTuristicos = new ArrayList<SitioTuristico>();
		SitioTuristico s;
		for (SitioTuristicoResumen sr : infoSitios) {
			s = servicioSitios.getInfoSitioInteres(sr.getId());
			sitiosTuristicos.add(s);
		}
		// Asociamos la coleccion de sitios turisticos a la estación
		servicioEstacion.establecerSitiosTuristicos(idNueva, sitiosTuristicos);
		// Imprimimos la estación para comprobar que se ha creado y se han añadido
		// correctamente los sitios
		System.out.println("\n---Obtenemos la estación creada con sus sitios turisticos asociados---\n");
		System.out.println(servicioEstacion.getEstacion(idNueva).toString());

	}

}
