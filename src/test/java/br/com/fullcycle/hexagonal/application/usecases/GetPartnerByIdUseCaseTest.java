package br.com.fullcycle.hexagonal.application.usecases;

import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.com.fullcycle.hexagonal.models.Partner;
import br.com.fullcycle.hexagonal.services.PartnerService;

class GetPartnerByIdUseCaseTest {

	@Test
	@DisplayName("Deve buscar um parceiro por id")
	public void testGetPartnerById() {
		// given
		final Long expectedId = UUID.randomUUID().getMostSignificantBits();
		final String expectedCNPJ = "1234567891";
		final String expectedEmail = "john.doe@gmal.com";
		final String expectedName = "John Doe";

		final GetPartnerByIdUseCase.Input getByIdInput = new GetPartnerByIdUseCase.Input(expectedId);

		final var aPartner = new Partner();
		aPartner.setId(expectedId);
		aPartner.setCnpj(expectedCNPJ);
		aPartner.setName(expectedName);
		aPartner.setEmail(expectedEmail);

		// when
		final var partnerService = Mockito.mock(PartnerService.class);
		when(partnerService.findById(expectedId)).thenReturn(Optional.of(aPartner));

		final var useCase = new GetPartnerByIdUseCase(partnerService);
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
		final Long expectedId = UUID.randomUUID().getMostSignificantBits();

		final GetPartnerByIdUseCase.Input getByIdInput = new GetPartnerByIdUseCase.Input(expectedId);

		// when
		final var partnerService = Mockito.mock(PartnerService.class);
		when(partnerService.findById(expectedId)).thenReturn(Optional.empty());

		final var useCase = new GetPartnerByIdUseCase(partnerService);
		final var output = useCase.execute(getByIdInput);

		// then
		Assertions.assertTrue(output.isEmpty());
	}

}
