package estaciones.repositorio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import estaciones.modelo.Bicicleta;

public interface RepositorioBicicletasMongoDB extends RepositorioBicicletasAdHoc, MongoRepository<Bicicleta, String> {

	// Busca las  bicicletas que se encuentran estacionadas en la Estacion con idEstacionActual
	// Listado paginado
    Page<Bicicleta> findByidEstacionActual(Pageable pageable, String idEstacionActual);
    
    // Busca las  bicicletas que se encuentran estacionadas y disponibles en la Estacion con idEstacionActual
  	// Listado paginado
    Page<Bicicleta> findByidEstacionActualAndDisponibleIsTrue(Pageable pageable, String idEstacionActual);
   
    
    // Añadida para daweb
    // Busca las  bicicletas que se encuentran estacionadas en la Estacion con idEstacionActual (sin paginar)
    List<Bicicleta> findByidEstacionActual(String idEstacionActual);

    List<Bicicleta> findByidEstacionActualAndDisponibleIsTrue(String idEstacionActual);
}
