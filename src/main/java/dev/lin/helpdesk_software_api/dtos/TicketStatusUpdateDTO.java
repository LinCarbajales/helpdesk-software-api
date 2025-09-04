package dev.lin.helpdesk_software_api.dtos;

import dev.lin.helpdesk_software_api.Ticket.TicketStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TicketStatusUpdateDTO(
    @NotNull(message = "New status cannot be null.")
    TicketStatus newStatus,

    @NotNull(message = "Attendee ID cannot be null.")
    @Min(value = 1, message = "Attendee ID must be greater than 0.")
    Long attendeeId
) {}