package estaciones.modelo;

import java.util.LinkedList;
import java.util.List;

import repositorio.Identificable;

//Representa un sitio de interés cercano a ciertas coordenadas
//se obtiene la información de su página de wikipedia
public class SitioTuristico implements Identificable {

	private String id;
	private String nombre;
	private String resumen;
	private List<String> categorias;
	private List<String> enlacesExternos;
	private String imagen;
	private String url;

	public SitioTuristico() {
	}

	public SitioTuristico(String id, String nombre, String resumen, List<String> categorias,
			List<String> enlacesExternos, String imagen, String url) {
		this.id = id;
		this.nombre = nombre;
		this.resumen = resumen;
		this.categorias = new LinkedList<String>(categorias);
		this.enlacesExternos = new LinkedList<String>(enlacesExternos);
		this.imagen = imagen;
		this.url = url;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public List<String> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<String> categorias) {
		this.categorias = categorias;
	}

	public List<String> getEnlacesExternos() {
		return enlacesExternos;
	}

	public void setEnlacesExternos(List<String> enlacesExternos) {
		this.enlacesExternos = enlacesExternos;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "SitioTuristico [nombre=" + nombre + ", resumen=" + resumen + ", categorias=" + categorias
				+ ", enlacesExternos=" + enlacesExternos + ", imagen=" + imagen + ", url=" + url + "]";
	}

}