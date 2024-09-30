package alquileres.rest;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import alquileres.modelo.Usuario;
import alquileres.servicio.IServicioAlquileres;
import alquileres.servicio.IServicioTiempo;
import io.jsonwebtoken.Claims;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;
import servicio.ServicioException;

@Path("alquileres")
public class AlquileresControladorRest {
	
	@Context
    private HttpServletRequest servletRequest;

	private IServicioAlquileres servicio = FactoriaServicios.getServicio(IServicioAlquileres.class);
	private IServicioTiempo tiempo = FactoriaServicios.getServicio(IServicioTiempo.class);
	
	// curl -i -X POST -H "Content-type: application/json" -d "{\"idUsuario\":\"U1\",\"idBicicleta\":\"bici1\"}" http://localhost:8080/api/alquileres
	// curl -i -X POST -H "Content-type: application/json" -d "{\"idUsuario\":\"usuario1\",\"idBicicleta\":\"bici1\"}" http://localhost:8080/api/alquileres -H "Authorization: Bearer token"

	@POST
	@RolesAllowed("usuario")
	public Response alquilar(AlquilerRequestDTO alquiler) throws ServicioException, EntidadNoEncontrada, RepositorioException  {
		servicio.alquilar(alquiler.getIdUsuario(), alquiler.getIdBicicleta());
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	// curl -i -X POST -H "Content-type: application/json" -d "{\"idUsuario\":\"U1\",\"idBicicleta\":\"bici1\"}" http://localhost:8080/api/alquileres/reservas
	// curl -i -X POST -H "Content-type: application/json" -d "{\"idUsuario\":\"usuario1\",\"idBicicleta\":\"bici1\"}" http://localhost:8080/api/alquileres/reservas  -H "Authorization: Bearer token"
	
	@POST
	@Path("/reservas")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("usuario")
	public Response reservar(AlquilerRequestDTO alquiler) throws ServicioException, EntidadNoEncontrada, RepositorioException {
		servicio.reservar(alquiler.getIdUsuario(), alquiler.getIdBicicleta());
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	// curl -i -X POST -H "Content-type: application/json" http://localhost:8080/api/alquileres/reservas/activa
	// curl -i -X POST -H "Content-type: application/json" http://localhost:8080/api/alquileres/reservas/activa -H "Authorization: Bearer token"		
	
	@POST
	@Path("/reservas/activa")
	@RolesAllowed("usuario")
	public Response confirmarReserva() throws ServicioException, EntidadNoEncontrada, RepositorioException  {
		if (this.servletRequest.getAttribute("claims") != null) {
            Claims claims = (Claims) this.servletRequest.getAttribute("claims");
            servicio.confirmarReserva(claims.getSubject());
        }
		
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@DELETE
	@Path("/reservas/activa")
	@RolesAllowed("usuario")
	public Response cancelarReserva() throws ServicioException, EntidadNoEncontrada, RepositorioException {
		if (this.servletRequest.getAttribute("claims") != null) {
            Claims claims = (Claims) this.servletRequest.getAttribute("claims");
            servicio.cancelarReserva(claims.getSubject());
        }
		return Response.status(Response.Status.NO_CONTENT).build();

	}
	
	
	@GET
	@Path("/activo")
	@RolesAllowed("usuario")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAlquilerActivo() throws RepositorioException {
		
		
		AlquilerDTO alquiler;
		try {
			String id="";
			if (this.servletRequest.getAttribute("claims") != null) {
			    Claims claims = (Claims) this.servletRequest.getAttribute("claims");
			    id = claims.getSubject();
			}
			
			alquiler = servicio.getAlquilerActivo(id);
		} catch (ServicioException e) {
			return Response.status(Response.Status.NO_CONTENT).build();

		}
		return Response.status(Response.Status.OK).entity(alquiler).build();
		
	}
	
	@GET
	@Path("reservas/activa")
	@RolesAllowed("usuario")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReservaActiva() throws RepositorioException {
		ReservaDTO reserva;
		try {
			String id="";
			if (this.servletRequest.getAttribute("claims") != null) {
			    Claims claims = (Claims) this.servletRequest.getAttribute("claims");
			    id = claims.getSubject();
			}
			
			reserva = servicio.getReservaActiva(id);
		} catch (ServicioException e) {
			return Response.status(Response.Status.NO_CONTENT).build();

		}
		
		return Response.status(Response.Status.OK).entity(reserva).build();

	}


	// curl -i -H "Accept: application/json" http://localhost:8080/api/alquileres/usuario
	// curl -i -H "Accept: application/json" http://localhost:8080/api/alquileres/usuario -H "Authorization: Bearer token"
	
	@GET
	@Path("/usuario")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("usuario")
	public Response historialUsuario() throws Exception 
	{
		String id="";
		if (this.servletRequest.getAttribute("claims") != null) {
            Claims claims = (Claims) this.servletRequest.getAttribute("claims");
            id = claims.getSubject();
        }
		Usuario usuario = servicio.historialUsuario(id);
		HistorialResponseDTO historial = new HistorialResponseDTO(usuario.getId(), usuario.bloqueado(tiempo.now()), usuario.tiempoUsoHoy(tiempo.now()), usuario.tiempoUsoSemana(tiempo.now()));
		historial.setReservas(usuario.getReservas());
		historial.setAlquileres(usuario.getAlquileres());
		return Response.status(Response.Status.OK).entity(historial).build();
	}
	
	// Ejecutar primero ProgramaServicioAlquileres
	// curl -i -X DELETE -H "Content-type: application/json" http://localhost:8080/api/alquileres/U3/reservas-caducadas
	// curl -i -X DELETE -H "Content-type: application/json" http://localhost:8080/api/alquileres/U3/reservas-caducadas -H "Authorization: Bearer token "

	@DELETE
	@Path("{id}/reservas-caducadas")
	//@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("gestor")
	public Response liberarBloqueo(@PathParam("id") String id) throws Exception {
		servicio.liberarBloqueo(id);
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	

	// curl -i -X PATCH -H "Content-type: application/x-www-form-urlencoded" -d "idEstacion=idEstacion1" http://localhost:8080/api/alquileres/activo
	// curl -i -X PATCH -H "Content-type: application/x-www-form-urlencoded" -d "idEstacion=idEstacion1" http://localhost:8080/api/alquileres/activo -H "Authorization: Bearer token"
			
	@PATCH
	@Path("/activo")
	@RolesAllowed("usuario")
	//@Consumes(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	public Response dejarBicicleta(@FormParam("idEstacion") String idEstacion) throws ServicioException, EntidadNoEncontrada, RepositorioException {
		String id="";
		if (this.servletRequest.getAttribute("claims") != null) {
            Claims claims = (Claims) this.servletRequest.getAttribute("claims");
            id = claims.getSubject();
        }
		servicio.dejarBicicleta(id, idEstacion);
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
}
