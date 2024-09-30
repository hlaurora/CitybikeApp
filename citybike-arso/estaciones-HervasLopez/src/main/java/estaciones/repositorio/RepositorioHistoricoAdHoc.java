package estaciones.repositorio;

import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.modelo.Historico;
import repositorio.RepositorioException;
import repositorio.RepositorioString;

public interface RepositorioHistoricoAdHoc extends RepositorioString<Historico> {

	public Historico getByBiciYEstacion(Bicicleta bicicleta, Estacion estacion) throws RepositorioException;	
	
}
