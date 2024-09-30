package estaciones.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

public class EditarEstacionDTO {

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
	
	private String codigoPostal;

	public EditarEstacionDTO(
			@NotNull(message = "La nueva estación debe tener nombre") @NotBlank(message = "La nueva estación debe tener nombre") String nombre,
			@Min(value = 0, message = "El número de puestos no debe ser menor de 0") int numPuestos,
			@NotNull(message = "La nueva estación debe tener dirección") @NotBlank(message = "La nueva estación debe tener dirección") String direccion, 
			String codigoPostal) {
		super();
		this.nombre = nombre;
		this.numPuestos = numPuestos;
		this.direccion = direccion;
		this.codigoPostal = codigoPostal;
	}

	public EditarEstacionDTO() {

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

	
	
	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

}
