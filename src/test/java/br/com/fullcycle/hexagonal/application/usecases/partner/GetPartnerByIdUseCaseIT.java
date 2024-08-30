package br.com.fullcycle.hexagonal.application.usecases.partner;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.repository.InMemoryPartnerRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.PartnerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.PartnerJpaRepository;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GetPartnerByIdUseCaseIT extends IntegrationTest {

	@Autowired
	private GetPartnerByIdUseCase useCase;

	@Autowired
	private PartnerJpaRepository partnerRepository;

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
		final GetPartnerByIdUseCase.Input getByIdInput = new GetPartnerByIdUseCase.Input(aPartner.getId().toString());

		// when
		final var partnerRepository = new InMemoryPartnerRepository();

		final var useCase = new GetPartnerByIdUseCase(partnerRepository);
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
		final TSID expectedId = TSID.fast();

		final GetPartnerByIdUseCase.Input getByIdInput = new GetPartnerByIdUseCase.Input(expectedId.toString());

		// when
		final var output = useCase.execute(getByIdInput);

		// then
		Assertions.assertTrue(output.isEmpty());
	}

	private PartnerEntity createPartner() {
		final var aPartner = new PartnerEntity();
		aPartner.setCnpj("1234567891");
		aPartner.setName("John Doe LTDA");
		aPartner.setEmail("johndoe@gmail.conm");
		return partnerRepository.save(aPartner);
	}

}
