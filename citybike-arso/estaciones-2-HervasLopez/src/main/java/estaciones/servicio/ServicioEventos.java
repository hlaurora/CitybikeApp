package estaciones.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import estaciones.eventos.EventoBicicletaDesactivada;
import estaciones.eventos.PublicadorEventos;

//Implementacion de la interfaz IServicioEventos para lanzar el evento de "bicicleta-desactivada"
@Service
public class ServicioEventos implements IServicioEventos {
	
	// Publicador de Eventos
	private PublicadorEventos publicadorEventos;
			
	@Autowired
	public ServicioEventos (PublicadorEventos publicadorEventos) {
		this.publicadorEventos = publicadorEventos;
	}
	
	
	@Override
	public void bajaBicicleta(String idBici) {
		// Se crea el evento "bicicleta-desactivada" con el id de la bicicleta
		EventoBicicletaDesactivada e = new EventoBicicletaDesactivada(idBici);
		publicadorEventos.sendMessage(e, "bicicleta-desactivada");
	}
	
}
