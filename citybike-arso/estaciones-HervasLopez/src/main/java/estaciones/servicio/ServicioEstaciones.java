package estaciones.servicio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import estaciones.dto.BicicletaDTO;
import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.modelo.Historico;
import estaciones.modelo.SitioTuristico;
import estaciones.repositorio.RepositorioBicicletaJPA;
import estaciones.repositorio.RepositorioEstacionesAdHoc;
import estaciones.repositorio.RepositorioHistoricoAdHoc;
import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;
import servicio.ServicioException;

public class ServicioEstaciones implements IServicioEstaciones {

	// Repositorio Estaciones
	private RepositorioEstacionesAdHoc repositorioEstaciones = FactoriaRepositorios.getRepositorio(Estacion.class);
	// Repositorio Bicicletas
	private RepositorioBicicletaJPA repositorioBicicletas = FactoriaRepositorios.getRepositorio(Bicicleta.class);
	// Repositorio Historico
	private RepositorioHistoricoAdHoc repositorioHistorico = FactoriaRepositorios.getRepositorio(Historico.class);
	// Servicio SitiosTuristicos
	ISitiosTuristicos servicioSitios = FactoriaServicios.getServicio(ISitiosTuristicos.class);

	// Alta de una estación de aparcamiento, retorna el identificador asignado
	@Override
	public String altaEstacion(String nombre, int numPuestos, String direccion, double lat, double lng)
			throws ServicioException {

		// Control de integridad de los datos
		if (nombre == null || nombre.isEmpty())
			throw new IllegalArgumentException("nombre: no debe ser nulo ni vacio");

		if (numPuestos == 0)
			throw new IllegalArgumentException("numero de puestos: no debe ser 0");

		if (direccion == null || direccion.isEmpty())
			throw new IllegalArgumentException("direccion: no debe ser nula ni vacia");

		if (Double.isNaN(lat))
			throw new IllegalArgumentException("lat: no debe ser nula");

		if (Double.isNaN(lng))
			throw new IllegalArgumentException("lng: no debe ser nula");

		// Crea la nueva Estacion y la añade al repositorio de estaciones
		Estacion nueva = new Estacion(nombre, numPuestos, direccion, lat, lng);
		String id;
		try {
			id = repositorioEstaciones.add(nueva);
		} catch (RepositorioException e) {
			throw new ServicioException(e.getMessage());
		}
		return id;
	}

	// Obtener una estación a partir de su identificador, retorna la Estacion
	@Override
	public Estacion getEstacion(String idEstacion) throws ServicioException {
		// Control de integridad de los datos
		if (idEstacion == null || idEstacion.isEmpty())
			throw new IllegalArgumentException("idEstacion: no debe ser nulo ni vacio");
		try {
			return repositorioEstaciones.getById(idEstacion);
		} catch (RepositorioException | EntidadNoEncontrada e) {
			throw new ServicioException(e.getMessage());
		}
	}

	// Obtener una bicicleta a partir de su identificador, retorna la bicicleta
	@Override
	public Bicicleta getBicicleta(String id) throws ServicioException {
		// Control de integridad de los datos
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id: no debe ser nulo ni vacio");
		try {
			return repositorioBicicletas.getById(id);
		} catch (EntidadNoEncontrada | RepositorioException e) {
			throw new ServicioException(e.getMessage());
		}
	}

	// Obtiene una lista con información de sitios turísticos cercanos a la estación
	// con idEstacion, retorna dicha lista
	@Override
	public List<SitioTuristicoResumen> getSitiosTuristicos(String idEstacion) throws ServicioException {
		// Control de integridad de los datos
		if (idEstacion == null || idEstacion.isEmpty())
			throw new IllegalArgumentException("idEstacion: no debe ser nulo ni vacio");
		// Obtiene la estación del repositorio
		Estacion estacion;
		try {
			estacion = repositorioEstaciones.getById(idEstacion);
			List<SitioTuristicoResumen> sitios = new ArrayList<SitioTuristicoResumen>();
			// Obtiene la lista de sitios de interés cercanos a la estación mediante sus
			// coordenadas, llamando al servicio de sitios turisticos
			sitios = servicioSitios.getSitioInteres(estacion.getlLat(), estacion.getlLng());
			return sitios;
		} catch (RepositorioException | EntidadNoEncontrada e) {
			throw new ServicioException(e.getMessage());
		}

	}

