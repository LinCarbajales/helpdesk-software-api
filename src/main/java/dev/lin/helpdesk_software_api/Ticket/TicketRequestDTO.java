package dev.lin.helpdesk_software_api.Ticket;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TicketRequestDTO(
    @NotNull(message = "Requester ID cannot be null.")
    Long requesterId,
    
    @NotNull(message = "Subject ID cannot be null.")
    @Min(value = 1, message = "Subject ID must be between 1 and 8.")
    @Max(value = 8, message = "Subject ID must be between 1 and 8.")
    Long subjectId,
    
    @NotBlank(message = "Description cannot be blank.")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters.")
    String description
) {}