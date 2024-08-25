package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.person.Cpf;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.application.usecases.CreateCustomerUseCase.Input;
import br.com.fullcycle.hexagonal.application.usecases.CreateCustomerUseCase.Output;

import java.util.Objects;

public class CreateCustomerUseCase extends UseCase<Input, Output> {

	private final CustomerRepository customerRepository;

	public CreateCustomerUseCase(final CustomerRepository customerRepository) {
		this.customerRepository = Objects.requireNonNull(customerRepository);
	}

	@Override
	public Output execute(final Input input) {
		if (customerRepository.customerOfCPF(new Cpf(input.cpf())).isPresent()) {
			throw new ValidationException("Customer already exists");
		}

		if (customerRepository.customerOfEmail(new Email(input.email())).isPresent()) {
			throw new ValidationException("Customer already exists");
		}

		var customer = Customer.newCustomer(input.name(), input.cpf(), input.email());

		customer = customerRepository.create(customer);

		return new Output(customer.customerId().value(), customer.cpf().value(), customer.email().value(),
				customer.name().value());
	}

	public record Input(String cpf, String email, String name) {

	}

	public record Output(String id, String cpf, String email, String name) {

	}

}
