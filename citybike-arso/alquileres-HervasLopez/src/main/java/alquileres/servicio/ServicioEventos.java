package alquileres.servicio;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import alquileres.rabbitmq.EventoAlquileres;

public class ServicioEventos implements IServicioEventos {

	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private String routingKey;

	// En el constructor del servicio abrimos la conexion y el canal
	public ServicioEventos() {
		factory = new ConnectionFactory();
		try {
			//factory.setUri("amqps://qnwlfaob:JSIZ4SPAF0cixppW6K1iHSA1xuNw0Bpr@stingray.rmq.cloudamqp.com/qnwlfaob");
			factory.setUri("amqp://guest:guest@rabbitmq:5672");
			connection = factory.newConnection();
			channel = connection.createChannel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		routingKey = "citybike.estaciones";
	}
	
	// Inicio del consumidor 
	public void iniciarConsumidor() {
		
	}

	// Cuando se alquila una bicicleta, se envia el evento "bicicleta-alquilada" con
	// el id de la bicicleta y la fecha de inicio
	public void bicicletaAlquilada(String idBici, LocalDateTime horaInicio) throws Exception {

		EventoAlquileres evento = new EventoAlquileres(idBici,
				Date.from(horaInicio.atZone(ZoneId.systemDefault()).toInstant()));
		ObjectMapper objectMapper = new ObjectMapper();
		String mensaje = objectMapper.writeValueAsString(evento);
		channel.basicPublish("citybike", routingKey + ",bicicleta-alquilada",
				new AMQP.BasicProperties().builder().build(), mensaje.getBytes());

	}

	// Cuando concluye un alquiler, se envia el evento "bicicleta-
	// alquiler-concluido" con el id de la bicicleta y la fecha de fin
	@Override
	public void bicicletaAlquilerConcluido(String idBici, LocalDateTime horaFin) throws Exception {

		EventoAlquileres evento = new EventoAlquileres(idBici,
				Date.from(horaFin.atZone(ZoneId.systemDefault()).toInstant()));
		ObjectMapper objectMapper = new ObjectMapper();
		String mensaje = objectMapper.writeValueAsString(evento);
		channel.basicPublish("citybike", routingKey + ",bicicleta-alquiler-concluido",
				new AMQP.BasicProperties().builder().build(), mensaje.getBytes());

	}
}
