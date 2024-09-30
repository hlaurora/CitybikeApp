package estaciones.repositorio;

import java.util.List;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.modelo.Historico;
import repositorio.RepositorioException;
import repositorio.RepositorioMongoDB;
import utils.PropertiesReader;

public class RepositorioHistoricoMongoDB extends RepositorioMongoDB<Historico> implements RepositorioHistoricoAdHoc {

	protected MongoClient mongoClient;
	protected MongoDatabase database;
	protected MongoCollection<Historico> coleccion;

	public RepositorioHistoricoMongoDB() {
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

			coleccion = database.getCollection("historico", Historico.class).withCodecRegistry(defaultCodecRegistry);

		} catch (Exception e) {

		}
	}

	@Override
	public MongoCollection<Historico> getColeccion() {
		return coleccion;
	}

	// Devuelve el Historico que activo hay para una Bicicleta y una Estacion
	@Override
	public Historico getByBiciYEstacion(Bicicleta bicicleta, Estacion estacion) throws RepositorioException {

		try {
			Bson query = Filters.exists("fechaFin", false);
			FindIterable<Historico> resultados = getColeccion().find(query);

			MongoCursor<Historico> it = resultados.iterator();

			while (it.hasNext()) {
				Historico h = it.next();
				if (h.getBicicleta().equals(bicicleta.getId())
						&& h.getEstacion().equals(estacion.getId()))
					return h;
			}

		} catch (Exception e) {
			throw new RepositorioException("error getByBiciYEstacion", e);
		}

		return null;
	}

	@Override
	public List<String> getIds() throws RepositorioException {
		return null;
	}

}
