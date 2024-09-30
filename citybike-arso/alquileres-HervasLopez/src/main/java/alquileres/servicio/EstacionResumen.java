package alquileres.servicio;

import java.time.LocalDate;


// Clase para el Resumen de la clase Estacion
// Incluye el id, el nombre, la dirección postal y si tiene huecos libres
public class EstacionResumen {

	private String id;
	private String nombre;
	private String direccion;
	private boolean huecosLibres;
	private String fechaAlta;
	private int puestosDisponibles;
	private int numPuestos;
	private String codigoPostal;

	public EstacionResumen() {
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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public boolean isHuecosLibres() {
		return huecosLibres;
	}

	public void setHuecosLibres(boolean huecosLibres) {
		this.huecosLibres = huecosLibres;
	}

	public String getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public int getPuestosDisponibles() {
		return puestosDisponibles;
	}

	public void setPuestosDisponibles(int puestosDisponibles) {
		this.puestosDisponibles = puestosDisponibles;
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

	@Override
	public String toString() {
		return "EstacionResumen [id=" + id + ", nombre=" + nombre + ", direccion=" + direccion + ", huecosLibres="
				+ huecosLibres + ", fechaAlta=" + fechaAlta + ", puestosDisponibles=" + puestosDisponibles
				+ ", numPuestos=" + numPuestos + ", codigoPostal=" + codigoPostal + "]";
	}
	
	
	

	

}
