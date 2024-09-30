package repositorio;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyNamingStrategy;
import javax.json.bind.spi.JsonbProvider;
/*
public abstract class RepositorioJSON<T extends Identificable> implements RepositorioString<T> {

	// Directorio en el que se guardan los ficheros
	public final static String DIRECTORIO = "json/";

	static {
		File directorio = new File(DIRECTORIO);
		if(!directorio.exists())
			directorio.mkdir();
	}

	@Override
	public String add(T entity) throws RepositorioException {

		// Configuración del contexto
		JsonbConfig config = new JsonbConfig().withFormatting(true)
				.withPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES);

		// Construcción del contexto
		Jsonb contexto = JsonbProvider.provider().create().withConfig(config).build();
		String cadenaJSON = contexto.toJson(entity);

		try {
			// Escribe el fichero
			String nombreArchivo = "json/SitioTuristico-" + entity.getId() + ".json";
			try (FileWriter fileWriter = new FileWriter(new File(nombreArchivo))) {
				fileWriter.write(cadenaJSON);
			}
		} catch (Exception e) {
			throw new RepositorioException(e.getMessage(), e);
		}

		return entity.getId();
	}

	public abstract Class<T> getClase();

	@Override
	public void update(T entity) throws RepositorioException, EntidadNoEncontrada {
		String id = entity.getId();
		// Existe documento con ese id? -> guardar
		File file = new File("json/SitioTuristico-" + id + ".json");
		if (!file.exists()) {
			throw new EntidadNoEncontrada("no existe id: " + id);
		}

		try {
			JsonbConfig config = new JsonbConfig().withFormatting(true)
					.withPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES);
			Jsonb jsonb = JsonbProvider.provider().create().withConfig(config).build();
			String cadenaJSON = jsonb.toJson(entity);

			try (FileWriter fileWriter = new FileWriter(file)) {
				fileWriter.write(cadenaJSON);
			}

		} catch (Exception e) {
			throw new RepositorioException(e.getMessage(), e);
		}

	}

	@Override
	public void delete(T entity) throws EntidadNoEncontrada {
		String id = entity.getId();
		// Existe documento con ese id? -> borrar
		File file = new File("json/SitioTuristico-" + id + ".json");
		if (!file.exists()) {
			throw new EntidadNoEncontrada("no existe id: " + id);
		}

		file.delete();

	}

	@Override
	public T getById(String id) throws RepositorioException, EntidadNoEncontrada {
		File file = new File("json/SitioTuristico-" + id + ".json");

		// Existe documento con ese id? -> obtener
		if (!file.exists()) {
			throw new EntidadNoEncontrada("no existe id: " + id);
		}

		try {

			JsonbConfig config = new JsonbConfig().withFormatting(true)
					.withPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES);
			Jsonb jsonb = JsonbProvider.provider().create().withConfig(config).build();
			// Obtiene el json del documento
			T entidad = (T) jsonb.fromJson(new FileReader(file), getClase());
			return entidad;
		} catch (Exception e) {
			throw new RepositorioException(e.getMessage(), e);
		}

	}

	@Override
	public List<T> getAll() throws RepositorioException, EntidadNoEncontrada {
		LinkedList<T> resultado = new LinkedList<>();
		for (String id : getIds()) {
			T entidad = getById(id);
			resultado.add(entidad);
		}
		return resultado;
	}

	@Override
	public List<String> getIds() {

		LinkedList<String> resultado = new LinkedList<>();

		File directorio = new File(DIRECTORIO);

		File[] entidades = directorio.listFiles(f -> f.isFile() && f.getName().endsWith(".json"));

		final String prefijo = getClase().getSimpleName() + "-";
		for (File file : entidades) {

			String id = file.getName().substring(prefijo.length(), file.getName().length() - 5);

			resultado.add(id);
		}

		return resultado;
	}

}*/
