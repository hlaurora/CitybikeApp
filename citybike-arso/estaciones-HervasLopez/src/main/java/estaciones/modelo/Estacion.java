package estaciones.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonRepresentation;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import repositorio.Identificable;

//Representa una estación de bicicletas
//Contiene una lista de Sitios Turisticos cercanos
public class Estacion implements Identificable {

	@BsonId
	@BsonRepresentation(BsonType.OBJECT_ID)
	private String id;
	private String nombre;
	@BsonProperty(value = "num_puestos")
	private int numPuestos;
	private String direccion;
	private Point location; // Coordenadas
	@BsonProperty(value = "fecha_alta")
	private LocalDate fechaAlta;
	@BsonProperty(value = "sitios_turisticos")
	private List<SitioTuristico> sitiosTuristicos = new ArrayList<SitioTuristico>();
	@BsonProperty(value = "bicicletas")
	private List<String> bicicletas = new ArrayList<String>();

	public Estacion() {

	}

	public Estacion(String nombre, int numPuestos, String direccion, double lat, double lng) {
		this.nombre = nombre;
		this.numPuestos = numPuestos;
		this.direccion = direccion;
		this.location = new Point(new Position(lat, lng));
		this.fechaAlta = LocalDate.now();
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

	public int getNumPuestos() {
		return numPuestos;
	}

	public void setNumPuestos(int numPuestos) {
		this.numPuestos = numPuestos;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	// Devuelve la coordenada Latitud del atributo location
	public double getlLat() {
		return this.location.getPosition().getValues().get(0);
	}

	// Devuelve la coordenada Longitud del atributo location
	public double getlLng() {
		return this.location.getPosition().getValues().get(1);
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public List<SitioTuristico> getSitiosTuristicos() {
		return sitiosTuristicos;
	}

	public void addSitioTuristico(SitioTuristico s) {
		this.sitiosTuristicos.add(s);
	}

	public void setSitiosTuristicos(List<SitioTuristico> sitios) {
		for (SitioTuristico s : sitios) {
			this.addSitioTuristico(s);
		}
	}


	// Añade una Bicicleta a la Estacion si hay algún puesto libre y la bicicleta
	// está disponible y no está aparcada ya
	public boolean addBicicleta(Bicicleta b) {
		if (bicicletas.size() < numPuestos && b.isDisponible() && b.getIdEstacionActual()!=null) {
			this.bicicletas.add(b.getId());
			return true;
		}
		System.out.println("La estación está completa o la bicicleta no está disponible o la bicicleta ya está aparcada\n");
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
	public double distanciaCoordenadas(double lat, double lng) {
		double distanciaLatitud = this.getlLat() - lat;
		double distanciaLongitud = this.getlLng() - lng;
		double distancia = Math.sqrt((distanciaLatitud * distanciaLatitud) + (distanciaLongitud * distanciaLongitud));
		return distancia;
	}

	@Override
	public String toString() {
		return "Estacion [id=" + id + ", nombre=" + nombre + ", numPuestos=" + numPuestos + ", direccion=" + direccion
				+ ", location=" + location + ", fechaAlta=" + fechaAlta + ", sitiosTuristicos=" + sitiosTuristicos
				+ ", bicicletas=" + bicicletas + "]";
	}

}
