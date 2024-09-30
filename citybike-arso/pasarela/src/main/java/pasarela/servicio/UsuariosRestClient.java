package pasarela.servicio;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsuariosRestClient {
	
	@GET("registrado")
	Call<Map<String, Object>> VerificarCredenciales(@Query("nombre") String nombre, @Query("contrasena") String contrasena);

	@GET("oauth")
	Call<Map<String, Object>> VerificarOAuth(@Query("token") String token);

}
