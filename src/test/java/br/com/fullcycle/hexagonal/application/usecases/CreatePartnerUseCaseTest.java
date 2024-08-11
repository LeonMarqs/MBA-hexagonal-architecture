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
import br.com.fullcycle.hexagonal.models.Partner;
import br.com.fullcycle.hexagonal.services.PartnerService;

public class CreatePartnerUseCaseTest {
	@Test
	@DisplayName("Deve criar um parceiro")
	public void testCreatePartner() {
		// given
		final String expectedCNPJ = "1234567891";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe LTDA";

		final CreatePartnerUseCase.Input createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail,
				expectedName);

		// when
		final var customerService = Mockito.mock(PartnerService.class);
		when(customerService.findByCnpj(expectedCNPJ)).thenReturn(Optional.empty());
		when(customerService.findByEmail(expectedEmail)).thenReturn(Optional.empty());
		when(customerService.save(any())).then(a -> {
			var customer = a.getArgument(0, Partner.class);
			customer.setId(UUID.randomUUID().getMostSignificantBits());
			return customer;
		});
		final var useCase = new CreatePartnerUseCase(customerService);
		final var output = useCase.execute(createInput);

		// then
		Assertions.assertNotNull(output.id());
		Assertions.assertEquals(expectedCNPJ, output.cnpj());
		Assertions.assertEquals(expectedEmail, output.email());
		Assertions.assertEquals(expectedName, output.name());
	}

	@Test
	@DisplayName("Não deve cadastrar um parceiro com CNPJ duplicado")
	public void testCreateWithDuplicatedCNPJShouldFail() {
		// given
		final String expectedCNPJ = "1234567891";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe";
		final String expectedError = "Partner already exists";

		final CreatePartnerUseCase.Input createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail,
				expectedName);

		final var aPartner = new Partner();
		aPartner.setId(UUID.randomUUID().getMostSignificantBits());
		aPartner.setCnpj(expectedCNPJ);
		aPartner.setName(expectedName);
		aPartner.setEmail(expectedEmail);

		// when
		final var customerService = Mockito.mock(PartnerService.class);
		when(customerService.findByCnpj(expectedCNPJ)).thenReturn(Optional.of(aPartner));

		final var useCase = new CreatePartnerUseCase(customerService);
		final var actualException = Assertions.assertThrows(ValidationException.class,
				() -> useCase.execute(createInput));

		// then
		Assertions.assertEquals(expectedError, actualException.getMessage());
	}

	@Test
	@DisplayName("Não deve cadastrar um parceiro com e-mail duplicado")
	public void testCreateWithDuplicatedEmailShouldFail() {
		// given
		final String expectedCNPJ = "1234567891";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe";
		final String expectedError = "Partner already exists";

		final CreatePartnerUseCase.Input createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail,
				expectedName);

		final var aPartner = new Partner();
		aPartner.setId(UUID.randomUUID().getMostSignificantBits());
		aPartner.setCnpj(expectedCNPJ);
		aPartner.setName(expectedName);
		aPartner.setEmail(expectedEmail);

		// when
		final var customerService = Mockito.mock(PartnerService.class);
		when(customerService.findByEmail(expectedEmail)).thenReturn(Optional.of(aPartner));

		final var useCase = new CreatePartnerUseCase(customerService);
		final var actualException = Assertions.assertThrows(ValidationException.class,
				() -> useCase.execute(createInput));

		// then
		Assertions.assertEquals(expectedError, actualException.getMessage());
	}

}
