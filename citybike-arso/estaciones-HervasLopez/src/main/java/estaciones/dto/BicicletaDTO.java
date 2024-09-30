package estaciones.dto;

import java.io.Serializable;

public class BicicletaDTO implements Serializable {
	
	private String id;
	private String modelo;
	private boolean disponible;
	private String idEstacionActual;
	
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
