package estaciones.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import repositorio.Identificable;

//Representa una estación de bicicletas

@Document(collection = "estaciones")
public class Estacion implements Identificable {

	@Id
	private String id;
	private String nombre;
	private int numPuestos;
	private String direccion;
	//private GeoJsonPoint location; // Coordenadas
	private LocalDate fechaAlta;
	private List<String> bicicletas = new ArrayList<String>();
	private String codigoPostal;

	public Estacion() {

	}

	public Estacion(String nombre, int numPuestos, String direccion, String codigoPostal) {
		this.nombre = nombre;
		this.numPuestos = numPuestos;
		this.direccion = direccion;
		//this.location = new GeoJsonPoint(lat, lng);
		this.fechaAlta = LocalDate.now();
		this.codigoPostal = codigoPostal;
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
	
	

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public int getNumPuestos() {
		return numPuestos;
	}

	public void setNumPuestos(int numPuestos) {
		this.numPuestos = numPuestos;
	}
	
	public int getHuecosLibres() {
		return this.numPuestos - this.bicicletas.size();
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	// Devuelve la coordenada Latitud del atributo location
	/*public double getlLat() {
		return this.location.getX();
	}

	// Devuelve la coordenada Longitud del atributo location
	public double getlLng() {
		return this.location.getY();
	}

	public GeoJsonPoint getLocation() {
		return location;
	}

	public void setLocation(GeoJsonPoint location) {
		this.location = location;
	}*/

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	// Añade una Bicicleta a la Estacion si hay algún puesto libre
	public boolean addBicicleta(Bicicleta b) {
		if (bicicletas.size() < numPuestos) {
			this.bicicletas.add(b.getId());
			return true;
		}
		System.out.println("La estación está completa\n");
		return false;
	}

	public List<String> getBicicletas() {
		return bicicletas;
	}

	public void setBicicletas(List<String> bicicletas) {
		this.bicicletas = bicicletas;
	}

	// Elimina la Bicicleta de la Estacion (de la lista bicicletas)
	public void retirarBici(Bicicleta b) {
		String borrar = null;
		for (String bicicleta : this.bicicletas) {
			if (bicicleta.equals(b.getId())) {
				borrar = bicicleta;
				break;
			}
		}
		if (borrar != null)
			this.bicicletas.remove(borrar);
	}

	// Comprueba si hay algun puesto libre en la Estacion
	public boolean hayPuestoLibre() {
		return bicicletas.size() < numPuestos;
	}

	// Calcula la distancia entre la Estación y unas coordenadas dadas
/*	public double distanciaCoordenadas(double lat, double lng) {
		double distanciaLatitud = this.getlLat() - lat;
		double distanciaLongitud = this.getlLng() - lng;
		double distancia = Math.sqrt((distanciaLatitud * distanciaLatitud) + (distanciaLongitud * distanciaLongitud));
		return distancia;
	}*/

	@Override
	public String toString() {
		return "Estacion [id=" + id + ", nombre=" + nombre + ", numPuestos=" + numPuestos + ", direccion=" + direccion
				+ ", fechaAlta=" + fechaAlta + ", codigoPostal=" + codigoPostal + ", bicicletas=" + bicicletas + "]";
	}

}
