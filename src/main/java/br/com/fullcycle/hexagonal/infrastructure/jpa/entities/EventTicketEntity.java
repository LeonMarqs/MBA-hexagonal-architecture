package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.event.EventTicket;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketId;
import io.hypersistence.tsid.TSID;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity(name = "EventTicket")
@Table(name = "events_tickets")
public class EventTicketEntity {

	@Id
	private TSID ticketId;

	private TSID customerId;

	private int ordering;

	@ManyToOne(fetch = FetchType.LAZY)
	private EventEntity event;

	public EventTicketEntity() {
	}

	public EventTicketEntity(final TSID ticketId, final TSID customerId, final int ordering, final EventEntity event) {
		this.ticketId = ticketId;
		this.customerId = customerId;
		this.event = event;
		this.ordering = ordering;
	}

	public static EventTicketEntity of(final EventEntity event, final EventTicket ev) {
		return new EventTicketEntity(TSID.from(ev.ticketId().value()), TSID.from(ev.customerId().value()),
				ev.ordering(), event);
	}

	public EventTicket toEventTicket() {
		return new EventTicket(TicketId.with(this.ticketId.toString()), EventId.with(this.event.id().toString()),
				CustomerId.with(this.customerId.toString()), this.ordering);
	}

	public TSID ticketId() {
		return ticketId;
	}

	public void setTicketId(TSID ticketId) {
		this.ticketId = ticketId;
	}

	public TSID customerId() {
		return customerId;
	}

	public void setCustomerId(TSID customerId) {
		this.customerId = customerId;
	}

	public int ordering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}

	public EventEntity event() {
		return event;
	}

	public void setEvent(EventEntity event) {
		this.event = event;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EventTicketEntity that = (EventTicketEntity) o;
		return ordering == that.ordering && Objects.equals(ticketId, that.ticketId) && Objects.equals(customerId, that.customerId)
				&& Objects.equals(event, that.event);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ticketId, customerId, ordering, event);
	}

}
