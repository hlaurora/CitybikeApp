package repositorio;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

// Repositorio para las clases almacenadas en MongoDB
public abstract class RepositorioMongoDB<T extends Identificable> implements RepositorioString<T> {

	public abstract MongoCollection<T> getColeccion();

	@Override
	public String add(T entity) throws RepositorioException {
		if (entity.getId() != null) {
			try {
				if (getById(entity.getId()) != null) {
					throw new RepositorioException("Ya existe una entidad con ese ID");
				}
			} catch (EntidadNoEncontrada e) {
				e.printStackTrace();
			}
		}
		this.getColeccion().insertOne(entity);
		return entity.getId();
	}

	@Override
	public void update(T entity) throws RepositorioException, EntidadNoEncontrada {
		if (entity.getId() == null) {
			throw new RepositorioException("El ID de la entidad no puede ser nulo");
		}

		T entidadExistente = getById(entity.getId());
		// Busca la entidad y la reemplaza en la coleccion
		if (entidadExistente != null) {
			this.getColeccion().replaceOne(Filters.eq("_id", new ObjectId(entity.getId())), entity);
		} else {
			throw new EntidadNoEncontrada("La entidad no existe");
		}
	}

	@Override
	public void delete(T entity) throws RepositorioException, EntidadNoEncontrada {
		try {
			DeleteResult result = this.getColeccion().deleteOne(Filters.eq("_id", new ObjectId(entity.getId())));
			if (result.getDeletedCount() == 0) {
				throw new EntidadNoEncontrada("La entidad no existe");
			}
		} catch (IllegalArgumentException e) {
			throw new RepositorioException("Error al intentar eliminar la entidad", e);
		}
	}

	@Override
	public T getById(String id) throws RepositorioException, EntidadNoEncontrada {
		// Buscar el documento por el campo "_id"		
		try {
	        ObjectId objectId = new ObjectId(id);
	        T entidad = (T) getColeccion().find(Filters.eq("_id", objectId)).first();
	        if (entidad == null) {
	            throw new EntidadNoEncontrada("La entidad no existe");
	        }
	        return entidad;
	    } catch (IllegalArgumentException e) {
	        throw new RepositorioException("Error al intentar obtener la entidad por ID", e);
	    }
	}

	@Override
	public List<T> getAll() throws RepositorioException {
	    try {
	        List<T> listado = new ArrayList<T>();
	        FindIterable<T> resultados = getColeccion().find();

	        MongoCursor<T> it = resultados.iterator();
	        while (it.hasNext()) {
	            listado.add(it.next());
	        }

	        return listado;
	    } catch (Exception e) {
	        throw new RepositorioException("Error al obtener todos los elementos", e);
	    }
	}

}
