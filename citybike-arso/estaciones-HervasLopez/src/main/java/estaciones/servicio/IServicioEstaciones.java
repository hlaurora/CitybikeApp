package estaciones.servicio;

import java.util.List;

import estaciones.dto.BicicletaDTO;
import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.modelo.SitioTuristico;
import servicio.ServicioException;

//Define los métodos altaEstacion, getEstacion, getSitiosTuristicos y establecerSitiosTuristicos
//para la gestión de las estaciones y la obtención de sitios turisticos
public interface IServicioEstaciones {
	
	public String altaEstacion(String nombre, int numPuestos, String direccion, double lat, double lng) throws ServicioException;
	public Estacion getEstacion(String id) throws ServicioException;
	public Bicicleta getBicicleta(String id) throws ServicioException;
	public List<SitioTuristicoResumen> getSitiosTuristicos(String idEstacion) throws ServicioException;
	public void establecerSitiosTuristicos(String idEstacion, List<SitioTuristico> sitios) throws ServicioException;
	public String altaBicicleta(String modelo, Estacion estacion) throws ServicioException;
	public void estacionarBicicleta(String idBici, String idEstacion) throws ServicioException;
	public void estacionarBicicleta(String idBici) throws ServicioException;
	public void retirarBicicleta(String idBici) throws ServicioException;
	public void bajaBicicleta(String idBici, String motivo) throws ServicioException;
	public List<Bicicleta> getBicicletasCercanas(double lat, double lng) throws ServicioException;
	public List<BicicletaDTO> getBicicletasCercanasDTO(double lat, double lng) throws ServicioException;
	public List<Estacion> getEstacionesTuristicas() throws ServicioException;
}
