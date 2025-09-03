package dev.lin.helpdesk_software_api.Ticket;

import dev.lin.helpdesk_software_api.Implementations.IGenericService;
import dev.lin.helpdesk_software_api.dtos.CombinedTicketDTO;
import dev.lin.helpdesk_software_api.dtos.TicketRequestDTO;
import dev.lin.helpdesk_software_api.dtos.TicketResponseDTO;

import java.util.List;

public interface TicketService extends IGenericService<TicketResponseDTO, TicketRequestDTO> {
    TicketResponseDTO updateTicketStatus(Long ticketId, TicketUpdateRequestDTO dtoRequest);
    List<CombinedTicketDTO> getAllCombinedTickets();
}