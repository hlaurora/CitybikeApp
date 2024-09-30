package estaciones.web.buscadorBicis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import estaciones.dto.BicicletaDTO;
import estaciones.servicio.IServicioEstaciones;
import servicio.FactoriaServicios;
import servicio.ServicioException;

@Named
@ViewScoped
public class BuscadorBicisWeb implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Double latitud;
    private Double longitud;
    private List<BicicletaDTO> bicicletas = new ArrayList<BicicletaDTO>();
    
	@Inject
	protected FacesContext facesContext;
    
    private IServicioEstaciones servicioEstaciones;  
    
    public BuscadorBicisWeb() {       
    	servicioEstaciones = FactoriaServicios.getServicio(IServicioEstaciones.class);   }
    
    public void buscarBicis() {             
    	if (latitud==null || longitud==null)
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							facesContext.getApplication().evaluateExpressionGet(facesContext,
									"#{text['error']}", String.class),
							facesContext.getApplication().evaluateExpressionGet(facesContext, "#{text['errorCoordenadas']}",
									String.class)));
		else {
    	try {
            bicicletas = servicioEstaciones.getBicicletasCercanasDTO(latitud, longitud);
        } catch (ServicioException e) {
            e.printStackTrace();
        }     
		}
    }
    
    
    public Double getLatitud() {
        return latitud;
    }
    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }
    public Double getLongitud() {
        return longitud;
    }
    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
    
    public List<BicicletaDTO> getBicis() {
    	return bicicletas;
	}

}
