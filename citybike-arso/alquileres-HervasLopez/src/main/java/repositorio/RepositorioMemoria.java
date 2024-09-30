package repositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RepositorioMemoria<T extends Identificable> implements RepositorioString<T> {

	private HashMap<String, T> entidades = new HashMap<>();

	@Override
	public String add(T entidad) {
		String idS = String.valueOf(entidad.getId());
		this.entidades.put(idS, entidad);
		return idS;
	}

	@Override
	public void update(T entidad) throws EntidadNoEncontrada {
		if (!this.entidades.containsKey(entidad.getId()))
			throw new EntidadNoEncontrada(entidad.getId() + " no existe en el repositorio");

		this.entidades.put(entidad.getId(), entidad);
	}

	@Override
	public void delete(T entidad) throws EntidadNoEncontrada {
		if (!this.entidades.containsKey(entidad.getId()))
			throw new EntidadNoEncontrada(entidad.getId() + " no existe en el repositorio");

		this.entidades.remove(entidad.getId());
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
