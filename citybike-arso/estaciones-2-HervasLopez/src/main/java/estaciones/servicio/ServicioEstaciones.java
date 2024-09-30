package estaciones.servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import estaciones.dto.BicicletaDTO;
import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.repositorio.RepositorioBicicletasAdHoc;
import estaciones.repositorio.RepositorioEstacionesAdHoc;
import repositorio.EntidadNoEncontrada;
import servicio.ServicioException;

@Service
public class ServicioEstaciones implements IServicioEstaciones {

	// Repositorio Estaciones
	private RepositorioEstacionesAdHoc repositorioEstaciones;

	// Repositorio Bicicletas
	private RepositorioBicicletasAdHoc repositorioBicicletas;

	// Servicio Eventos
	private IServicioEventos servicioEventos;

	@Autowired
	public ServicioEstaciones(RepositorioEstacionesAdHoc repositorioEstaciones,
			RepositorioBicicletasAdHoc repositorioBicicletas, IServicioEventos servicioEventos) {
		this.repositorioEstaciones = repositorioEstaciones;
		this.repositorioBicicletas = repositorioBicicletas;
		// this.publicadorEventos = publicadorEventos;
		this.servicioEventos = servicioEventos;
	}

	////////////////////////////////
	// Funcionalidad para el gestor//
	////////////////////////////////

	/* Alta de una estación de aparcamiento, retorna el identificador asignado */
	@Override
	public String altaEstacion(String nombre, int numPuestos, String direccion, String codigoPostal) {

		// Control de integridad de los datos
		if (nombre == null || nombre.isEmpty())
			throw new IllegalArgumentException("nombre: no debe ser nulo ni vacio");

		if (numPuestos == 0)
			throw new IllegalArgumentException("numero de puestos: no debe ser 0");

		if (direccion == null || direccion.isEmpty())
			throw new IllegalArgumentException("direccion: no debe ser nula ni vacia");

		if (codigoPostal == null || codigoPostal.isEmpty())
			throw new IllegalArgumentException("codigoPostal: no debe ser nulo");

		// Crea la nueva Estacion y la añade al repositorio de estaciones
		Estacion nueva = new Estacion(nombre, numPuestos, direccion, codigoPostal);
		return repositorioEstaciones.save(nueva).getId();
	}

	/*
	 * Da de alta una nueva bicicleta en la Estacion proporcionada y retorna el id
	 * de la bici creada
	 */
	public String altaBicicleta(String modelo, String idEstacion) throws EntidadNoEncontrada, ServicioException {
		// Control de integridad de los datos
		if (modelo == null || modelo.isEmpty())
			throw new IllegalArgumentException("MODELO: no debe ser nulo ni vacio");
		if (idEstacion == null || idEstacion.isEmpty())
			throw new IllegalArgumentException("IDESTACION: no debe ser nulo");

		// Recupera la Estacion proporcionada del repositorio
		Estacion estacion;
		Optional<Estacion> estacionOptional = repositorioEstaciones.findById(idEstacion);
		if (!estacionOptional.isPresent())
			throw new EntidadNoEncontrada("No existe la estacion con id: " + idEstacion);
		estacion = estacionOptional.get();
		// Comprueba si hay un puesto libre en la Estacion
		if (estacion.hayPuestoLibre()) {
			// Si lo hay, crea la bicicleta y la añade al repositorio y a la Estacion
			Bicicleta bicicleta = new Bicicleta(modelo);
			bicicleta.setIdEstacionActual(estacion.getId());
			bicicleta = repositorioBicicletas.save(bicicleta);
			estacion.addBicicleta(bicicleta);
			// Actualiza el repositorio
			repositorioEstaciones.save(estacion);
			return bicicleta.getId();
		} else throw new ServicioException("No hay hueco en la estación " + idEstacion);

	}

	/*
	 * Da de baja la Bicicleta con idBici. Esta bici se considera no disponible y ya
	 * no ocupa sitio en ninguna estación.
	 */
	public void bajaBicicleta(String idBici, String motivo) throws EntidadNoEncontrada {
		// Control de integridad de los datos
		if (idBici == null || idBici.isEmpty())
			throw new IllegalArgumentException("idBici: no debe ser nulo ni vacio");
		if (motivo == null || motivo.isEmpty())
			throw new IllegalArgumentException("motivo: no debe ser nulo ni vacio");
		// Recupera la Bicicleta y la retira con retirarBicicleta
		Bicicleta bicicleta;
		Optional<Bicicleta> biciOptional = repositorioBicicletas.findById(idBici);
		if (!biciOptional.isPresent())
			throw new EntidadNoEncontrada("No existe la bicicleta con id: " + idBici);

		bicicleta = biciOptional.get();

		this.retirarBicicleta(bicicleta);
		// Da de baja la Bicicleta (pasa a no disponible)
		bicicleta.darBaja(motivo);
		repositorioBicicletas.save(bicicleta);

		// Evento bicicleta-desactivada
		servicioEventos.bajaBicicleta(idBici);
	}

