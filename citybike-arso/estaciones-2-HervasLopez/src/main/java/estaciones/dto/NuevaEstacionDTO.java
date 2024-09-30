package estaciones.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para una nueva Estacion")
public class NuevaEstacionDTO {

	@Schema(description = "Nombre de la nueva Estacion")
	@NotNull(message = "La nueva estación debe tener nombre")
	@NotBlank(message = "La nueva estación debe tener nombre")
	private String nombre;
	@Schema(description = "Numero de puestos de la Estacion")
	@Min(value = 0, message = "El número de puestos no debe ser menor de 0")
	private int numPuestos;
	@Schema(description = "Dirección de la Estacion")
	@NotNull(message = "La nueva estación debe tener dirección")
	@NotBlank(message = "La nueva estación debe tener dirección")
	private String direccion;
	/*@Schema(description = "Coordenada latitud de la Estacion")
	private double lat;
	@Schema(description = "Coordenada longitud de la Estacion")
	private double lng;*/
	@Schema(description = "codigoPostal de la Estacion")
	@NotNull(message = "La nueva estación debe tener codigoPostal")
	@NotBlank(message = "La nueva estación debe tener codigoPostal")
	private String codigoPostal;

	public NuevaEstacionDTO(String nombre, int numPuestos, String direccion, //double lat, double lng, 
							String codigoPostal) {
		super();
		this.nombre = nombre;
		this.numPuestos = numPuestos;
		this.direccion = direccion;
		//this.lat = lat;
		//this.lng = lng;
		this.codigoPostal = codigoPostal;
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

	
	
	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	/*public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}*/

}
