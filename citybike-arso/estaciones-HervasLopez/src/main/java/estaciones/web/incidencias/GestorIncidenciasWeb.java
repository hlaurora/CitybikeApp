package estaciones.web.incidencias;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import estaciones.dto.IncidenciaDTO;
import estaciones.servicio.IServicioIncidencias;
import servicio.FactoriaServicios;
import servicio.ServicioException;

@Named
@ViewScoped
public class GestorIncidenciasWeb implements Serializable {

	private static final long serialVersionUID = 1L;
	private IncidenciaDTO incidencia;
	private String idIncidencia;
	private String motivoCierre = "";
	private String operario = "";
	private boolean reparada = true;

	@Inject
	protected FacesContext facesContext;

	private IServicioIncidencias servicioIncidencias;

	public GestorIncidenciasWeb() {
		servicioIncidencias = FactoriaServicios.getServicio(IServicioIncidencias.class);
	}

	public void load() {
		incidencia = servicioIncidencias.getIncidencia(idIncidencia);
	}

	public void volver() {
		try {
			facesContext.getExternalContext().redirect("/gestor/incidencias.xhtml");
		} catch (IOException e) {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No se ha podido navegar"));
			e.printStackTrace();
		}
	}

	public void resolverIncidencia() {
		try {
			if (reparada)
				servicioIncidencias.resolverIncidenciaReparada(incidencia.getBicicleta(), idIncidencia, motivoCierre);
			else
				servicioIncidencias.resolverIncidenciaNoReparable(incidencia.getBicicleta(), idIncidencia,
						motivoCierre);
			volver();
		} catch (ServicioException e) {
			e.printStackTrace();
		}
	}

	public void cancelarIncidencia() {
		try {
			servicioIncidencias.cancelarIncidencia(incidencia.getBicicleta(), idIncidencia, motivoCierre);
			volver();
		} catch (ServicioException e) {
			e.printStackTrace();
		}
	}

	public void asignarIncidencia() {
		try {
			servicioIncidencias.asignarIncidencia(incidencia.getBicicleta(), idIncidencia, operario);
			volver();
		} catch (ServicioException e) {
			e.printStackTrace();
		}
	}

	public IncidenciaDTO getIncidenciaSeleccionada() {
		return incidencia;
	}

	public void setIncidenciaSeleccionada(IncidenciaDTO incidencia) {
		this.incidencia = incidencia;
	}

	public IncidenciaDTO getIncidencia() {
		return incidencia;
	}

	public void setIncidencia(IncidenciaDTO incidencia) {
		this.incidencia = incidencia;
	}

	public String getIdIncidencia() {
		return idIncidencia;
	}

	public void setIdIncidencia(String idIncidencia) {
		this.idIncidencia = idIncidencia;
	}

	public String getMotivoCierre() {
		return motivoCierre;
	}

	public void setMotivoCierre(String motivoCierre) {
		this.motivoCierre = motivoCierre;
	}

	public boolean isReparada() {
		return reparada;
	}

	public void setReparada(boolean reparada) {
		this.reparada = reparada;
	}

	public String getOperario() {
		return operario;
	}

	public void setOperario(String operario) {
		this.operario = operario;
	}

}
