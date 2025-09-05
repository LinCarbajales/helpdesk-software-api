package dev.lin.helpdesk_software_api.SolvedTicket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolvedTicketRepository extends JpaRepository<SolvedTicketEntity, Long> {
}