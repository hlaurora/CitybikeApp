package pasarela.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import servicio.ServicioException;

@ControllerAdvice
public class TratamientoServicioException {

	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RespuestaError handleGlobalException(ServicioException ex) {
		return new RespuestaError("Internal Server Error", ex.getMessage());
	}

	private static class RespuestaError {
		private String estado;
		private String mensaje;

		public RespuestaError(String estado, String mensaje) {
			this.estado = estado;
			this.mensaje = mensaje;
		}

	}
	
}
