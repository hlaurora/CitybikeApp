package alquileres.servicio;

import java.util.Map;

public interface IServicioUsuarios {
	
	Map<String, Object> verificarCredenciales(String username, String password);

}
