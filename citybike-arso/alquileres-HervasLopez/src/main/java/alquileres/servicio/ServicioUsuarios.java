package alquileres.servicio;

import java.util.HashMap;
import java.util.Map;

public class ServicioUsuarios implements IServicioUsuarios {
	Map<String, String> usuarios;
	
	public ServicioUsuarios() {
		usuarios = new HashMap<>();
	    usuarios.put("usuario1", "usuario");
	    //el gestor tiene también las funcionalidades de los usuarios
	    usuarios.put("gestor1", "gestor,usuario");
	}
	

	@Override
	public Map<String, Object> verificarCredenciales(String username, String password) {
		Map<String, Object> claims = new HashMap<String, Object>(); 
		claims.put("sub", username);
		claims.put("roles", usuarios.get(username));
		return claims;
	}

}
