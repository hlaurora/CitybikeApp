package estaciones.security;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/* Se activa cuando el usuario se ha autenticado con éxito en OAuth2. Su tarea es construir un token JWT con la información del usuario (claims).
 * */
@Component
public class SecuritySuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		DefaultOAuth2User usuario = (DefaultOAuth2User) authentication.getPrincipal();

		// Se imprime el nombre y login del usuario
		System.out.println(usuario.getName());
		String login = usuario.getAttribute("login");
		System.out.println(login);

		Map<String, Object> claims = fetchUserInfo(usuario);

		Date caducidad = Date.from(Instant.now().plusSeconds(3600));

		// Se crea el token con las claims y 1 hora de duración
		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, "secreto")
				.setExpiration(caducidad).compact();
		
		// Se escribe el token en la salida para el usuario
		response.getOutputStream().write(("Usuario autenticado correctamente!, este es tu token:\n\n" + token).getBytes());

	}

	private Map<String, Object> fetchUserInfo(DefaultOAuth2User usuario) {

		Map<String, Object> claims = new HashMap<>();

		claims.put("sub", usuario.getAttribute("name"));
		claims.put("roles", "gestor,usuario");

		return claims;
	}

}
