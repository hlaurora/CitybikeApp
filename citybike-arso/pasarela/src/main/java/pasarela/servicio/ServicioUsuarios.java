package pasarela.servicio;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import servicio.ServicioException;

@Service
public class ServicioUsuarios implements IServicioUsuarios {
	
	private static Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("http://usuarios:5268/api/usuarios/")
			.addConverterFactory(JacksonConverterFactory.create())
			.build();
	
	private UsuariosRestClient service = retrofit.create(UsuariosRestClient.class);
	
	@Override
	public Map<String, Object> verificarCredenciales(String nombre, String contrasena) throws ServicioException{
		
		Map<String, Object> claims;
		try {
			claims = service.VerificarCredenciales(nombre, contrasena).execute().body();
		} catch (IOException e) {
			throw new ServicioException(e.getMessage());
		}
		return claims;
		
	}

	@Override
	public Map<String, Object> verificarOAuth2(String id) throws ServicioException {
		Map<String, Object> claims;
		try {
			claims = service.VerificarOAuth(id).execute().body();
		} catch (IOException e) {
			throw new ServicioException(e.getMessage());
		}
		return claims;
	}

}
