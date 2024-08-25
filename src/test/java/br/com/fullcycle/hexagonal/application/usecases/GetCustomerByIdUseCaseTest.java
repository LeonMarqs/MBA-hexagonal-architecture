package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.repository.InMemoryCustomerRepository;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GetCustomerByIdUseCaseTest {

	@Test
	@DisplayName("Deve buscar um cliente por id")
	public void testGetCustomerById() {
		// given
		final String expectedCPF = "123.456.789-01";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe";

		// when
		final var customerRepository = new InMemoryCustomerRepository();
		final var customer = customerRepository.create(Customer.newCustomer(expectedName, expectedCPF, expectedEmail));

		final var expectedId = customer.customerId().value();
		final var getByIdInput = new GetCustomerByIdUseCase.Input(expectedId);
		final var useCase = new GetCustomerByIdUseCase(customerRepository);
		final var output = useCase.execute(getByIdInput).get();

		// then
		Assertions.assertEquals(expectedId, output.id());
		Assertions.assertEquals(expectedCPF, output.cpf());
		Assertions.assertEquals(expectedEmail, output.email());
		Assertions.assertEquals(expectedName, output.name());
	}

	@Test
	@DisplayName("Deve obter vazio ao buscar por id inválido")
	public void testGetCustomerByIdWithInvalidId() {
		// given

		final GetCustomerByIdUseCase.Input getByIdInput = new GetCustomerByIdUseCase.Input(TSID.fast().toString());

		// when
		final var customerRepository = new InMemoryCustomerRepository();

		final var useCase = new GetCustomerByIdUseCase(customerRepository);
		final var output = useCase.execute(getByIdInput);

		// then
		Assertions.assertTrue(output.isEmpty());
	}

}
