package estaciones.repositorio;

import java.util.List;

import estaciones.modelo.Estacion;
import repositorio.RepositorioException;
import repositorio.RepositorioString;

public interface RepositorioEstacionesAdHoc extends RepositorioString<Estacion> {

	public Estacion estacionPrimeraLibre() throws RepositorioException ;
	public List<Estacion> getByCercania(double lat, double lng) throws RepositorioException;
	public List<Estacion> getByMayorSitiosTuristicos() throws RepositorioException;
	
}
