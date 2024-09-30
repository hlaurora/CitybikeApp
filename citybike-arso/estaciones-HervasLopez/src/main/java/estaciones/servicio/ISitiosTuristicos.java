package estaciones.servicio;

import java.util.List;

import estaciones.modelo.SitioTuristico;
import servicio.ServicioException;

//Define los métodos getSitioInteres y getInfoSitioInteres para la obtención de sitios turísticos
public interface ISitiosTuristicos {
	public List<SitioTuristicoResumen> getSitioInteres(double lat, double lng) throws ServicioException;

	public SitioTuristico getInfoSitioInteres(String idSitio) throws ServicioException;
}
