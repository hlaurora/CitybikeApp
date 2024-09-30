package alquileres.rabbitmq;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import alquileres.servicio.IServicioAlquileres;
import servicio.FactoriaServicios;

public class Consumidor {

	public static void main(String[] args) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		//factory.setUri("amqps://qnwlfaob:JSIZ4SPAF0cixppW6K1iHSA1xuNw0Bpr@stingray.rmq.cloudamqp.com/qnwlfaob");
		factory.setUri("amqp://guest:guest@rabbitmq:5672");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		final String exchangeName = "citybike";
		final String queueName = "citybike-alquileres";
		final String bindingKey = "citybike.alquileres";

		boolean durable = true;
		boolean exclusive = false;
		boolean autodelete = false;

		Map<String, Object> properties = null;
		channel.queueDeclare(queueName, durable, exclusive, autodelete, properties);
		// Hacemos binding con la clave "citybike.alquileres" y con
		// "citybike.alquileres,bicicleta-desactivada", para así poder recibir los
		// mensajes que correspondan al evento "bicicleta-desactivada"
		channel.queueBind(queueName, exchangeName, bindingKey);
		channel.queueBind(queueName, exchangeName, bindingKey + ",bicicleta-desactivada");

		final IServicioAlquileres servicioAlquileres = FactoriaServicios.getServicio(IServicioAlquileres.class);

		boolean autoAck = false;
		channel.basicConsume(queueName, autoAck, "citybike-alquileres", new DefaultConsumer(channel) {
			// Clase para tratar los mensajes recibidos
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String routingKey = envelope.getRoutingKey();
				String contentType = properties.getContentType();
				long deliveryTag = envelope.getDeliveryTag();
				String contenido = new String(body);
				ObjectMapper objectMapper = new ObjectMapper();
				// Obtenemos el id del evento dividiendo el routing key
				String idEvento = routingKey.split(",")[1];
				System.out.println(contenido);
				// Si el evento es "bicicleta-desactivada", lo tratamos mediante el servicio de
				// alquileres
				if (idEvento.equals("bicicleta-desactivada")) {
					EventoBicicletaDesactivada evento = null;
					try {
						evento = objectMapper.readValue(contenido, EventoBicicletaDesactivada.class);
						servicioAlquileres.tratamientoBicicletaDesactivada(evento.getIdBicicleta());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// Confirma el procesamiento
				channel.basicAck(deliveryTag, false);
			}
		});

	}

}
