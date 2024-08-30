package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketId;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketStatus;
import io.hypersistence.tsid.TSID;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Objects;

@Entity(name = "Ticket")
@Table(name = "tickets")
public class TicketEntity {

	@Id
	private TSID id;

	private TSID customerId;

	private TSID eventId;

	@Enumerated(EnumType.STRING)
	private TicketStatus status;

	private Instant paidAt;

	private Instant reservedAt;

	public TicketEntity() {
	}

	public TicketEntity(final TSID id, final TSID customerId, final TSID eventId, final TicketStatus status, final Instant paidAt,
			final Instant reservedAt) {
		this.id = id;
		this.customerId = customerId;
		this.eventId = eventId;
		this.status = status;
		this.paidAt = paidAt;
		this.reservedAt = reservedAt;
	}

	public static TicketEntity of(final Ticket ticket) {
		return new TicketEntity(TSID.from(ticket.ticketId().value()), TSID.from(ticket.customerId().value()),
				TSID.from(ticket.eventId().value()), ticket.status(), ticket.paidAt(), ticket.reservedAt());
	}

	public Ticket toTicket() {
		return new Ticket(TicketId.with(this.id.toString()), CustomerId.with(this.customerId.toString()),
				EventId.with(this.eventId.toString()), this.status, this.paidAt, this.reservedAt);
	}

	public TSID getId() {
		return id;
	}

	public void setId(TSID id) {
		this.id = id;
	}

	public TSID customerId() {
		return customerId;
	}

	public void setCustomerId(TSID customerId) {
		this.customerId = customerId;
	}

	public TSID eventId() {
		return eventId;
	}

	public void setEventId(TSID eventId) {
		this.eventId = eventId;
	}

	public TicketStatus getStatus() {
		return status;
	}

	public void setStatus(TicketStatus status) {
		this.status = status;
	}

	public Instant getPaidAt() {
		return paidAt;
	}

	public void setPaidAt(Instant paidAt) {
		this.paidAt = paidAt;
	}

	public Instant getReservedAt() {
		return reservedAt;
	}

	public void setReservedAt(Instant reservedAt) {
		this.reservedAt = reservedAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TicketEntity that = (TicketEntity) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
