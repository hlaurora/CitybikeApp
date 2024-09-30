package estaciones.web;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class InicioSesionWeb implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;
	private String clave;

	@Inject
	protected FacesContext facesContext;

	public void login() {
		if ("cliente".equals(username) && "cliente".equals(clave)) {
			try {
				facesContext.getExternalContext().redirect("/cliente/home.xhtml");
			} catch (IOException e) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No se ha podido navegar"));
				e.printStackTrace();
			}
		} else if ("gestor".equals(username) && "gestor".equals(clave)) {
			try {
				facesContext.getExternalContext().redirect("/gestor/home.xhtml");
			} catch (IOException e) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No se ha podido navegar"));
				e.printStackTrace();
			}
		} else {
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							facesContext.getApplication().evaluateExpressionGet(facesContext,
									"#{text['errorInicioResumen']}", String.class),
							facesContext.getApplication().evaluateExpressionGet(facesContext, "#{text['errorInicio']}",
									String.class)));
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

}
