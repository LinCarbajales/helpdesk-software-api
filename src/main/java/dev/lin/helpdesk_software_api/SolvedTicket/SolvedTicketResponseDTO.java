package dev.lin.helpdesk_software_api.SolvedTicket;

import dev.lin.helpdesk_software_api.Ticket.TicketResponseDTO;
import java.time.LocalDateTime;

public record SolvedTicketResponseDTO(
    Long id,
    TicketResponseDTO ticket,
    Long attendeeId,
    LocalDateTime solvedAt
) {}