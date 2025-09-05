package dev.lin.helpdesk_software_api.Ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.lin.helpdesk_software_api.dtos.CombinedTicketDTO;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    List<TicketEntity> findAllByOrderByCreatedAtAsc();

    @Query("SELECT NEW dev.lin.helpdesk_software_api.dtos.CombinedTicketDTO(t.id, t.description, t.requester.name, a.name, t.status, t.createdAt, s.solvedAt) " +
           "FROM TicketEntity t " +
           "LEFT JOIN t.requester r " +
           "LEFT JOIN SolvedTicketEntity s ON s.ticket = t " +
           "LEFT JOIN s.attendee a")
    List<CombinedTicketDTO> findAllCombinedTickets();

}