package dev.lin.helpdesk_software_api.dtos;

import java.time.LocalDateTime;

import dev.lin.helpdesk_software_api.Ticket.TicketStatus;

public record CombinedTicketDTO(
    Long id,
    String description,
    String requesterName,
    String attendeeName,
    TicketStatus status,
    LocalDateTime createdAt,
    LocalDateTime solvedAt
) {
}