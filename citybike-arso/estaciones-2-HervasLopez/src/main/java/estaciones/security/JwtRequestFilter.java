package estaciones.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Implementación del control de autorización
		String authorization = request.getHeader("Authorization");

		String username = null;
		Set<String> roles_token;

		if (authorization != null && authorization.startsWith("Bearer ")) {
			String token = authorization.substring("Bearer ".length()).trim();

			// Busca el token en la petición
			if (token != null) {
				Claims claims = Jwts.parser().setSigningKey("secreto").parseClaimsJws(token).getBody();

				// Recupera el usuario y sus roles
				ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				roles_token = new HashSet<>(Arrays.asList(claims.get("Rol", String.class).split(",")));
				for (String s : roles_token) {
					authorities.add(new SimpleGrantedAuthority(s));
				}

				username = claims.getSubject();

				// Establece la autenticación en el contexto de seguridad de la aplicación
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
						authorities);

				SecurityContextHolder.getContext().setAuthentication(auth);
			}

			else {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token no válido");
			}

		}

		filterChain.doFilter(request, response);

	}

}
