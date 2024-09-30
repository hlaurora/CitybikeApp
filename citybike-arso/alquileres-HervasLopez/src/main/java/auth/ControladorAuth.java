package auth;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import alquileres.servicio.IServicioUsuarios;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import servicio.FactoriaServicios;

@Path("auth")
public class ControladorAuth {

	// curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/x-www-form-urlencoded" -d "username=usuario1&password=clave"
	// curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/x-www-form-urlencoded" -d "username=gestor1&password=clave"
	IServicioUsuarios servicioUsuarios = FactoriaServicios.getServicio(IServicioUsuarios.class);
	
	@POST
	@Path("/login")
	@PermitAll
	public Response login(@FormParam("username") String username, @FormParam("password") String password){
		Map<String, Object> claims = verificarCredenciales(username, password);
		if (claims != null) {
			Date caducidad = Date.from(
					Instant.now()
					.plusSeconds(3600));
			String token = Jwts.builder()
					.setClaims(claims)
					.signWith(SignatureAlgorithm.HS256, "secreto")
					.setExpiration(caducidad)
					.compact();
			return Response.ok(token).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciales inválidas").build();
		}

	}

	private Map<String, Object> verificarCredenciales(String username, String password) {
		return servicioUsuarios.verificarCredenciales(username, password);
	}

}
