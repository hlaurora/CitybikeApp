package alquileres.servicio;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ServicioTiempo implements IServicioTiempo {
	private Clock clock = Clock.systemDefaultZone();
	private ZoneId zoneId = ZoneId.systemDefault();

	public Clock getClock() {
		return clock;
	}

	public LocalDateTime now() {
		return LocalDateTime.now(getClock());
	}

	public void setFixedClockAt(LocalDateTime date) {
		this.clock = Clock.fixed(date.atZone(zoneId).toInstant(), zoneId);
	}

	public void resetSystemTime() {
		clock = Clock.systemDefaultZone();
	}
}
