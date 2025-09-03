package dev.lin.helpdesk_software_api.Ticket;

import dev.lin.helpdesk_software_api.Implementations.IGenericService;
import dev.lin.helpdesk_software_api.dtos.TicketRequestDTO;
import dev.lin.helpdesk_software_api.dtos.TicketResponseDTO;

public interface TicketService extends IGenericService<TicketResponseDTO, TicketRequestDTO> {
    TicketResponseDTO updateTicketStatus(Long ticketId, TicketUpdateRequestDTO dtoRequest);
}