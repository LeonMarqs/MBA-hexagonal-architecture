package br.com.fullcycle.hexagonal.application.domain.partner;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import io.hypersistence.tsid.TSID;

public record PartnerId(String value) {

	public PartnerId {
		if (value == null) {
			throw new ValidationException("Invalid value for PartnerId");
		}
	}

	public static PartnerId unique() {
		return new PartnerId(TSID.fast().toString());
	}

	public static PartnerId with(final String value) {
		try {
			return new PartnerId(TSID.from(value).toString());
		} catch (IllegalArgumentException ex) {
			throw new ValidationException("Invalid value for PartnerId");
		}
	}

}
