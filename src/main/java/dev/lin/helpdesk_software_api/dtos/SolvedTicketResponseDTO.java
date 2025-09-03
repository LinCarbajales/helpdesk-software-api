package dev.lin.helpdesk_software_api.dtos;

import java.time.LocalDateTime;

public record SolvedTicketResponseDTO(
    Long id,
    TicketResponseDTO ticket,
    Long attendeeId,
    LocalDateTime solvedAt
) {}