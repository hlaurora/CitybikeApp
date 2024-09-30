package estaciones.repositorio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

import estaciones.modelo.Estacion;
import repositorio.RepositorioException;
import repositorio.RepositorioMongoDB;
import utils.PropertiesReader;

public class RepositorioEstacionesMongoDB extends RepositorioMongoDB<Estacion> implements RepositorioEstacionesAdHoc {

	protected MongoClient mongoClient;
	protected MongoDatabase database;
	protected MongoCollection<Estacion> coleccion;

	public RepositorioEstacionesMongoDB() {
		PropertiesReader properties;
		try {
			properties = new PropertiesReader("mongo.properties");

			String connectionString = properties.getProperty("mongouri");

			MongoClient mongoClient = MongoClients.create(connectionString);

			String mongoDatabase = properties.getProperty("mongodatabase");

			database = mongoClient.getDatabase(mongoDatabase);

			CodecRegistry defaultCodecRegistry = CodecRegistries.fromRegistries(
					MongoClientSettings.getDefaultCodecRegistry(),
					CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

			coleccion = database.getCollection("estacion", Estacion.class).withCodecRegistry(defaultCodecRegistry);

		} catch (Exception e) {

		}
	}

	@Override
	public MongoCollection<Estacion> getColeccion() {
		return coleccion;
	}

	// Devuelve la primera Estacion del repositorio que tenga algún puesto libre
	@Override
	public Estacion estacionPrimeraLibre() throws RepositorioException {
		List<Estacion> estaciones = this.getAll();
		for (Estacion estacion : estaciones) {
			if (estacion.hayPuestoLibre()) {
				return estacion;
			}
		}
		return null;
	}

	// Devuelve las Estaciones cercanas a las coordenadas (lat, lng)
	@Override
	public List<Estacion> getByCercania(double lat, double lng) throws RepositorioException {
		// Por ejemplo distancia Máxima= 0.018 grados (2 km aproximadamente)
		double distanciaMax = 0.018;
		List<Estacion> estacionesCercanas = new ArrayList<Estacion>();
		// Recupera todas las Estaciones y calcula su distancia a las coordenadas dadas
		for (Estacion e : this.getAll()) {
			// Si es menor que distanciaMax, la añade a la lista
			if ((e.distanciaCoordenadas(lat, lng) <= distanciaMax) && (estacionesCercanas.size() < 3)) {
				estacionesCercanas.add(e);
			}
		}

		return estacionesCercanas;
	}

	@Override
	public List<Estacion> getByMayorSitiosTuristicos() throws RepositorioException {
	      ArrayList<Estacion> estaciones = new ArrayList<>();
	        try {
	            Bson filter = Filters.exists("sitios_turisticos"); 

	            AggregateIterable<Estacion> resultados = getColeccion().aggregate(Arrays.asList(
	                    Aggregates.match(filter),
	                    Aggregates.project(Projections.fields(
	                            Projections.include("nombre", "sitios_turisticos"),
	                            Projections.computed("num_sitios_turisticos", new Document("$size", "$sitios_turisticos"))
	                    )),
	                    //Ordenamos por numero de sitios turisticos
	                    Aggregates.sort(Sorts.descending("num_sitios_turisticos"))
	            ));

	            MongoCursor<Estacion> it = resultados.iterator();
	            while (it.hasNext()) {
	            	estaciones.add(it.next());
	            }

	        } catch (Exception e) {
	            throw new RepositorioException("error getEstacionesOrdenadasPorSitiosTuristicos", e);
	        }
	        return estaciones;
	}
	

	@Override
	public List<String> getIds() throws RepositorioException {
		// TODO Auto-generated method stub
		return null;
	}

}
