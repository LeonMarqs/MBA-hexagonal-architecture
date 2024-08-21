package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infraestructure.models.Customer;
import br.com.fullcycle.hexagonal.infraestructure.repositories.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateCustomerUseCaseIT extends IntegrationTest {

	@Autowired
	private CreateCustomerUseCase useCase;

	@Autowired
	private CustomerRepository customerRepository;

	@BeforeEach
	void tearDown() {
		customerRepository.deleteAll();
	}

	@Test
	@DisplayName("Deve criar um cliente")
	public void testCreateCustomer() {
		// given
		final String expectedCPF = "1234567891";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe";

		final CreateCustomerUseCase.Input createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

		// when

		final var output = useCase.execute(createInput);

		// then
		Assertions.assertNotNull(output.id());
		Assertions.assertEquals(expectedCPF, output.cpf());
		Assertions.assertEquals(expectedEmail, output.email());
		Assertions.assertEquals(expectedName, output.name());
	}

	@Test
	@DisplayName("Não deve cadastrar um cliente com CPF duplicado")
	public void testCreateWithDuplicatedCPFShouldFail() {
		// given
		final String expectedCPF = "1234567891";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe";
		final String expectedError = "Customer already exists";

		createCustomer();

		final var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

		// when
		final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

		// then
		Assertions.assertEquals(expectedError, actualException.getMessage());
	}

	@Test
	@DisplayName("Não deve cadastrar um cliente com e-mail duplicado")
	public void testCreateWithDuplicatedEmailShouldFail() {
		// given
		final String expectedCPF = "1234567891";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe";
		final String expectedError = "Customer already exists";

		createCustomer();

		final var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

		// when
		final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

		// then
		Assertions.assertEquals(expectedError, actualException.getMessage());
	}

	private void createCustomer() {
		final var aCustomer = new Customer();
		aCustomer.setCpf("1234567891");
		aCustomer.setEmail("john.doe@gmal.com");
		aCustomer.setName("John Doe");

		customerRepository.save(aCustomer);
	}

}
