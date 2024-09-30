package estaciones.rest;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import estaciones.dto.BicicletaDTO;
import estaciones.dto.EditarEstacionDTO;
import estaciones.dto.NuevaBicicletaDTO;
import estaciones.dto.NuevaEstacionDTO;
import estaciones.modelo.Bicicleta;
import estaciones.servicio.EstacionResumen;
import estaciones.servicio.IServicioEstaciones;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import repositorio.EntidadNoEncontrada;
import servicio.ServicioException;

// Interfaz Swagger-UI: http://localhost:8070/swagger-ui.html

@RestController
@Tag(name = "Citybike - Estaciones", description = "Microservicio de estaciones de la aplicación Citybike")
public class EstacionesControladorRest {

	private IServicioEstaciones servicioEstaciones;

	@Autowired
	public EstacionesControladorRest(IServicioEstaciones servicioEstaciones) {
		this.servicioEstaciones = servicioEstaciones;
	}

	@Autowired
	private PagedResourcesAssembler<BicicletaDTO> pagedBicicletaAssembler;

	@Autowired
	private PagedResourcesAssembler<EstacionResumen> pagedEstacionesAssembler;

	/*
	 * Funcionalidad para el gestor
	 */

	// curl -i -X POST -H "Content-type: application/json" -d
	// "{\"nombre\":\"conRest\",\"numPuestos\":1, \"direccion\":\"calle mayor\",
	// \"lat\":50.0, \"lng\": 60.0}" http://localhost:8070/estaciones/ -H
	// "Authorization: Bearer token"

	@Operation(summary = "Dar de alta una estación", description = "Da de alta una estación con un nombre, número de puestos, dirección y coordenadas")
	@PostMapping("/estaciones")
	@PreAuthorize("hasAuthority('gestor')")
	// url para probar la operacion en Swagger ui
	// @PostMapping("/public/estaciones")
	public ResponseEntity<Void> altaEstacion(@Valid @RequestBody NuevaEstacionDTO nuevaEstacion) {

		String id = servicioEstaciones.altaEstacion(nuevaEstacion.getNombre(), nuevaEstacion.getNumPuestos(),
				nuevaEstacion.getDireccion(), nuevaEstacion.getCodigoPostal());

		URI nuevaURL = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(nuevaURL).build();
	}

	// curl -i -X POST -H "Content-type: application/json" -d
	// "{\"modelo\":\"paseo\",\"idEstacion\":\"66150413defc28686e5ac10c\"}"
	// http://localhost:8070/estaciones/bicicletas -H "Authorization: Bearer token"

	@Operation(summary = "Dar de alta una bicicleta", description = "Da de alta una bicicleta con un modelo y el id de la estación en la que está aparcada")
	@PostMapping("/estaciones/bicicletas")
	@PreAuthorize("hasAuthority('gestor')")
	// url para probar la operacion en Swagger ui
	// @PostMapping("/public/estaciones/bicicletas")
	public ResponseEntity<Void> altaBicicleta(@Valid @RequestBody NuevaBicicletaDTO nuevaBicicleta)
			throws EntidadNoEncontrada, ServicioException {

		String id;
		id = servicioEstaciones.altaBicicleta(nuevaBicicleta.getModelo(), nuevaBicicleta.getIdEstacion());

		URI nuevaURL = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(nuevaURL).build();
	}

	// curl -i -X PATCH -H "Content-type: application/json" -d "{\"motivo\":\"no
	// va\"}"
	// http://localhost:8070/estaciones/bicicletas/f07c50a6-21c8-40a7-bf78-8df610a9c61f
	// -H "Authorization: Bearer "

	@Operation(summary = "Dar de baja una bicicleta", description = "Da de baja una bicicleta con un motivo y su id")
	@PatchMapping("/estaciones/bicicletas/{id}")
	@PreAuthorize("hasAuthority('gestor')")
	// url para probar la operacion en Swagger ui
	// @PatchMapping("/public/estaciones/bicicletas/{id}")
	public ResponseEntity<Void> bajaBicicleta(@RequestBody String motivo, @PathVariable String id)
			throws EntidadNoEncontrada {

		try {
			servicioEstaciones.bajaBicicleta(id, motivo);
		} catch (EntidadNoEncontrada e) {
			throw new EntidadNoEncontrada(e.getMessage());
		}
		URI nuevaURL = ServletUriComponentsBuilder.fromCurrentRequest().path("").buildAndExpand(id).toUri();

		return ResponseEntity.created(nuevaURL).build();

	}

