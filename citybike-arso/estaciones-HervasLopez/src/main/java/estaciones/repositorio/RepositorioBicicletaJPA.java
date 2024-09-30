package estaciones.repositorio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import estaciones.modelo.Bicicleta;
import estaciones.modelo.EstadoIncidencia;
import estaciones.modelo.Incidencia;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import repositorio.RepositorioJPA;
import utils.EntityManagerHelper;

public class RepositorioBicicletaJPA extends RepositorioJPA<Bicicleta> {

	// Devuelve la clase Bicicleta
	@Override
	public Class<Bicicleta> getClase() {
		return Bicicleta.class;
	}

	// Devuelve el nombre simple de Bicicleta
	@Override
	public String getNombre() {
		return Bicicleta.class.getName().substring(Bicicleta.class.getName().lastIndexOf(".") + 1);
	}

	public Incidencia getIncidenciaById(String uuid) {
		EntityManager em = EntityManagerHelper.getEntityManager();

		TypedQuery<Incidencia> query = em.createQuery("SELECT i FROM Incidencia i WHERE i.id = :uuid",
				Incidencia.class);
		query.setParameter("uuid", uuid);
		Incidencia incidencia = query.getSingleResult();

		try {
			return incidencia;
		} catch (NoResultException e) {
			return null; // Devuelve una lista vacía si no hay resultados
		} finally {
			em.close();
		}
	}

	// @Override
	public List<Incidencia> recuperarIncidenciasAbiertas() throws RepositorioException, EntidadNoEncontrada {

		EntityManager em = EntityManagerHelper.getEntityManager();

		TypedQuery<Incidencia> query = em.createQuery("SELECT i FROM Incidencia i WHERE i.estado IN :estados",
				Incidencia.class);
		query.setParameter("estados", Arrays.asList(EstadoIncidencia.ASIGNADA, EstadoIncidencia.PENDIENTE));
		List<Incidencia> incidencias = query.getResultList();

		try {
			return incidencias;
		} catch (NoResultException e) {
			return new ArrayList<>(); // Devuelve una lista vacía si no hay resultados
		} finally {
			em.close();
		}
	}

}
