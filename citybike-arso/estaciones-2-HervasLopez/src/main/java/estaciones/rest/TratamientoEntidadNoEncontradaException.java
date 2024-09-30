package estaciones.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import repositorio.EntidadNoEncontrada;

@ControllerAdvice
public class TratamientoEntidadNoEncontradaException {
	@ExceptionHandler(EntidadNoEncontrada.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public RespuestaError handleGlobalException(EntidadNoEncontrada ex) {
		return new RespuestaError("Not Found", ex.getMessage());
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
