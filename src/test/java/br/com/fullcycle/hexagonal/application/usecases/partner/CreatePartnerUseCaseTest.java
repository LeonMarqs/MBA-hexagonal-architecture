package br.com.fullcycle.hexagonal.application.usecases.partner;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repository.InMemoryPartnerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreatePartnerUseCaseTest {

	@Test
	@DisplayName("Deve criar um parceiro")
	public void testCreatePartner() {
		// given
		final String expectedCNPJ = "62.674.971/0001-74";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe LTDA";

		final CreatePartnerUseCase.Input createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

		// when
		final var partnerRepository = new InMemoryPartnerRepository();

		final var useCase = new CreatePartnerUseCase(partnerRepository);
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
		final String expectedCNPJ = "62.674.971/0001-74";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe LTDA";
		final String expectedError = "Partner already exists";

		final var customerRepository = new InMemoryPartnerRepository();
		customerRepository.create(Partner.newPartner(expectedName, expectedCNPJ, "email2@gmail.com"));

		final CreatePartnerUseCase.Input createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

		// when
		final var useCase = new CreatePartnerUseCase(customerRepository);
		final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

		// then
		Assertions.assertEquals(expectedError, actualException.getMessage());
	}

	@Test
	@DisplayName("Não deve cadastrar um parceiro com e-mail duplicado")
	public void testCreateWithDuplicatedEmailShouldFail() {
		// given
		final String expectedCNPJ = "91.857.754/0001-18";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe LTDA";
		final String expectedError = "Partner already exists";

		final var customerRepository = new InMemoryPartnerRepository();
		customerRepository.create(Partner.newPartner(expectedName, expectedCNPJ, expectedEmail));

		final CreatePartnerUseCase.Input createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

		// when
		final var useCase = new CreatePartnerUseCase(customerRepository);
		final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

		// then
		Assertions.assertEquals(expectedError, actualException.getMessage());
	}

}
