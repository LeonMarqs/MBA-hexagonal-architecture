package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.infraestructure.models.Customer;
import br.com.fullcycle.hexagonal.infraestructure.repositories.CustomerRepository;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GetCustomerByIdUseCaseIT extends IntegrationTest {

	@Autowired
	private GetCustomerByIdUseCase useCase;

	@Autowired
	private CustomerRepository customerRepository;

	@BeforeEach
	void tearDown() {
		customerRepository.deleteAll();
	}

	@Test
	@DisplayName("Deve buscar um cliente por id")
	public void testGetCustomerById() {
		// given
		final String expectedCPF = "1234567891";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe";

		final var customer = createCustomer();

		final GetCustomerByIdUseCase.Input getByIdInput = new GetCustomerByIdUseCase.Input(customer.getId().toString());

		// when
		final var output = useCase.execute(getByIdInput).get();

		// then
		Assertions.assertEquals(customer.getId(), output.id());
		Assertions.assertEquals(expectedCPF, output.cpf());
		Assertions.assertEquals(expectedEmail, output.email());
		Assertions.assertEquals(expectedName, output.name());
	}

	@Test
	@DisplayName("Deve obter vazio ao buscar por id inválido")
	public void testGetCustomerByIdWithInvalidId() {
		// given
		final String expectedId = TSID.fast().toString();

		final GetCustomerByIdUseCase.Input getByIdInput = new GetCustomerByIdUseCase.Input(expectedId);

		// when
		final var output = useCase.execute(getByIdInput);

		// then
		Assertions.assertTrue(output.isEmpty());
	}

	private Customer createCustomer() {
		final var customer = new Customer();
		customer.setCpf("1234567891");
		customer.setEmail("john.doe@gmal.com");
		customer.setName("John Doe");
		return customerRepository.save(customer);
	}

}
