package estaciones.servicio;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumen de la entidad Estación")
public class EstacionResumen {

	@Schema(description = "Identificador de la estación")
	private String id;
	@Schema(description = "Nombre de la estación")
	private String nombre;
	@Schema(description = "Dirección de la estación")
	private String direccion;
	@Schema(description = "Indicador de huecos libres", example = "true")
	private boolean huecosLibres;
	@Schema(description = "Fecha de alta de la estación")
	private String fechaAlta;
	@Schema(description = "Número de huecos libres")
	private int puestosDisponibles;
	@Schema(description = "Número puestos")
	private int numPuestos;
	@Schema(description = "codigoPostal")
	private String codigoPostal;

	public EstacionResumen() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPuestosDisponibles() {
		return puestosDisponibles;
	}

	public void setPuestosDisponibles(int puestosDisponibles) {
		this.puestosDisponibles = puestosDisponibles;
	}

	
	
	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	

	public int getNumPuestos() {
		return numPuestos;
	}

	public void setNumPuestos(int numPuestos) {
		this.numPuestos = numPuestos;
	}

	public String getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}



	public boolean isHuecosLibres() {
		return huecosLibres;
	}

	public void setHuecosLibres(boolean huecosLibres) {
		this.huecosLibres = huecosLibres;
	}

	

}
