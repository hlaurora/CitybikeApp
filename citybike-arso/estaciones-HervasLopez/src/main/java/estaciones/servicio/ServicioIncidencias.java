package estaciones.servicio;

import java.util.ArrayList;
import java.util.List;

import estaciones.dto.IncidenciaDTO;
import estaciones.modelo.Bicicleta;
import estaciones.modelo.EstadoIncidencia;
import estaciones.modelo.Incidencia;
import estaciones.repositorio.RepositorioBicicletaJPA;
import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;
import servicio.ServicioException;

public class ServicioIncidencias implements IServicioIncidencias {

	private RepositorioBicicletaJPA repositorioBicicletas = FactoriaRepositorios.getRepositorio(Bicicleta.class);
	private IServicioEstaciones servicioEstacion = FactoriaServicios.getServicio(IServicioEstaciones.class);

	// Crea una incidencia en la bicicleta con el id pasado como parámetro y
	// devuelve el id de la nueva incidencia
	@Override
	public String crear(String idBici, String descripcion) throws ServicioException {
		// Control de integridad de los datos
		if (idBici == null || idBici.isEmpty())
			throw new IllegalArgumentException("idBici: no debe ser nulo ni vacio");
		if (descripcion == null || descripcion.isEmpty())
			throw new IllegalArgumentException("descripcion: no debe ser nula ni vacia");
		// Obtenemos la bicicleta del repositorio
		Bicicleta bicicleta;
		try {
			bicicleta = repositorioBicicletas.getById(idBici);
		} catch (EntidadNoEncontrada | RepositorioException e) {
			throw new ServicioException(e.getMessage());
		}
		String idIncidencia = null;
		// Comprobamos que la bicicleta con idBici se ha obtenido del repositorio
		if (bicicleta != null && bicicleta.isDisponible()) {
			// Añadimos la incidencia a la bicicleta
			idIncidencia = bicicleta.addIncidencia(descripcion);
		} else if (!bicicleta.isDisponible()) {
			System.out.println("No se puede crear una incidencia sobre la bicicleta con id" + idBici
					+ " ya que esta no está disponible\n");
		}
		// Actualizamos la bicicleta en el repositorio
		try {
			repositorioBicicletas.update(bicicleta);
		} catch (RepositorioException | EntidadNoEncontrada e) {
			throw new ServicioException(e.getMessage());
		}
		return idIncidencia;
	}

	public IncidenciaDTO getIncidencia(String id) {
		Incidencia incidencia = repositorioBicicletas.getIncidenciaById(id);
		return transformToDTO(incidencia);
	}

	// Recupera las incidencias abiertas de todas las bicicletas
	@Override
	public List<Incidencia> recuperarAbiertas() throws ServicioException {

		try {
			return repositorioBicicletas.recuperarIncidenciasAbiertas();
		} catch (RepositorioException | EntidadNoEncontrada e) {
			throw new ServicioException(e.getMessage());
		}

	}

	public List<IncidenciaDTO> recuperarAbiertasDTO() throws ServicioException {
		List<Incidencia> incidenciasAbiertas = recuperarAbiertas();
		List<IncidenciaDTO> incidenciasAbiertasDTO = new ArrayList<IncidenciaDTO>();
		for (Incidencia i : incidenciasAbiertas) {
			if (i != null)
				incidenciasAbiertasDTO.add(transformToDTO(i));
		}
		return incidenciasAbiertasDTO;

	}

	// Cancela una incidencia de una bicicleta, devuelve la incidencia cancelada
	@Override
	public Incidencia cancelarIncidencia(String idBici, String idIncidencia, String motivo) throws ServicioException {
		// Recupera la bicicleta del repositorio
		Bicicleta bici;
		try {
			bici = repositorioBicicletas.getById(idBici);
		} catch (EntidadNoEncontrada | RepositorioException e) {
			throw new ServicioException(e.getMessage());
		}
		// Se cancela la incidencia, indicando el motivo de cancelación
		Incidencia incidencia = bici.cambiarEstadoIncidencia(idIncidencia, EstadoIncidencia.CANCELADA, motivo);
		// Se actualiza la bicicleta
		try {
			repositorioBicicletas.update(bici);
		} catch (RepositorioException | EntidadNoEncontrada e) {
			throw new ServicioException(e.getMessage());
		}
		return incidencia;
	}

	// Asigna una incidencia a un operario, devuelve la incidencia
	@Override
	public Incidencia asignarIncidencia(String idBici, String idIncidencia, String operario) throws ServicioException {
		// Recupera la bicicleta del repositorio
		Bicicleta bici;
		try {
			bici = repositorioBicicletas.getById(idBici);

			// Se asigna la incidencia, indicando el operario
			Incidencia incidencia = bici.cambiarEstadoIncidencia(idIncidencia, EstadoIncidencia.ASIGNADA, operario);
			// Se actualiza la bicicleta
			repositorioBicicletas.update(bici);
			// Se retira la bicicleta de la estación en la que está usando
			// servicioEstacion
			servicioEstacion.retirarBicicleta(idBici);
			return incidencia;
		} catch (EntidadNoEncontrada | RepositorioException e) {
			throw new ServicioException(e.getMessage());
		}
	}

	// Resuelve una incidencia que ha sido reparada, devuelve la incidencia
	@Override
	public Incidencia resolverIncidenciaReparada(String idBici, String idIncidencia, String motivo)
			throws ServicioException {
		try {
			// Recupera la bicicleta del repositorio
			Bicicleta bici = repositorioBicicletas.getById(idBici);
			// Se resuelve la incidencia, indicando el motivo
			Incidencia incidencia = bici.cambiarEstadoIncidencia(idIncidencia, EstadoIncidencia.RESUELTA, motivo);
			// Se actualiza la bicicleta y se estaciona en una estación disponible, usando
			// servicioEstacion
			repositorioBicicletas.update(bici);
			servicioEstacion.estacionarBicicleta(idBici);
			return incidencia;
		} catch (RepositorioException | EntidadNoEncontrada | ServicioException e) {
			throw new ServicioException(e.getMessage());
		}
	}

	// Resuelve una incidencia que no se ha podido reparar, devuelve la incidencia
	@Override
	public Incidencia resolverIncidenciaNoReparable(String idBici, String idIncidencia, String motivo)
			throws ServicioException {
		try {
			// Recupera la bicicleta del repositorio
			Bicicleta bici = repositorioBicicletas.getById(idBici);
			// Se resuelve la incidencia, indicando el motivo
			Incidencia incidencia = bici.cambiarEstadoIncidencia(idIncidencia, EstadoIncidencia.RESUELTA, motivo);
			// Se actualiza la bicicleta
			repositorioBicicletas.update(bici);
			// Se da de baja la bicicleta usando el servicioEstacion
			servicioEstacion.bajaBicicleta(idBici, "Incidencia no reparable");
			return incidencia;
		} catch (RepositorioException | EntidadNoEncontrada e) {
			throw new ServicioException(e.getMessage());
		}
	}

	private IncidenciaDTO transformToDTO(Incidencia incidencia) {
		return new IncidenciaDTO(incidencia.getId(), incidencia.getDescripcion(), incidencia.getFechaCreacion(),
				incidencia.getFechaCierre(), incidencia.getMotivoCierre(), incidencia.getOperario(),
				incidencia.getEstado(), incidencia.getBicicleta().getId());

	}

}
