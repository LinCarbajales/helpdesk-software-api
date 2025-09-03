package dev.lin.helpdesk_software_api.Ticket;

import dev.lin.helpdesk_software_api.Employee.EmployeeEntity;
import dev.lin.helpdesk_software_api.Employee.EmployeeRepository;
import dev.lin.helpdesk_software_api.SolvedTicket.SolvedTicketEntity;
import dev.lin.helpdesk_software_api.SolvedTicket.SolvedTicketRepository;
import dev.lin.helpdesk_software_api.dtos.TicketRequestDTO;
import dev.lin.helpdesk_software_api.dtos.TicketResponseDTO;
import dev.lin.helpdesk_software_api.exceptions.EmployeeNotFoundException;
import dev.lin.helpdesk_software_api.exceptions.TicketNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final SolvedTicketRepository solvedTicketRepository;
    private final TicketMapper ticketMapper;
    private final EmployeeRepository employeeRepository;

    public TicketServiceImpl(
        TicketRepository ticketRepository,
        SolvedTicketRepository solvedTicketRepository,
        TicketMapper ticketMapper,
        EmployeeRepository employeeRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.solvedTicketRepository = solvedTicketRepository;
        this.ticketMapper = ticketMapper;
        this.employeeRepository = employeeRepository;
    }
    
    @Override
    public List<TicketResponseDTO> getAllEntities() {
        return ticketRepository.findAllByOrderByCreatedAtAsc()
                               .stream()
                               .map(ticketMapper::toDTO)
                               .collect(Collectors.toList());
    }

    @Override
    public TicketResponseDTO storeEntity(TicketRequestDTO dtoRequest) {
        TicketEntity ticket = ticketMapper.toEntity(dtoRequest);
        TicketEntity createdTicket = ticketRepository.save(ticket);
        return ticketMapper.toDTO(createdTicket);
    }

    @Override
    public TicketResponseDTO showById(Long id) {
        return ticketRepository.findById(id)
                               .map(ticketMapper::toDTO)
                               .orElseThrow(() -> new TicketNotFoundException("Ticket not found. Id " + id + " does not exist."));
    }

    @Transactional
    public TicketResponseDTO updateTicketStatus(Long ticketId, TicketUpdateRequestDTO dtoRequest) {
        return ticketRepository.findById(ticketId)
            .map(ticket -> {
                if (dtoRequest.newStatus() == TicketStatus.ATTENDED) {
                    if (ticket.getStatus().canTransitionTo(dtoRequest.newStatus())) {
                        ticket.setStatus(dtoRequest.newStatus());

                        EmployeeEntity attendee = employeeRepository.findById(dtoRequest.attendeeId())
                                .orElseThrow(() -> new EmployeeNotFoundException(dtoRequest.attendeeId()));
                        
                        SolvedTicketEntity solvedTicket = new SolvedTicketEntity(ticket, attendee);
                        solvedTicketRepository.save(solvedTicket);
                        
                        TicketEntity updatedTicket = ticketRepository.save(ticket);
                        return ticketMapper.toDTO(updatedTicket);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            })
            .orElse(null);
    }
}