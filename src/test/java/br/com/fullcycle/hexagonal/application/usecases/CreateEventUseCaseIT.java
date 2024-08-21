package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infraestructure.models.Partner;
import br.com.fullcycle.hexagonal.infraestructure.repositories.EventRepository;
import br.com.fullcycle.hexagonal.infraestructure.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.infraestructure.services.EventService;
import br.com.fullcycle.hexagonal.infraestructure.services.PartnerService;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CreateEventUseCaseIT extends IntegrationTest {

	@Autowired
	private EventService eventService;

	@Autowired
	private PartnerRepository partnerRepository;

	@Autowired
	private PartnerService partnerService;

	@Autowired
	private CreateEventUseCase useCase;

	@Autowired
	private EventRepository eventRepository;

	@BeforeEach
	@AfterEach
	void tearDown() {
		eventRepository.deleteAll();
		partnerRepository.deleteAll();
	}

	@Test
	@DisplayName("Deve criar um evento")
	public void testCreateEvent() {
		// given
		final var expectedName = "Disney on Ice";
		final var expectedDate = "2021-01-01";
		final var expectedTotalSpots = 100;

		final var partner = createPartner();
		final var expectedPartnerId = partner.getId();

		final var createInput = new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId, expectedTotalSpots);

		// when
		final var output = useCase.execute(createInput);

		// then
		Assertions.assertNotNull(output.id());
		Assertions.assertEquals(expectedDate, output.date());
		Assertions.assertEquals(expectedName, output.name());
		Assertions.assertEquals(expectedTotalSpots, output.totalSpots());
		Assertions.assertEquals(expectedPartnerId, output.partnerId());
	}

	@Test
	@DisplayName("Não deve criar um evento quando o Partner não for encontrado")
	public void testCreateEvent_whenPartnerNotFoundThrowError() {
		// given
		final var expectedName = "Disney on Ice";
		final var expectedDate = "2021-01-01";
		final var expectedTotalSpots = 100;
		final var expectedPartnerId = TSID.fast().toLong();
		final var expectedError = "Partner not found";

		final var createInput = new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId, expectedTotalSpots);

		// when
		final var useCase = new CreateEventUseCase(eventService, partnerService);
		final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

		// then
		Assertions.assertEquals(expectedError, actualException.getMessage());
	}

	private Partner createPartner() {
		final var partner = new Partner();
		partner.setCnpj("1234567891");
		partner.setEmail("partner@gmail.com");
		partner.setName("Partner");
		return partnerRepository.save(partner);
	}

}
