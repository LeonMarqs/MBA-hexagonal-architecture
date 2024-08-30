package br.com.fullcycle.hexagonal.application.domain.event;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import io.hypersistence.tsid.TSID;

public record EventId(String value) {

	public EventId {
		if (value == null) {
			throw new ValidationException("Invalid value for EventId");
		}
	}

	public static EventId unique() {
		return new EventId(TSID.fast().toString());
	}

	public static EventId with(final String value) {
		try {
			return new EventId(TSID.from(value).toString());
		} catch (IllegalArgumentException ex) {
			throw new ValidationException("Invalid value for EventId");
		}
	}

}
