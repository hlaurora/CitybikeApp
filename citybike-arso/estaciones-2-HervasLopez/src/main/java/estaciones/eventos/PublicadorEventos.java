package estaciones.eventos;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import estaciones.eventos.config.RabbitMQConfig;

@Component
public class PublicadorEventos {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void sendMessage(Object evento, String idEvento) {
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_SEND+","+idEvento, evento);
	}

}
