package br.com.fullcycle.hexagonal.infrastructure.jpa.repositories;

import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.TicketEntity;
import io.hypersistence.tsid.TSID;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TicketJpaRepository extends CrudRepository<TicketEntity, TSID> {

	Optional<TicketEntity> findByEventIdAndCustomerId(TSID id, TSID customerId);

}