	// Asocia la colección de sitios turísticos porporcionada a la estación con
	// idEstacion
	@Override
	public void establecerSitiosTuristicos(String idEstacion, List<SitioTuristico> sitiosTuristicos)
			throws ServicioException {
		// Control de integridad de los datos
		if (idEstacion == null || idEstacion.isEmpty())
			throw new IllegalArgumentException("idEstacion: no debe ser nulo ni vacio");
		if (sitiosTuristicos.isEmpty() || sitiosTuristicos == null)
			throw new IllegalArgumentException("sitios: no debe ser nulo ni vacio");

		// Obtiene la estacion del repositorio
		Estacion estacion;
		try {
			estacion = repositorioEstaciones.getById(idEstacion);
			// Asocia el listado de sitios turisticos a la estación y actualiza su valor en
			// el repositorio
			estacion.setSitiosTuristicos(sitiosTuristicos);
			repositorioEstaciones.update(estacion);
		} catch (RepositorioException | EntidadNoEncontrada e) {
			throw new ServicioException(e.getMessage());
		}
	}

	// Da de alta una nueva bicicleta en la Estacion proporcionada
	public String altaBicicleta(String modelo, Estacion estacion) throws ServicioException {
		// Control de integridad de los datos
		if (modelo == null || modelo.isEmpty())
			throw new IllegalArgumentException("MODELO: no debe ser nulo ni vacio");
		if (estacion == null)
			throw new IllegalArgumentException("ESTACION: no debe ser nulo");

		// Recupera la Estacion proporcionada del repositorio
		Estacion estacionAnadir;
		try {
			estacionAnadir = repositorioEstaciones.getById(estacion.getId());
			// Comprueba si hay un puesto libre en la Estacion
			if (estacionAnadir.hayPuestoLibre()) {
				// Si lo hay, crea la bicicleta y la añade al repositorio y a la Estacion
				Bicicleta bicicleta = new Bicicleta(modelo);
				bicicleta.setIdEstacionActual(estacionAnadir.getId());
				String idBicicleta = repositorioBicicletas.add(bicicleta);
				estacionAnadir.addBicicleta(bicicleta);
				// Actualiza el repositorio
				repositorioEstaciones.update(estacionAnadir);
				// Crea un nuevo Historico para la Bicicleta y lo añade al repositorio
				Historico historico = new Historico(idBicicleta, estacionAnadir.getId());
				repositorioHistorico.add(historico);
				return idBicicleta;
			}
		} catch (RepositorioException | EntidadNoEncontrada e) {
			throw new ServicioException(e.getMessage());
		}
		System.out.println("No hay un sitio disponible en esa estación\n");
		return null;

	}

	// Estaciona una Bicicleta con idBici en la Estacion con idEstacion
	public void estacionarBicicleta(String idBici, String idEstacion) throws ServicioException {
		// Control de integridad de los datos
		if (idBici == null || idBici.isEmpty())
			throw new IllegalArgumentException("idBici: no debe ser nulo ni vacio");
		if (idEstacion == null || idEstacion.isEmpty())
			throw new IllegalArgumentException("idEstacion: no debe ser nulo ni vacio");
		Bicicleta bicicleta;
		boolean anadida;
		Estacion estacion;
		try {
			// Recupera la Bicicleta y la Estacion de los repositorios
			bicicleta = repositorioBicicletas.getById(idBici);
			estacion = repositorioEstaciones.getById(idEstacion);
			// Añade la bicicleta a la Estacion
			anadida = estacion.addBicicleta(bicicleta);
			if (anadida) {
				// Si se ha añadido correctamente, actualiza la Estacion
				repositorioEstaciones.update(estacion);
				bicicleta.setIdEstacionActual(idEstacion);
				// Actualiza la Bicicleta en el repositorio
				repositorioBicicletas.update(bicicleta);
				// Crea un nuevo Historico y lo añade al repositorio
				Historico historico = new Historico(idBici, idEstacion);
				repositorioHistorico.add(historico);
			} else
				System.out.println("No hay un sitio disponible en esa estación\n");
		} catch (EntidadNoEncontrada | RepositorioException e) {
			throw new ServicioException(e.getMessage());
		}
	}

	// Estaciona la Bicicleta con idBici en una Estacion con algún puesto libre
	public void estacionarBicicleta(String idBici) throws ServicioException {
		// Control de integridad de los datos
		if (idBici == null || idBici.isEmpty())
			throw new IllegalArgumentException("idBici: no debe ser nulo ni vacio");
		// Busca una Estacion libre en el Repositorio y estaciona la Bicicleta
		Estacion estacionLibre;
		try {
			estacionLibre = repositorioEstaciones.estacionPrimeraLibre();
		} catch (RepositorioException e) {
			throw new ServicioException(e.getMessage());
		}
		estacionarBicicleta(idBici, estacionLibre.getId());
	}

