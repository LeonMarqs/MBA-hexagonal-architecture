package br.com.fullcycle.hexagonal.application.domain.customer;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import io.hypersistence.tsid.TSID;

public record CustomerId(String value) {

	public CustomerId {
		if (value == null) {
			throw new ValidationException("Invalid value for CustomerId");
		}
	}

	public static CustomerId unique() {
		return new CustomerId(TSID.fast().toString());
	}

	public static CustomerId with(final String value) {
		try {
			return new CustomerId(TSID.from(value).toString());
		} catch (IllegalArgumentException ex) {
			throw new ValidationException("Invalid value for CustomerId");
		}
	}

}
