package estaciones.repositorio;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import estaciones.modelo.Estacion;

@NoRepositoryBean
public interface RepositorioEstacionesAdHoc extends PagingAndSortingRepository<Estacion, String> {


}
