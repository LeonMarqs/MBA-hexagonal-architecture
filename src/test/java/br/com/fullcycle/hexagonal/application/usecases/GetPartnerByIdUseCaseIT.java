package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.infraestructure.models.Partner;
import br.com.fullcycle.hexagonal.infraestructure.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.infraestructure.services.PartnerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

class GetPartnerByIdUseCaseIT extends IntegrationTest {

	@Autowired
	private GetPartnerByIdUseCase useCase;

	@Autowired
	private PartnerRepository partnerRepository;

	@BeforeEach
	@AfterEach
	void tearDown() {
		partnerRepository.deleteAll();
	}

	@Test
	@DisplayName("Deve buscar um parceiro por id")
	public void testGetPartnerById() {
		// given
		final var aPartner = createPartner();
		final GetPartnerByIdUseCase.Input getByIdInput = new GetPartnerByIdUseCase.Input(aPartner.getId());

		// when
		final var partnerService = Mockito.mock(PartnerService.class);
		when(partnerService.findById(aPartner.getId())).thenReturn(Optional.of(aPartner));

		final var useCase = new GetPartnerByIdUseCase(partnerService);
		final var output = useCase.execute(getByIdInput).get();

		// then
		Assertions.assertEquals(aPartner.getId(), output.id());
		Assertions.assertEquals(aPartner.getCnpj(), output.cnpj());
		Assertions.assertEquals(aPartner.getEmail(), output.email());
		Assertions.assertEquals(aPartner.getName(), output.name());
	}

	@Test
	@DisplayName("Deve obter vazio ao buscar por id inválido")
	public void testGetPartnerByIdWithInvalidId() {
		// given
		final Long expectedId = UUID.randomUUID().getMostSignificantBits();

		final GetPartnerByIdUseCase.Input getByIdInput = new GetPartnerByIdUseCase.Input(expectedId);

		// when
		final var output = useCase.execute(getByIdInput);

		// then
		Assertions.assertTrue(output.isEmpty());
	}

	private Partner createPartner() {
		final var aPartner = new Partner();
		aPartner.setCnpj("1234567891");
		aPartner.setName("John Doe LTDA");
		aPartner.setEmail("johndoe@gmail.conm");
		return partnerRepository.save(aPartner);
	}

}
