package dev.lin.helpdesk_software_api.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import dev.lin.helpdesk_software_api.Ticket.TicketStatus;

public record TicketResponseDTO(
    Long id,
    Long requesterId,
    Long subjectId,
    String description,
    TicketStatus status,
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime createdAt,
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime updatedAt
) {}