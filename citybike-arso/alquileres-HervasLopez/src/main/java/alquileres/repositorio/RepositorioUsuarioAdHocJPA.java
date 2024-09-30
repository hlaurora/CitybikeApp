package alquileres.repositorio;

import java.util.ArrayList;
import java.util.List;

import alquileres.modelo.Alquiler;
import alquileres.modelo.Reserva;
import alquileres.modelo.Usuario;
import alquileres.persistencia.jpa.AlquilerEntidad;
import alquileres.persistencia.jpa.ReservaEntidad;
import alquileres.persistencia.jpa.UsuarioEntidad;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import repositorio.RepositorioJPA;

public class RepositorioUsuarioAdHocJPA extends RepositorioJPA<UsuarioEntidad> implements RepositorioUsuarioAdHoc {

	@Override
	public Class<UsuarioEntidad> getClase() {
		return UsuarioEntidad.class;
	}

	@Override
	public String getNombre() {
		return UsuarioEntidad.class.getName().substring(UsuarioEntidad.class.getName().lastIndexOf(".") + 1);
	}
	
	
	public String add(Usuario usuario) throws RepositorioException {
		UsuarioEntidad entity = transformToDTO(usuario);
		return super.add(entity);
	}
	
	public void update(Usuario usuario) throws RepositorioException, EntidadNoEncontrada {
		UsuarioEntidad entity = transformToDTO(usuario);
		super.update(entity);
	}
	
	public void delete(Usuario usuario) throws RepositorioException, EntidadNoEncontrada {
		UsuarioEntidad entity = transformToDTO(usuario);
		super.delete(entity);
	}
	
	public Usuario obtenerPorId(String id) throws EntidadNoEncontrada, RepositorioException {
		UsuarioEntidad entity = super.getById(id);
		return transformFromDTO(entity);
	}
	
	public List<Usuario> obtenerUsuarios() throws EntidadNoEncontrada, RepositorioException {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		List<UsuarioEntidad> entity = super.getAll();
		for(UsuarioEntidad usuarioEntidad : entity)
			usuarios.add(transformFromDTO(usuarioEntidad));
		return usuarios;
	}
	
	private UsuarioEntidad transformToDTO(Usuario usuario) {
		UsuarioEntidad usuarioEntidad = new UsuarioEntidad(usuario.getId(), null, null);
		for (Reserva r : usuario.getReservas())
			usuarioEntidad.addReserva(r.getIdBicicleta(), r.getCreada(), r.getCaducidad());
		for (Alquiler a : usuario.getAlquileres())
			usuarioEntidad.addAlquiler(a.getIdBicicleta(), a.getInicio(), a.getFin());
		return usuarioEntidad;
	}

	private Usuario transformFromDTO(UsuarioEntidad usuarioEntidad) {
		Usuario usuario = new Usuario(usuarioEntidad.getId());
		for (ReservaEntidad r : usuarioEntidad.getReservas()) {
			Reserva reserva = new Reserva(r.getIdBicicleta(), r.getCreada());
			reserva.setCaducidad(r.getCaducidad());
			usuario.addReserva(reserva);
		}
		for (AlquilerEntidad a : usuarioEntidad.getAlquileres()) {
			Alquiler alquiler = new Alquiler(a.getIdBicicleta(), a.getInicio());
			alquiler.setFin(a.getFin());
			usuario.addAlquiler(alquiler);
		}
		return usuario;
	}

}
