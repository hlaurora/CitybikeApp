package pasarela.security;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenBuilder {

	
	private static final String SECRET_KEY = "secreto";
	
	public static String buildToken(Map<String, Object> claims) {
		Date caducidad = Date.from(
				Instant.now()
				.plusSeconds(10800)); 
		String token = Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.setExpiration(caducidad)
				.compact();
		return token;
	}
	
}
