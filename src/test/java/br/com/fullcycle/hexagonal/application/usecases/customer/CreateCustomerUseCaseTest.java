package br.com.fullcycle.hexagonal.application.usecases.customer;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repository.InMemoryCustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreateCustomerUseCaseTest {

	@Test
	@DisplayName("Deve criar um cliente")
	public void testCreateCustomer() {
		// given
		final String expectedCPF = "123.456.789-01";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe";

		final CreateCustomerUseCase.Input createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

		// when
		final var customerRepository = new InMemoryCustomerRepository();
		final var useCase = new CreateCustomerUseCase(customerRepository);
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
		final String expectedCPF = "123.456.789-01";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe";
		final String expectedError = "Customer already exists";

		final var customerRepository = new InMemoryCustomerRepository();

		customerRepository.create(Customer.newCustomer(expectedName, expectedCPF, "emailteste@gmail.com"));

		final CreateCustomerUseCase.Input createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

		// when
		final var useCase = new CreateCustomerUseCase(customerRepository);
		final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

		// then
		Assertions.assertEquals(expectedError, actualException.getMessage());
	}

	@Test
	@DisplayName("Não deve cadastrar um cliente com e-mail duplicado")
	public void testCreateWithDuplicatedEmailShouldFail() {
		// given
		final String expectedCPF = "123.456.789-01";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe";
		final String expectedError = "Customer already exists";

		final var customerRepository = new InMemoryCustomerRepository();

		customerRepository.create(Customer.newCustomer(expectedName, "123.123.123-01", expectedEmail));

		final CreateCustomerUseCase.Input createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

		// when
		final var useCase = new CreateCustomerUseCase(customerRepository);
		final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

		// then
		Assertions.assertEquals(expectedError, actualException.getMessage());
	}

}
