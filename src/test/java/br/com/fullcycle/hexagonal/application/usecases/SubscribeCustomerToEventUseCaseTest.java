package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.infraestructure.models.Customer;
import br.com.fullcycle.hexagonal.infraestructure.models.Event;
import br.com.fullcycle.hexagonal.infraestructure.models.TicketStatus;
import br.com.fullcycle.hexagonal.infraestructure.services.CustomerService;
import br.com.fullcycle.hexagonal.infraestructure.services.EventService;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SubscribeCustomerToEventUseCaseTest {

	@Test
	@DisplayName("Deve comprar um ticket de um evento")
	public void testReserveTicket() {
		final long customerID = TSID.fast().toLong();
		final long eventID = TSID.fast().toLong();
		final int expectedTicketsSize = 1;

		final var aEvent = new Event();
		aEvent.setId(eventID);
		aEvent.setName("Disney on Ice");
		aEvent.setTotalSpots(100);

		final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(customerID, aEvent.getId());

		// when
		final var customerService = mock(CustomerService.class);
		final var eventService = mock(EventService.class);

		when(customerService.findById(customerID)).thenReturn(Optional.of(new Customer()));
		when(eventService.findById(eventID)).thenReturn(Optional.of(aEvent));
		when(eventService.findTicketByEventIdAndCustomerId(eventID, customerID)).thenReturn(Optional.empty());
		when(eventService.save(any())).thenAnswer(a -> {
			final var arg = a.getArgument(0, Event.class);
			Assertions.assertEquals(expectedTicketsSize, aEvent.getTickets().size());
			return arg;
		});

		final var useCase = new SubscribeCustomerToEventUseCase(customerService, eventService);
		final var output = useCase.execute(subscribeInput);

		// then
		Assertions.assertEquals(eventID, output.eventId());
		Assertions.assertNotNull(output.reservationDate());
		Assertions.assertEquals(TicketStatus.PENDING.name(), output.ticketStatus());
	}

}
