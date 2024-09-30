package alquileres.repositorio;

import java.util.List;

import alquileres.modelo.Usuario;
import alquileres.persistencia.jpa.UsuarioEntidad;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import repositorio.RepositorioString;

public interface RepositorioUsuarioAdHoc extends RepositorioString<UsuarioEntidad>{
	
	public String add(Usuario usuario) throws RepositorioException;
	public void update(Usuario usuario) throws RepositorioException, EntidadNoEncontrada;
	public void delete(Usuario usuario) throws RepositorioException, EntidadNoEncontrada;
	public Usuario obtenerPorId(String id) throws EntidadNoEncontrada, RepositorioException;
	public List<Usuario> obtenerUsuarios() throws EntidadNoEncontrada, RepositorioException;
	
}
