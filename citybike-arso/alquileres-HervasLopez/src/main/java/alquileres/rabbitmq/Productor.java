package alquileres.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Productor {

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri("amqps://qnwlfaob:JSIZ4SPAF0cixppW6K1iHSA1xuNw0Bpr@stingray.rmq.cloudamqp.com/qnwlfaob");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		// envío del mensaje
		String mensaje = "hola";
		String routingKey = "citybike.estaciones";
		channel.basicPublish("citybike", routingKey, new AMQP.BasicProperties().builder().build(), mensaje.getBytes());

		channel.close();
		connection.close();
	}

}
