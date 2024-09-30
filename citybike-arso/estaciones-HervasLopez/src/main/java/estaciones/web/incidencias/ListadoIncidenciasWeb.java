package estaciones.web.incidencias;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import estaciones.dto.IncidenciaDTO;
import estaciones.servicio.IServicioIncidencias;
import servicio.FactoriaServicios;
import servicio.ServicioException;

@Named
@ViewScoped
public class ListadoIncidenciasWeb implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<IncidenciaDTO> incidencias = new ArrayList<IncidenciaDTO>();
	private IncidenciaDTO incidenciaSeleccionada;

	@Inject
	protected FacesContext facesContext;

	private IServicioIncidencias servicioIncidencias;

	public ListadoIncidenciasWeb() {
		servicioIncidencias = FactoriaServicios.getServicio(IServicioIncidencias.class);
	}

	@PostConstruct
	public void init() {
		try {
			incidencias = servicioIncidencias.recuperarAbiertasDTO();
		} catch (ServicioException e) {
			e.printStackTrace();
		}
	}

	public void filaSeleccionada(SelectEvent<IncidenciaDTO> event) {
		incidenciaSeleccionada = event.getObject();
		try {
			facesContext.getExternalContext()
					.redirect("/gestor/gestionIncidencia.xhtml?id=" + incidenciaSeleccionada.getId());
		} catch (IOException e) {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No se ha podido navegar"));
			e.printStackTrace();
		}

	}

	public List<IncidenciaDTO> getIncidencias() {
		return incidencias;
	}

	public void setIncidencias(List<IncidenciaDTO> incidencias) {
		this.incidencias = incidencias;
	}

	public IncidenciaDTO getIncidenciaSeleccionada() {
		return incidenciaSeleccionada;
	}

	public void setIncidenciaSeleccionada(IncidenciaDTO incidenciaSeleccionada) {
		this.incidenciaSeleccionada = incidenciaSeleccionada;
	}

}
