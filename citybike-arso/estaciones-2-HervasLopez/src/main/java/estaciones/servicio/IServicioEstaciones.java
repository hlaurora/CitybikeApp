package estaciones.servicio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import estaciones.dto.BicicletaDTO;
import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import repositorio.EntidadNoEncontrada;
import servicio.ServicioException;

//Define los métodos altaEstacion, getEstacion, getSitiosTuristicos y establecerSitiosTuristicos
//para la gestión de las estaciones y la obtención de sitios turisticos
public interface IServicioEstaciones {

	String altaEstacion(String nombre, int numPuestos, String direccion, String codigoPostal);

	String altaBicicleta(String modelo, String idEstacion) throws EntidadNoEncontrada, ServicioException;

	void bajaBicicleta(String idBici, String motivo) throws EntidadNoEncontrada;

	Page<BicicletaDTO> getBicicletasListadoPaginado(Pageable pageable, String idEstacion);

	Page<EstacionResumen> getEstacionesListadoPaginado(Pageable pageable);
	
	//añadidas para daweb
	List<EstacionResumen> getEstacionesSinPaginar();
	EstacionResumen editarEstacion(String id, String nombre, int numPuestos, String direccion) throws EntidadNoEncontrada;
	void eliminarEstacion(String id) throws EntidadNoEncontrada, ServicioException;
	List<BicicletaDTO> getBicicletasByEstacion(String idEstacion);
	List<BicicletaDTO> getBicicletasDisponibles(String idEstacion);

		
	Page<BicicletaDTO> getBicicletasDisponiblesListadoPaginado(Pageable pageable, String idEstacion);

	EstacionResumen getInfoEstacion(String idEstacion) throws EntidadNoEncontrada;

	void estacionarBicicleta(String idBici, String idEstacion) throws EntidadNoEncontrada, ServicioException;

	Estacion getEstacion(String id) throws EntidadNoEncontrada;

	Bicicleta getBicicleta(String id) throws EntidadNoEncontrada;

	void setDisponibilidad(String idBici, boolean bool) throws EntidadNoEncontrada;
	
	BicicletaDTO transformToDTO(Bicicleta bicicleta);

}
