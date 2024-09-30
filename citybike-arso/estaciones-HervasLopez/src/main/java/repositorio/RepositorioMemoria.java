package repositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RepositorioMemoria<T extends Identificable> implements RepositorioString<T> {

	private HashMap<String, T> entidades = new HashMap<>();
	private int id = 1;

	@Override
	public String add(T estacion) {
		String idS = String.valueOf(this.id++);
		estacion.setId(idS);
		this.entidades.put(idS, estacion);

		return idS;
	}

	@Override
	public void update(T estacion) throws EntidadNoEncontrada {
		if (!this.entidades.containsKey(estacion.getId()))
			throw new EntidadNoEncontrada(estacion.getId() + " no existe en el repositorio");

		this.entidades.put(estacion.getId(), estacion);
	}

	@Override
	public void delete(T estacion) throws EntidadNoEncontrada {
		if (!this.entidades.containsKey(estacion.getId()))
			throw new EntidadNoEncontrada(estacion.getId() + " no existe en el repositorio");

		this.entidades.remove(estacion.getId());
	}

	@Override
	public T getById(String id) throws EntidadNoEncontrada {
		if (!this.entidades.containsKey(id))
			throw new EntidadNoEncontrada(id + " no existe en el repositorio");

		return this.entidades.get(id);
	}

	@Override
	public List<T> getAll() {
		return new ArrayList<>(this.entidades.values());
	}

	@Override
	public List<String> getIds() {
		return new ArrayList<>(this.entidades.keySet());
	}

}
