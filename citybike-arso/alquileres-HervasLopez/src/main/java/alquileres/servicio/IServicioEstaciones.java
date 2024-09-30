package alquileres.servicio;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

// Interfaz para la comunicación mediante retrofit con el microservicio Estaciones
public interface IServicioEstaciones {

	@GET("/estaciones/info/{id}")
	Call<EstacionResumen> tieneHueco(@Path("id") String id);

	@PATCH("/estaciones/aparcadas/{idEstacion}/bicicleta/{idBicicleta}")
	Call<Void> situarBicicleta(@Path("idEstacion") String idEstacion, @Path("idBicicleta") String idBicicleta);

}
