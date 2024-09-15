package br.com.fullcycle.infrastructure.rest.presenters;

import br.com.fullcycle.application.Presenter;
import br.com.fullcycle.application.customer.GetCustomerByIdUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class GetCustomerByIdResponseEntity implements Presenter<Optional<GetCustomerByIdUseCase.Output>, Object> {

	private static final Logger LOG = LoggerFactory.getLogger(GetCustomerByIdResponseEntity.class);

	@Override
	public ResponseEntity<?> present(final Optional<GetCustomerByIdUseCase.Output> input) {
		return input.map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
	}

	@Override
	public ResponseEntity<?> present(final Throwable error) {
		LOG.error("An error was observed at GetCustomerByIdUseCase", error);
		return ResponseEntity.notFound().build();
	}

}
