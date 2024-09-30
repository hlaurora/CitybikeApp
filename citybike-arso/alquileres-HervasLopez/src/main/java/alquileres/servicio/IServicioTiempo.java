package alquileres.servicio;

import java.time.LocalDateTime;

public interface IServicioTiempo {
	LocalDateTime now();

	void setFixedClockAt(LocalDateTime date);

	void resetSystemTime();
}
