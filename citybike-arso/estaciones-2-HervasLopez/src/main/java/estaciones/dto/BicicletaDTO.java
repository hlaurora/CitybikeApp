package estaciones.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de la entidad Bicicleta")
public class BicicletaDTO implements Serializable {

	@Schema(description = "Identificador de la Bicicleta")
	private String id;
	@Schema(description = "Modelo de la Bicicleta")
	private String modelo;
	@Schema(description = "Indicador de si la Bicicleta se encuentra disponible", example = "true")
	private boolean disponible;
	@Schema(description = "Identificador de la Estacion en la que se encuentra la bicicleta")
	private String idEstacionActual;

	public BicicletaDTO() {
	}

	public BicicletaDTO(String id, String modelo, boolean disponible, String idEstacionActual) {
		super();
		this.id = id;
		this.modelo = modelo;
		this.disponible = disponible;
		this.idEstacionActual = idEstacionActual;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;

	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public String getIdEstacionActual() {
		return idEstacionActual;
	}

	public void setIdEstacionActual(String idEstacionActual) {
		this.idEstacionActual = idEstacionActual;
	}

}
