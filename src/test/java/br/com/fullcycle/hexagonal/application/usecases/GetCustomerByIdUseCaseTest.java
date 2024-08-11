package br.com.fullcycle.hexagonal.application.usecases;

import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.com.fullcycle.hexagonal.models.Customer;
import br.com.fullcycle.hexagonal.services.CustomerService;

class GetCustomerByIdUseCaseTest {

	@Test
	@DisplayName("Deve buscar um cliente por id")
	public void testGetCustomerById() {
		// given
		final Long expectedId = UUID.randomUUID().getMostSignificantBits();
		final String expectedCPF = "1234567891";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe";

		final GetCustomerByIdUseCase.Input getByIdInput = new GetCustomerByIdUseCase.Input(expectedId);

		final var aCustomer = new Customer();
		aCustomer.setId(expectedId);
		aCustomer.setCpf(expectedCPF);
		aCustomer.setName(expectedName);
		aCustomer.setEmail(expectedEmail);

		// when
		final var customerService = Mockito.mock(CustomerService.class);
		when(customerService.findById(expectedId)).thenReturn(Optional.of(aCustomer));

		final var useCase = new GetCustomerByIdUseCase(customerService);
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
		final Long expectedId = UUID.randomUUID().getMostSignificantBits();

		final GetCustomerByIdUseCase.Input getByIdInput = new GetCustomerByIdUseCase.Input(expectedId);

		// when
		final var customerService = Mockito.mock(CustomerService.class);
		when(customerService.findById(expectedId)).thenReturn(Optional.empty());

		final var useCase = new GetCustomerByIdUseCase(customerService);
		final var output = useCase.execute(getByIdInput);

		// then
		Assertions.assertTrue(output.isEmpty());
	}

}
