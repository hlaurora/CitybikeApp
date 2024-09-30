package estaciones.servicio;

import java.util.List;

import estaciones.dto.IncidenciaDTO;
import estaciones.modelo.Incidencia;
import servicio.ServicioException;

public interface IServicioIncidencias {

	// Crear incidencia
	public String crear(String idBici, String descripcion) throws ServicioException;

	public IncidenciaDTO getIncidencia(String id);

	// Recuperar incidencia
	public List<Incidencia> recuperarAbiertas() throws ServicioException;

	public List<IncidenciaDTO> recuperarAbiertasDTO() throws ServicioException;

	// Gestión de incidencias
	public Incidencia cancelarIncidencia(String idBici, String idIncidencia, String motivo) throws ServicioException;

	public Incidencia asignarIncidencia(String idBici, String idIncidencia, String operario) throws ServicioException;

	public Incidencia resolverIncidenciaReparada(String idBici, String idIncidencia, String motivo)
			throws ServicioException;

	public Incidencia resolverIncidenciaNoReparable(String idBici, String idIncidencia, String motivo)
			throws ServicioException;

}
