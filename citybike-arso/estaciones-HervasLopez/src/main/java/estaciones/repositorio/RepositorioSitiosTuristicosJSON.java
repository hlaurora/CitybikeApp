package estaciones.repositorio;

import estaciones.modelo.SitioTuristico;
import repositorio.RepositorioJSON;

public class RepositorioSitiosTuristicosJSON extends RepositorioJSON<SitioTuristico> {
	
	@Override
	public Class<SitioTuristico> getClase() {
		return SitioTuristico.class;
	}
	
}
