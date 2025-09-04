package dev.lin.helpdesk_software_api.Ticket;

import dev.lin.helpdesk_software_api.Implementations.IEditableService;
import dev.lin.helpdesk_software_api.dtos.CombinedTicketDTO;
import dev.lin.helpdesk_software_api.dtos.TicketRequestDTO;
import dev.lin.helpdesk_software_api.dtos.TicketResponseDTO;
import dev.lin.helpdesk_software_api.dtos.TicketStatusUpdateDTO;
import dev.lin.helpdesk_software_api.dtos.TicketEditDTO;

import java.util.List;

public interface ITicketService extends IEditableService<TicketResponseDTO, TicketRequestDTO> {
    TicketResponseDTO updateTicketStatus(Long ticketId, TicketStatusUpdateDTO dtoRequest);
    List<CombinedTicketDTO> getAllCombinedTickets();
    TicketResponseDTO updateTicket(Long ticketId, TicketEditDTO dtoRequest);
}