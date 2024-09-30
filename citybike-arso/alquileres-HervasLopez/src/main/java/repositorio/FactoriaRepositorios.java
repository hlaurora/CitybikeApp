package repositorio;

import utils.PropertiesReader;

public class FactoriaRepositorios {

	private static final String PROPERTIES = "repositorios.properties";

	@SuppressWarnings("unchecked")
	public static <T, K, R extends Repositorio<T, K>> R getRepositorio(Class<?> entidad) {
		try {
			PropertiesReader properties = new PropertiesReader(PROPERTIES);
			String clase = properties.getProperty(entidad.getName());
			return (R) Class.forName(clase).getConstructor().newInstance();
		} catch (Exception e) {

			e.printStackTrace(); 

			throw new RuntimeException("No se ha podido obtener el repositorio para la entidad: " + entidad.getName());
		}

	}

}