	/* Retira la Bicicleta con idBici de la Estacion en la que se encuentra */
	private void retirarBicicleta(Bicicleta bicicleta) throws EntidadNoEncontrada {
		Estacion estacion;

		if (bicicleta.getIdEstacionActual() != null) {
			// Recupera la estacion
			String idEstacion = bicicleta.getIdEstacionActual();
			Optional<Estacion> estacionOptional = repositorioEstaciones.findById(idEstacion);
			if (!estacionOptional.isPresent())
				throw new EntidadNoEncontrada("No existe la estacion con id: " + idEstacion);
			estacion = estacionOptional.get();

			// Retira la Bici de la Estacion
			estacion.retirarBici(bicicleta);
			bicicleta.setIdEstacionActual(null);
			// Actualiza los repositorios
			repositorioBicicletas.save(bicicleta);
			repositorioEstaciones.save(estacion);
		}
	}

	/*
	 * Obtener un listado con todas las bicicletas de una estación. Devuelve las
	 * bicicletas que están estacionadas en la estación.
	 */

	@Override
	public Page<BicicletaDTO> getBicicletasListadoPaginado(Pageable pageable, String idEstacion) {

		if (idEstacion == null || idEstacion.isEmpty())
			throw new IllegalArgumentException("IDESTACION: no debe ser nulo");

		return this.repositorioBicicletas.findByidEstacionActual(pageable, idEstacion).map((bicicleta) -> {
			BicicletaDTO bici = transformToDTO(bicicleta);
			return bici;
		});
	}

	//////////////////////////////////
	// Funcionalidad para el usuario//
	//////////////////////////////////

	/*
	 * Recuperar la información de todas las estaciones (sin bicicletas), con
	 * listado paginado
	 */
	public Page<EstacionResumen> getEstacionesListadoPaginado(Pageable pageable) {
		return this.repositorioEstaciones.findAll(pageable).map((estacion) -> {
			EstacionResumen resumen = transformtoDTO(estacion);
			return resumen;
		});

	}

	// DAWEB
	@Override
	public List<EstacionResumen> getEstacionesSinPaginar() {
		List<EstacionResumen> resumenes = new ArrayList<EstacionResumen>();

		for (Estacion e : this.repositorioEstaciones.findAll()) {
			EstacionResumen resumen = transformtoDTO(e);
			resumenes.add(resumen);
		}

		return resumenes;

	}

	@Override
	public EstacionResumen editarEstacion(String id, String nombre, int numPuestos, String direccion)
			throws EntidadNoEncontrada {

		// Control de integridad de los datos
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id: no debe ser nulo ni vacio");

		Estacion estacion;

		Optional<Estacion> estacionOptional = repositorioEstaciones.findById(id);
		if (!estacionOptional.isPresent())
			throw new EntidadNoEncontrada("No existe la estacion con id: " + id);
		estacion = estacionOptional.get();

		estacion.setNombre(nombre);
		estacion.setDireccion(direccion);
		estacion.setNumPuestos(numPuestos);
		repositorioEstaciones.save(estacion);
		return transformtoDTO(estacion);
	}

	@Override
	public void eliminarEstacion(String id) throws EntidadNoEncontrada, ServicioException {
		// Control de integridad de los datos
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id: no debe ser nulo ni vacio");

		Estacion estacion;

		Optional<Estacion> estacionOptional = repositorioEstaciones.findById(id);
		if (!estacionOptional.isPresent())
			throw new EntidadNoEncontrada("No existe la estacion con id: " + id);
		estacion = estacionOptional.get();

		if (estacion.getBicicletas().size() > 0)
			throw new ServicioException(
					"No se puede borrar la estacion con id: " + id + " porque hay bicis estacionadas");

		repositorioEstaciones.delete(estacion);

	}
	
	@Override
	public List<BicicletaDTO> getBicicletasByEstacion(String idEstacion) {
		
		if (idEstacion == null || idEstacion.isEmpty())
			throw new IllegalArgumentException("idEstacion: no debe ser nulo");
		
		List<Bicicleta> bicisEntity = repositorioBicicletas.findByidEstacionActual(idEstacion);
		List<BicicletaDTO> bicis = new ArrayList<BicicletaDTO>();
		
		for (Bicicleta b : bicisEntity) {
			BicicletaDTO bici = transformToDTO(b);
			bicis.add(bici);
		}

		return bicis;

	}
	
	
	
	

	/*
	 * Recuperar la información de una estación con idEstacion (sin bicicletas)
	 */
	public EstacionResumen getInfoEstacion(String idEstacion) throws EntidadNoEncontrada {
		Estacion estacion = this.getEstacion(idEstacion);
		EstacionResumen resumen = transformtoDTO(estacion);
		return resumen;
	}

	/*
	 * Devuelve las bicicletas que están estacionadas y disponibles en la Estacion
	 * con idEstacion, listado paginado
	 */

