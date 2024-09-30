package pasarela.servicio;

import java.util.Map;

import servicio.ServicioException;

public interface IServicioUsuarios {
	
	public Map<String, Object> verificarCredenciales(String nombre, String contrasena) throws ServicioException;	
	Map<String, Object> verificarOAuth2(String id) throws ServicioException;	
	
}
