package br.com.fullcycle.hexagonal.application.usecases;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.models.Customer;
import br.com.fullcycle.hexagonal.services.CustomerService;

public class CreateCustomerUseCaseTest {
	@Test
	@DisplayName("Deve criar um cliente")
	public void testCreateCustomer() {
		// given
		final String expectedCPF = "1234567891";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe";

		final CreateCustomerUseCase.Input createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail,
				expectedName);

		// when
		final var customerService = Mockito.mock(CustomerService.class);
		when(customerService.findByCpf(expectedCPF)).thenReturn(Optional.empty());
		when(customerService.findByEmail(expectedEmail)).thenReturn(Optional.empty());
		when(customerService.save(any())).then(a -> {
			var customer = a.getArgument(0, Customer.class);
			customer.setId(UUID.randomUUID().getMostSignificantBits());
			return customer;
		});
		final var useCase = new CreateCustomerUseCase(customerService);
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

		final CreateCustomerUseCase.Input createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail,
				expectedName);

		final var aCustomer = new Customer();
		aCustomer.setId(UUID.randomUUID().getMostSignificantBits());
		aCustomer.setCpf(expectedCPF);
		aCustomer.setName(expectedName);
		aCustomer.setEmail(expectedEmail);

		// when
		final var customerService = Mockito.mock(CustomerService.class);
		when(customerService.findByCpf(expectedCPF)).thenReturn(Optional.of(aCustomer));

		final var useCase = new CreateCustomerUseCase(customerService);
		final var actualException = Assertions.assertThrows(ValidationException.class,
				() -> useCase.execute(createInput));

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

		final CreateCustomerUseCase.Input createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail,
				expectedName);

		final var aCustomer = new Customer();
		aCustomer.setId(UUID.randomUUID().getMostSignificantBits());
		aCustomer.setCpf(expectedCPF);
		aCustomer.setName(expectedName);
		aCustomer.setEmail(expectedEmail);

		// when
		final var customerService = Mockito.mock(CustomerService.class);
		when(customerService.findByEmail(expectedEmail)).thenReturn(Optional.of(aCustomer));

		final var useCase = new CreateCustomerUseCase(customerService);
		final var actualException = Assertions.assertThrows(ValidationException.class,
				() -> useCase.execute(createInput));

		// then
		Assertions.assertEquals(expectedError, actualException.getMessage());
	}

}
