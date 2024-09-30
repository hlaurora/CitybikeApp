package estaciones.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para una nueva Bicicleta")
public class NuevaBicicletaDTO {

	@Schema(description = "Modelo de la Bicicleta")
	@NotNull(message = "La nueva bicicleta debe tener un modelo")
	@NotBlank(message = "La nueva bicicleta debe tener un modelo")
	private String modelo;
	@NotNull(message = "La nueva bicicleta debe estar en una estación")
	@NotBlank(message = "La nueva bicicleta debe estar en una estación")
	@Schema(description = "Identificador de la Estacion en la que se estacionará la Bicicleta")
	private String idEstacion;

	public NuevaBicicletaDTO(String modelo, String idEstacion) {
		super();
		this.modelo = modelo;
		this.idEstacion = idEstacion;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getIdEstacion() {
		return idEstacion;
	}

	public void setIdEstacion(String idEstacion) {
		this.idEstacion = idEstacion;
	}

}
