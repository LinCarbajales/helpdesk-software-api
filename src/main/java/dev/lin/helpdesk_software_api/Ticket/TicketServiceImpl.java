package dev.lin.helpdesk_software_api.Ticket;

import dev.lin.helpdesk_software_api.Implementations.IGenericService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements IGenericService<TicketResponseDTO, TicketRequestDTO> {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
    
    @Override
    public List<TicketResponseDTO> getAllEntities() {
        return ticketRepository.findAllByOrderByCreatedAtAsc()
                               .stream()
                               .map(TicketMapper::toDTO)
                               .collect(Collectors.toList());
    }

    @Override
    public TicketResponseDTO storeEntity(TicketRequestDTO dtoRequest) {
        TicketEntity ticket = TicketMapper.toEntity(dtoRequest);
        TicketEntity createdTicket = ticketRepository.save(ticket);
        return TicketMapper.toDTO(createdTicket);
    }
}