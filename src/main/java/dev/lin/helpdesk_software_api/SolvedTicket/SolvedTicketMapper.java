package dev.lin.helpdesk_software_api.SolvedTicket;

import dev.lin.helpdesk_software_api.Ticket.TicketMapper;
import dev.lin.helpdesk_software_api.dtos.SolvedTicketResponseDTO;

import org.springframework.stereotype.Component;

@Component
public class SolvedTicketMapper {

    private final TicketMapper ticketMapper;

    public SolvedTicketMapper(TicketMapper ticketMapper) {
        this.ticketMapper = ticketMapper;
    }

    public SolvedTicketResponseDTO toDTO(SolvedTicketEntity solvedTicket) {
        if (solvedTicket == null) {
            return null;
        }

        return new SolvedTicketResponseDTO(
            solvedTicket.getId(),
            ticketMapper.toDTO(solvedTicket.getTicket()),
            solvedTicket.getAttendee().getId(),
            solvedTicket.getSolvedAt()
        );
    }
}