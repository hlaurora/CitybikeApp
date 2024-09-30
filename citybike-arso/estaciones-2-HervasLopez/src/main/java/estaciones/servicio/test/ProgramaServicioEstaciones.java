package estaciones.servicio.test;

/*
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import estaciones.EstacionesApplication;
import estaciones.servicio.IServicioEstaciones;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

public class ProgramaServicioEstaciones {
	

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext contexto = SpringApplication.run(EstacionesApplication.class, args);
		IServicioEstaciones servicioEstacion = contexto.getBean(IServicioEstaciones.class);
		
		
		// Latitud y longitud de prueba 
		double lat = 38.023694444444; 
		double lng = -1.1741388888889; 

		// Estación de prueba con esas coordenadas y suficientas huecos libres
		String idEstacion1 = servicioEstacion.altaEstacion("estacion1", 10, "calle1", lat, lng);		
		
		// Añadimos dos bicicletas a la estacion1
		servicioEstacion.altaBicicleta("Modelo a", idEstacion1);
		servicioEstacion.altaBicicleta("Modelo b", idEstacion1);

		// Obtenemos el listado de estaciones y lo imprimimos												
		System.out.println(servicioEstacion.getInfoEstacion(idEstacion1));
		
		// Estacion con solo 1 hueco y una bici aparcada
		String idEstacion2 = servicioEstacion.altaEstacion("estacion2", 1, "calle2", lat, lng);	

		servicioEstacion.altaBicicleta("Bici de estacion2", idEstacion2);

		
		contexto.close();

		



		
		/*
		
		// Estaciona la bici1 en la Estacion2
		System.out.println("\n---Obtenemos las bicicletas aparcadas en las dos estaciones---\n");
		System.out.println("Estación 1:");
		for (String b : servicioEstacion.getEstacion(idEstacion1).getBicicletas()) {
			System.out.println(b.toString());
		}
		System.out.println("Estación 2:");
		for (String b : servicioEstacion.getEstacion(idEstacion2).getBicicletas()) {
			System.out.println(b.toString());
		}

		///// Dar de baja una bibicleta /////
		// Damos de baja la bicicleta 2 , aparcada en la estación 2
		servicioEstacion.bajaBicicleta(idBici2, "Bici defectuosa");
		System.out.println("\n---Obtenemos las bicicletas aparcadas en la estación 2---\n");
		System.out.println(servicioEstacion.getEstacion(idEstacion1).getBicicletas().toString());

		
		
		

		
	}

}*/
