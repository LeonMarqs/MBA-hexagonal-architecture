package br.com.fullcycle.hexagonal.application.usecases.partner;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.PartnerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.PartnerJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CreatePartnerUseCaseIT extends IntegrationTest {

	@Autowired
	private CreatePartnerUseCase useCase;

	@Autowired
	private PartnerJpaRepository partnerRepository;

	@BeforeEach
	void tearDown() {
		partnerRepository.deleteAll();
	}

	@Test
	@DisplayName("Deve criar um parceiro")
	public void testCreatePartner() {
		// given
		final String expectedCNPJ = "62.674.971/0001-74";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe LTDA";

		final CreatePartnerUseCase.Input createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

		// when
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
		final String expectedName = "John Doe";
		final String expectedError = "Partner already exists";

		final CreatePartnerUseCase.Input createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

		createPartner(expectedCNPJ, "email@gmail.com");

		// when
		final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

		// then
		Assertions.assertEquals(expectedError, actualException.getMessage());
	}

	@Test
	@DisplayName("Não deve cadastrar um parceiro com e-mail duplicado")
	public void testCreateWithDuplicatedEmailShouldFail() {
		// given
		final String expectedCNPJ = "62.674.971/0001-74";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe";
		final String expectedError = "Partner already exists";

		final CreatePartnerUseCase.Input createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

		createPartner("1234567892", expectedEmail);

		// when
		final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

		// then
		Assertions.assertEquals(expectedError, actualException.getMessage());
	}

	private void createPartner(final String cnpj, final String email) {
		final var aPartner = new PartnerEntity();
		aPartner.setCnpj(cnpj);
		aPartner.setName("John Doe");
		aPartner.setEmail(email);

		partnerRepository.save(aPartner);
	}

}
