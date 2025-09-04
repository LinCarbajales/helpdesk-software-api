package dev.lin.helpdesk_software_api.dtos;

public record TicketEditDTO(
    String description,
    Long requesterId,
    Long subjectId
) {
}