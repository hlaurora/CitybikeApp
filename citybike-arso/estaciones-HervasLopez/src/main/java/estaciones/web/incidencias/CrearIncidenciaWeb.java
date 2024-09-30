package estaciones.web.incidencias;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import estaciones.servicio.IServicioIncidencias;
import servicio.FactoriaServicios;
import servicio.ServicioException;

@Named
@ViewScoped
public class CrearIncidenciaWeb implements Serializable {

	private static final long serialVersionUID = 1L;
	private String descripcion;
	private String idBici;

	@Inject
	protected FacesContext facesContext;
	private IServicioIncidencias servicioIncidencias;

	public CrearIncidenciaWeb() {
		servicioIncidencias = FactoriaServicios.getServicio(IServicioIncidencias.class);
	}

	public void crearIncidencia() {
		if (idBici == null || idBici.isEmpty() || descripcion==null || descripcion.isEmpty())
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							facesContext.getApplication().evaluateExpressionGet(facesContext,
									"#{text['error']}", String.class),
							facesContext.getApplication().evaluateExpressionGet(facesContext, "#{text['errorIdBici']}",
									String.class)));
		else {
			try {
				String resultado = servicioIncidencias.crear(idBici, descripcion);
				if (resultado != null)
					facesContext.addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									facesContext.getApplication().evaluateExpressionGet(facesContext,
											"#{text['incidenciaCreadaResumen']}", String.class),
									facesContext.getApplication().evaluateExpressionGet(facesContext,
											"#{text['incidenciaCreada']}", String.class) + " " + idBici));
				else
					facesContext.addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									facesContext.getApplication().evaluateExpressionGet(facesContext,
											"#{text['incidenciaErrorResumen']}", String.class),
									facesContext.getApplication().evaluateExpressionGet(facesContext,
											"#{text['incidenciaError']}", String.class) + " " + idBici));
			} catch (ServicioException e) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								facesContext.getApplication().evaluateExpressionGet(facesContext,
										"#{text['incidenciaErrorResumen']}", String.class),
								facesContext.getApplication().evaluateExpressionGet(facesContext,
										"#{text['incidenciaError']}", String.class) + " " + idBici));
				//e.printStackTrace();
			}
		}
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getIdBici() {
		return idBici;
	}

	public void setIdBici(String idBici) {
		this.idBici = idBici;
	}

}
