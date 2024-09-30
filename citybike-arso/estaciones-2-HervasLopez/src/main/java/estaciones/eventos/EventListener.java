package estaciones.eventos;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import estaciones.eventos.config.RabbitMQConfig;
import estaciones.servicio.IServicioEstaciones;
import repositorio.EntidadNoEncontrada;

@Component
public class EventListener {

	private IServicioEstaciones servicioEstaciones;

	@Autowired
	public EventListener(IServicioEstaciones servicioEstaciones) {
		this.servicioEstaciones = servicioEstaciones;
	}

	// Escucha en la cola de estaciones "citybike-estaciones"
	@RabbitListener(queues = RabbitMQConfig.QUEUE)
	public void handleEvent(Message mensaje) throws EntidadNoEncontrada, JsonMappingException, JsonProcessingException {
		System.out.println("Mensaje recibido: " + mensaje.toString());

		String body = new String(mensaje.getBody());
		String idEvento = mensaje.getMessageProperties().getReceivedRoutingKey().split(",")[1];
		ObjectMapper objectMapper = new ObjectMapper();
		EventoAlquileres evento = null;
		evento = objectMapper.readValue(body, EventoAlquileres.class);

		// Evento "bicicleta-alquilada" cuando se ha alquilado y se tiene que poner a 'no disponible'
		if (evento != null && idEvento.equals("bicicleta-alquilada")) {
			servicioEstaciones.setDisponibilidad(evento.getIdBicicleta(), false);
		}

		// Evento "bivivleta-alquiler-concluido" cuando se concluye un alquiler y se pone a 'disponible' la bici
		else if (evento != null && idEvento.equals("bicicleta-alquiler-concluido")) {
			servicioEstaciones.setDisponibilidad(evento.getIdBicicleta(), true);
		}
	}

}