	// curl -i -H "Accept: application/json"
	// "http://localhost:8070/estaciones/6614fa259cf67938487f7a0c/bicicletas?page=0&size=1"
	// -H "Authorization: Bearer token"
	@Operation(summary = "Obtener bicicletas", description = "Obtiene todas las bicicletas aparcadas en una estación")
	@GetMapping("/estaciones/{id}/bicicletasPaged/")
	@PreAuthorize("hasAuthority('gestor')")
	// url para probar la operacion en Swagger ui
	// @GetMapping("/public/estaciones/{id}/bicicletas")
	public PagedModel<EntityModel<BicicletaDTO>> getBicicletas(@RequestParam int page, @RequestParam int size,
			@PathVariable String id) {
		Pageable paginacion = PageRequest.of(page, size, Sort.by("modelo").ascending());
		Page<BicicletaDTO> resultado1 = this.servicioEstaciones.getBicicletasListadoPaginado(paginacion, id);

		return this.pagedBicicletaAssembler.toModel(resultado1, bicicleta -> {
			EntityModel<BicicletaDTO> model = EntityModel.of(bicicleta);
			try {
				model.add(WebMvcLinkBuilder.linkTo(
						WebMvcLinkBuilder.methodOn(EstacionesControladorRest.class).getBicicletaById(bicicleta.getId()))
						.withSelfRel());

				String urlBajaBicicleta = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
						.methodOn(EstacionesControladorRest.class).bajaBicicleta("motivo", bicicleta.getId())).toUri()
						.toString();
				model.add(Link.of(urlBajaBicicleta, "Dar de baja"));

			} catch (Exception e) {
				e.printStackTrace();
			}

			return model;
		});
	}

	/*
	 * Funcionalidad para el usuario
	 */

	// curl -i -X PATCH -H "Content-type: application/json"
	// http://localhost:8070/estaciones/66150413defc28686e5ac10c/bicicleta/2ce44d3a-0315-44eb-b6f5-b43291360c23

	@Operation(summary = "Estacionar una bicicleta", description = "Estaciona una bicicleta con su id y el id de la estación")
	@PatchMapping("/estaciones/aparcadas/{idEstacion}/bicicleta/{idBicicleta}")
	// Es pública para poder llamarla desde el microservicio Alquileres
	// @PreAuthorize("hasAuthority('usuario')")
	public ResponseEntity<Void> estacionarBicicleta(@PathVariable String idEstacion, @PathVariable String idBicicleta)
			throws EntidadNoEncontrada, ServicioException {
		try {
			servicioEstaciones.estacionarBicicleta(idBicicleta, idEstacion);
		} catch (EntidadNoEncontrada e) {
			throw new EntidadNoEncontrada(e.getMessage());
		} catch (ServicioException e) {
			throw new ServicioException(e.getMessage());
		}
		URI nuevaURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("estaciones/bicicletas/{idBicicleta}")
				.buildAndExpand(idBicicleta).toUri();

		return ResponseEntity.created(nuevaURL).build();

	}

	// curl -i -H "Accept: application/json"
	// "http://localhost:8070/estaciones?page=0&size=3" -H "Authorization: Bearer
	// token"

	@Operation(summary = "Obtener estaciones", description = "Obtiene todas las estaciones que hay, incluyendo información de estas como si tienen huecos libres")
	@GetMapping("/estacionesPaged")
	@PreAuthorize("hasAuthority('usuario')")
	// url para probar la operacion en Swagger ui
	// @GetMapping("/public/estaciones")
	public PagedModel<EntityModel<EstacionResumen>> getEstacionesPaginado(@RequestParam int page,
			@RequestParam int size) {
		Pageable paginacion = PageRequest.of(page, size, Sort.by("nombre").ascending());
		Page<EstacionResumen> resultado1 = this.servicioEstaciones.getEstacionesListadoPaginado(paginacion);

		return this.pagedEstacionesAssembler.toModel(resultado1, estacion -> {
			EntityModel<EstacionResumen> model = EntityModel.of(estacion);
			try {
				model.add(WebMvcLinkBuilder.linkTo(
						WebMvcLinkBuilder.methodOn(EstacionesControladorRest.class).getInfoEstacion(estacion.getId()))
						.withSelfRel());

			} catch (Exception e) {
				e.printStackTrace();
			}

			return model;
		});
	}

	@Operation(summary = "Obtener estaciones", description = "Obtiene todas las estaciones que hay, incluyendo información de estas como si tienen huecos libres")
	@GetMapping("/estaciones")
	@PreAuthorize("hasAnyAuthority('gestor','usuario')")
	public ResponseEntity<List<EstacionResumen>> getEstaciones() {

		List<EstacionResumen> estaciones = servicioEstaciones.getEstacionesSinPaginar();

		return ResponseEntity.ok(estaciones);
	}

	@PutMapping("/estaciones/{idEstacion}")
	@PreAuthorize("hasAuthority('gestor')")
	public ResponseEntity<EstacionResumen> editarEstacion(@PathVariable String idEstacion,
			@Valid @RequestBody EditarEstacionDTO nuevaEstacion) {
		EstacionResumen est = null;
		try {
			est = servicioEstaciones.editarEstacion(idEstacion, nuevaEstacion.getNombre(),
					nuevaEstacion.getNumPuestos(), nuevaEstacion.getDireccion());
		} catch (EntidadNoEncontrada e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(est);
	}

	@DeleteMapping("/estaciones/{idEstacion}")
	@PreAuthorize("hasAuthority('gestor')")
	public ResponseEntity<Void> eliminarEstacion(@PathVariable String idEstacion) {
		try {
			servicioEstaciones.eliminarEstacion(idEstacion);
		} catch (EntidadNoEncontrada e) {
			e.printStackTrace();
		} catch (ServicioException e) {
			return ResponseEntity.internalServerError().build();
		}

		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Obtener bicis", description = "Obtiene todas las bicis que hay en la estacion indicada")
	@GetMapping("/estaciones/{idEstacion}/bicicletas")
	@PreAuthorize("hasAuthority('gestor')")
	public ResponseEntity<List<BicicletaDTO>> getBicicletas(@PathVariable String idEstacion) {

		List<BicicletaDTO> bicis = servicioEstaciones.getBicicletasByEstacion(idEstacion);

		return ResponseEntity.ok(bicis);
	}

	// curl -i -H "Accept: application/json"
	// http://localhost:8070/estaciones/6614fa259cf67938487f7a0c
	@Operation(summary = "Obtener información de una estación", description = "Obtiene información de una estación con su id")
	@GetMapping("/estaciones/info/{id}")
	// Es pública para poder llamarla desde el microservicio Alquileres
	// @PreAuthorize("hasAuthority('usuario')")
	public EstacionResumen getInfoEstacion(@PathVariable String id) throws EntidadNoEncontrada {
		EstacionResumen estacion;
		try {
			estacion = servicioEstaciones.getInfoEstacion(id);
		} catch (EntidadNoEncontrada e) {
			throw new EntidadNoEncontrada(e.getMessage());
		}
		return estacion;
	}

	// curl -i -H "Accept: application/json"
	// "http://localhost:8070/estaciones/6614fa259cf67938487f7a0c/bicicletas?page=0&size=1"
	// -H "Authorization: Bearer token"
	@Operation(summary = "Obtener bicicletas disponibles", description = "Obtiene las bicicletas de una estación que están disponibles, con el id de la estación")
	@GetMapping("/estaciones/{id}/bicicletasPaged/disponibles")
	@PreAuthorize("hasAuthority('usuario')")
	// url para probar la operacion en Swagger ui
	// @GetMapping("/public/estaciones/{id}/bicicletas/disponibles")
	public PagedModel<EntityModel<BicicletaDTO>> getBicicletasDisponiblesPaginado(@RequestParam int page,
			@RequestParam int size, @PathVariable String id) {
		Pageable paginacion = PageRequest.of(page, size, Sort.by("modelo").ascending());
		Page<BicicletaDTO> resultado1 = this.servicioEstaciones.getBicicletasDisponiblesListadoPaginado(paginacion, id);

		return this.pagedBicicletaAssembler.toModel(resultado1, bicicleta -> {
			EntityModel<BicicletaDTO> model = EntityModel.of(bicicleta);
			try {
				model.add(WebMvcLinkBuilder.linkTo(
						WebMvcLinkBuilder.methodOn(EstacionesControladorRest.class).getBicicletaById(bicicleta.getId()))
						.withSelfRel());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return model;
		});
	}
	
	
	@Operation(summary = "Obtener bicis", description = "Obtiene todas las bicis disponibles que hay en la estacion indicada")
	@GetMapping("/estaciones/{idEstacion}/bicicletas/disponibles")
	@PreAuthorize("hasAuthority('usuario')")
	public ResponseEntity<List<BicicletaDTO>> getBicicletasDisponibles(@PathVariable String idEstacion) {

		List<BicicletaDTO> bicis = servicioEstaciones.getBicicletasDisponibles(idEstacion);

		return ResponseEntity.ok(bicis);
	}
	
	
	

	// curl -i -H "Accept: application/json"
	// http://localhost:8070/estaciones/bicicletas/33f3bb7a-cb78-4e10-ba53-6ec5e1e0715b
	// -H "Authorization: Bearer token"
	@Operation(summary = "Obtener bicicleta", description = "Obtiene una bicicleta por su id")
	@GetMapping("/estaciones/bicicletas/{id}")
	public EntityModel<BicicletaDTO> getBicicletaById(@PathVariable String id) throws EntidadNoEncontrada {
		Bicicleta bicicleta;
		try {
			bicicleta = servicioEstaciones.getBicicleta(id);
		} catch (EntidadNoEncontrada e) {
			throw new EntidadNoEncontrada(e.getMessage());
		}

		BicicletaDTO bicicletaDTO = servicioEstaciones.transformToDTO(bicicleta);
		EntityModel<BicicletaDTO> model = EntityModel.of(bicicletaDTO);
		model.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EstacionesControladorRest.class).getBicicletaById(id))
				.withSelfRel());

		return model;
	}

}