	@Override
	public Page<BicicletaDTO> getBicicletasDisponiblesListadoPaginado(Pageable pageable, String idEstacion) {

		if (idEstacion == null || idEstacion.isEmpty())
			throw new IllegalArgumentException("IDESTACION: no debe ser nulo");

		return this.repositorioBicicletas.findByidEstacionActualAndDisponibleIsTrue(pageable, idEstacion)
				.map((bicicleta) -> {
					BicicletaDTO bici = transformToDTO(bicicleta);
					return bici;
				});
	}
	
	
	//DAWEB
	@Override
	public List<BicicletaDTO> getBicicletasDisponibles(String idEstacion) {
		if (idEstacion == null || idEstacion.isEmpty())
			throw new IllegalArgumentException("IDESTACION: no debe ser nulo");
		
		List<BicicletaDTO> bicicletas = new ArrayList<BicicletaDTO>();
		
		for(Bicicleta b : this.repositorioBicicletas.findByidEstacionActualAndDisponibleIsTrue(idEstacion)) {
			BicicletaDTO dto = transformToDTO(b);
			bicicletas.add(dto);
		}

		return bicicletas;
	}
	
	

	/*
	 * Estaciona una Bicicleta con idBici en la Estacion con idEstacion
	 */
	public void estacionarBicicleta(String idBici, String idEstacion) throws EntidadNoEncontrada, ServicioException {
		// Control de integridad de los datos
		if (idBici == null || idBici.isEmpty())
			throw new IllegalArgumentException("idBici: no debe ser nulo ni vacio");
		if (idEstacion == null || idEstacion.isEmpty())
			throw new IllegalArgumentException("idEstacion: no debe ser nulo ni vacio");

		boolean anadida;
		Estacion estacion = this.getEstacion(idEstacion);
		Bicicleta bicicleta = this.getBicicleta(idBici);

		// Añade la bicicleta a la Estacion
		anadida = estacion.addBicicleta(bicicleta);
		if (anadida) {
			// Si se ha añadido correctamente, actualiza la Estacion
			repositorioEstaciones.save(estacion);
			bicicleta.setIdEstacionActual(idEstacion);
			// Actualiza la Bicicleta en el repositorio
			repositorioBicicletas.save(bicicleta);
		} else {
			throw new ServicioException("No se ha podido añadir la bicicleta: " + idBici);
		}

	}

	// Obtener una estación a partir de su identificador, retorna la Estacion
	@Override
	public Estacion getEstacion(String idEstacion) throws EntidadNoEncontrada {
		// Control de integridad de los datos
		if (idEstacion == null || idEstacion.isEmpty())
			throw new IllegalArgumentException("idEstacion: no debe ser nulo ni vacio");
		Optional<Estacion> estacion = repositorioEstaciones.findById(idEstacion);
		if (!estacion.isPresent())
			throw new EntidadNoEncontrada("No existe la estacion con id: " + idEstacion);
		return estacion.get();
	}

	// Obtener una bicicleta a partir de su identificador, retorna la bicicleta
	@Override
	public Bicicleta getBicicleta(String id) throws EntidadNoEncontrada {
		// Control de integridad de los datos
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id: no debe ser nulo ni vacio");

		Optional<Bicicleta> bicicleta = repositorioBicicletas.findById(id);
		if (!bicicleta.isPresent())
			throw new EntidadNoEncontrada("No existe la bicicleta con id: " + id);

		return bicicleta.get();

	}

	/*
	 * Gestiona los eventos de "bicicleta-alquilada" y
	 * "bicicleta-alquiler-concluido" cambiando la disponibilidad de la bicicleta
	 */
	@Override
	public void setDisponibilidad(String idBici, boolean bool) throws EntidadNoEncontrada {
		Bicicleta bici = this.getBicicleta(idBici);
		bici.setDisponible(bool);
		repositorioBicicletas.save(bici);
	}

	// Transforma la bicicleta a BicicletaDTO
	public BicicletaDTO transformToDTO(Bicicleta bicicleta) {
		BicicletaDTO bdto = new BicicletaDTO(bicicleta.getId(), bicicleta.getModelo(), bicicleta.isDisponible(),
				bicicleta.getIdEstacionActual());
		return bdto;
	}

	// Transforma la Estacion en el dto EstacionResumen
	private EstacionResumen transformtoDTO(Estacion estacion) {
		EstacionResumen edto = new EstacionResumen();
		edto.setId(estacion.getId());
		edto.setDireccion(estacion.getDireccion());
		edto.setNombre(estacion.getNombre());
		edto.setHuecosLibres(estacion.hayPuestoLibre());
		edto.setPuestosDisponibles(estacion.getHuecosLibres());
		edto.setFechaAlta(estacion.getFechaAlta().toString());
		edto.setNumPuestos(estacion.getNumPuestos());
		edto.setCodigoPostal(estacion.getCodigoPostal());
		return edto;
	}
}
