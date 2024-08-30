package br.com.fullcycle.hexagonal.application.domain.event.ticket;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import io.hypersistence.tsid.TSID;

public record TicketId(String value) {

	public TicketId {
		if (value == null) {
			throw new ValidationException("Invalid value for TicketId");
		}
	}

	public static TicketId unique() {
		return new TicketId(TSID.fast().toString());
	}

	public static TicketId with(final String value) {
		try {
			return new TicketId(TSID.from(value).toString());
		} catch (IllegalArgumentException ex) {
			throw new ValidationException("Invalid value for TicketId");
		}
	}

}
