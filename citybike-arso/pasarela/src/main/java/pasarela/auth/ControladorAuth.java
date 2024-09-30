package pasarela.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pasarela.dto.RequestLoginDTO;
import pasarela.dto.RequestOAuth2DTO;
import pasarela.dto.ResponseAuthDTO;
import pasarela.security.JwtTokenBuilder;
import pasarela.servicio.IServicioUsuarios;
import servicio.ServicioException;

@RestController
@RequestMapping("/auth")

public class ControladorAuth {

	private IServicioUsuarios servicio;
	
	@Autowired
	public ControladorAuth(IServicioUsuarios servicio) {
		this.servicio = servicio;
	}
	
	@PostMapping("/login")
	public ResponseEntity<ResponseAuthDTO> autenticarContrasena(@RequestBody RequestLoginDTO requestLogin) throws ServicioException{
		Map<String, Object> claims = servicio.verificarCredenciales(requestLogin.getNombre(), requestLogin.getContrasena());
		String token = JwtTokenBuilder.buildToken(claims);
		ResponseAuthDTO response = new ResponseAuthDTO(token, claims.get("Id").toString(), claims.get("Nombre").toString(), claims.get("Rol").toString());
		return ResponseEntity.ok(response);
		
	}
	
	@GetMapping("/oauth2")
	public ResponseEntity<ResponseAuthDTO> autenticarGithub(@RequestBody RequestOAuth2DTO requestOauth) throws ServicioException{
		Map<String, Object> claims = servicio.verificarOAuth2(requestOauth.getIdOauth());
		String token = JwtTokenBuilder.buildToken(claims);
		ResponseAuthDTO response = new ResponseAuthDTO(token, claims.get("Id").toString(), claims.get("Nombre").toString(), claims.get("Rol").toString());
		return ResponseEntity.ok(response);
		
	}
	
	
	
}
