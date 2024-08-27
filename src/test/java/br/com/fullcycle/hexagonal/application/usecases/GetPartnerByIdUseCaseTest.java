package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.repository.InMemoryPartnerRepository;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GetPartnerByIdUseCaseTest {

	@Test
	@DisplayName("Deve buscar um parceiro por id")
	public void testGetPartnerById() {
		// given
		final String expectedCNPJ = "91.857.754/0001-18";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe LTDA";

		final var partnerRepository = new InMemoryPartnerRepository();
		final var partner = partnerRepository.create(Partner.newPartner(expectedName, expectedCNPJ, expectedEmail));
		final var expectedId = partner.partnerId().value();

		final GetPartnerByIdUseCase.Input getByIdInput = new GetPartnerByIdUseCase.Input(expectedId);

		// when
		final var useCase = new GetPartnerByIdUseCase(partnerRepository);
		final var output = useCase.execute(getByIdInput).get();

		// then
		Assertions.assertEquals(expectedId, output.id());
		Assertions.assertEquals(expectedCNPJ, output.cnpj());
		Assertions.assertEquals(expectedEmail, output.email());
		Assertions.assertEquals(expectedName, output.name());
	}

	@Test
	@DisplayName("Deve obter vazio ao buscar por id inválido")
	public void testGetPartnerByIdWithInvalidId() {
		// given
		final GetPartnerByIdUseCase.Input getByIdInput = new GetPartnerByIdUseCase.Input(TSID.fast().toString());

		// when
		final var partnerRepository = new InMemoryPartnerRepository();

		final var useCase = new GetPartnerByIdUseCase(partnerRepository);
		final var output = useCase.execute(getByIdInput);

		// then
		Assertions.assertTrue(output.isEmpty());
	}

}
