package estaciones.servicio;

// Resumen de un sitio de interés 
public class SitioTuristicoResumen {

	private String nombre;
	private String breveDescripción;
	private String url;
	private double distancia;
	private String id;

	public SitioTuristicoResumen(String nombre, String breveDescripcion, String url, double distancia) {
		this.nombre = nombre;
		this.breveDescripción = breveDescripcion;
		this.url = url;
		this.distancia = distancia;
		this.id = url.replace("https://es.wikipedia.org/wiki/", "");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getBreveDescripción() {
		return breveDescripción;
	}

	public void setBreveDescripción(String breveDescripción) {
		this.breveDescripción = breveDescripción;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}

	@Override
	public String toString() {
		return "SitioTuristicoResumen [nombre=" + nombre + ", breveDescripción=" + breveDescripción + ", url=" + url
				+ ", distancia=" + distancia + "]";
	}

}