	// Retira la Bicicleta con idBici de la Estacion en la que se encuentra
	public void retirarBicicleta(String idBici) throws ServicioException {
		// Control de integridad de los datos
		if (idBici == null || idBici.isEmpty())
			throw new IllegalArgumentException("idBici: no debe ser nulo ni vacio");
		// Recupera la bicicleta
		Bicicleta bicicleta;
		try {
			bicicleta = repositorioBicicletas.getById(idBici);
			if (bicicleta.getIdEstacionActual() != null) {
				// Recupera la Estacion y el Historico
				Estacion estacion = repositorioEstaciones.getById(bicicleta.getIdEstacionActual());
				Historico historico = repositorioHistorico.getByBiciYEstacion(bicicleta, estacion);
				// Fija la fechaFin del Historico
				historico.setFechaFin(LocalDateTime.now());
				// Retira la Bici de la Estacion
				estacion.retirarBici(bicicleta);
				bicicleta.setIdEstacionActual(null);
				// Actualiza los repositorios
				repositorioBicicletas.update(bicicleta);
				repositorioEstaciones.update(estacion);
				repositorioHistorico.update(historico);
			}
		} catch (EntidadNoEncontrada | RepositorioException e) {
			throw new ServicioException(e.getMessage());
		}

	}

	// Da de baja la Bicicleta con idBici
	public void bajaBicicleta(String idBici, String motivo) throws ServicioException {
		// Control de integridad de los datos
		if (idBici == null || idBici.isEmpty())
			throw new IllegalArgumentException("idBici: no debe ser nulo ni vacio");
		if (motivo == null || motivo.isEmpty())
			throw new IllegalArgumentException("motivo: no debe ser nulo ni vacio");
		// Recupera la Bicicleta y la retira con retirarBicicleta
		Bicicleta bicicleta;
		try {
			bicicleta = repositorioBicicletas.getById(idBici);
		} catch (EntidadNoEncontrada | RepositorioException e) {
			throw new ServicioException(e.getMessage());
		}
		this.retirarBicicleta(idBici);
		// Da de baja la Bicicleta (pasa a no disponible)
		bicicleta.darBaja(motivo);
		// Actualiza la Bicicleta en el repositorio
		try {
			repositorioBicicletas.update(bicicleta);
		} catch (RepositorioException | EntidadNoEncontrada e) {
			throw new ServicioException(e.getMessage());
		}
	}

	// Recupera las Bicicletas estacionadas cerca de la posicion (lat, lng)
	@Override
	public List<Bicicleta> getBicicletasCercanas(double lat, double lng) throws ServicioException {
		// Recupera las Estaciones del repositorio cercanas a (lat, lng)
		List<Estacion> estacionesCercanas;
		List<Bicicleta> bicisCercanas;
		try {
			estacionesCercanas = repositorioEstaciones.getByCercania(lat, lng);
			bicisCercanas = new ArrayList<Bicicleta>();
			// Recorre las estaciones y añade a la lista sus bicis disponibles
			for (Estacion e : estacionesCercanas) {
				for (String id : e.getBicicletas()) {
					Bicicleta b = repositorioBicicletas.getById(id);
					if (b.isDisponible()) {
						bicisCercanas.add(b);
					}
				}
			}
		} catch (RepositorioException | EntidadNoEncontrada e) {
			throw new ServicioException(e.getMessage());
		}
		// Devuelve la lista de Bicicletas
		return bicisCercanas;
	}

	public List<BicicletaDTO> getBicicletasCercanasDTO(double lat, double lng) throws ServicioException {
		List<Bicicleta> bicisCercanas = getBicicletasCercanas(lat, lng);
		List<BicicletaDTO> bicisCercanasDTO = new ArrayList<BicicletaDTO>();
		for (Bicicleta b : bicisCercanas) {
			bicisCercanasDTO.add(transformToDTO(b));
		}
		return bicisCercanasDTO;
	}

	// Recupera las Estaciones ordenadas de mayor a menor número de SitiosTuristicos
	// cerca
	@Override
	public List<Estacion> getEstacionesTuristicas() throws ServicioException {
		// Recupera todas las Estaciones del repositorio
		try {
			return repositorioEstaciones.getByMayorSitiosTuristicos();
		} catch (RepositorioException e) {
			throw new ServicioException(e.getMessage());
		}
	}

	private BicicletaDTO transformToDTO(Bicicleta bicicleta) {
		BicicletaDTO bdto = new BicicletaDTO(bicicleta.getId(), bicicleta.getModelo(), bicicleta.isDisponible(),
				bicicleta.getIdEstacionActual());
		return bdto;
	}

}
