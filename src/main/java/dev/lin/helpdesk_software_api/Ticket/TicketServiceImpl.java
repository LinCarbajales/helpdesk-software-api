package dev.lin.helpdesk_software_api.Ticket;

import dev.lin.helpdesk_software_api.Employee.EmployeeEntity;
import dev.lin.helpdesk_software_api.Employee.EmployeeRepository;
import dev.lin.helpdesk_software_api.SolvedTicket.SolvedTicketEntity;
import dev.lin.helpdesk_software_api.SolvedTicket.SolvedTicketRepository;
import dev.lin.helpdesk_software_api.Subject.SubjectEntity;
import dev.lin.helpdesk_software_api.Subject.SubjectRepository;
import dev.lin.helpdesk_software_api.dtos.CombinedTicketDTO;
import dev.lin.helpdesk_software_api.dtos.TicketRequestDTO;
import dev.lin.helpdesk_software_api.dtos.TicketResponseDTO;
import dev.lin.helpdesk_software_api.dtos.TicketStatusUpdateDTO;
import dev.lin.helpdesk_software_api.dtos.TicketEditDTO;
import dev.lin.helpdesk_software_api.exceptions.EmployeeNotFoundException;
import dev.lin.helpdesk_software_api.exceptions.SubjectNotFoundException;
import dev.lin.helpdesk_software_api.exceptions.TicketNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements ITicketService {

    private final TicketRepository ticketRepository;
    private final SolvedTicketRepository solvedTicketRepository;
    private final TicketMapper ticketMapper;
    private final EmployeeRepository employeeRepository;
    private final SubjectRepository subjectRepository;

    public TicketServiceImpl(
        TicketRepository ticketRepository,
        SolvedTicketRepository solvedTicketRepository,
        TicketMapper ticketMapper,
        EmployeeRepository employeeRepository,
        SubjectRepository subjectRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.solvedTicketRepository = solvedTicketRepository;
        this.ticketMapper = ticketMapper;
        this.employeeRepository = employeeRepository;
        this.subjectRepository = subjectRepository;
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

    @Override
    public List<CombinedTicketDTO> getAllCombinedTickets() {
        return ticketRepository.findAllCombinedTickets();
    }

    @Transactional
    public TicketResponseDTO updateTicketStatus(Long ticketId, TicketStatusUpdateDTO dtoRequest) {
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

        @Override
    @Transactional
    public TicketResponseDTO updateTicket(Long ticketId, TicketEditDTO dtoRequest) {
        return ticketRepository.findById(ticketId)
            .map(existingTicket -> {
                if (dtoRequest.description() != null) {
                    existingTicket.setDescription(dtoRequest.description());
                }

                if (dtoRequest.requesterId() != null) {
                    EmployeeEntity requester = employeeRepository.findById(dtoRequest.requesterId())
                        .orElseThrow(() -> new EmployeeNotFoundException(dtoRequest.requesterId()));
                    existingTicket.setRequester(requester);
                }

                if (dtoRequest.subjectId() != null) {
                    SubjectEntity subject = subjectRepository.findById(dtoRequest.subjectId())
                        .orElseThrow(() -> new SubjectNotFoundException("Subject with ID " + dtoRequest.subjectId() + " not found."));
                    existingTicket.setSubject(subject);
                }

                TicketEntity updatedTicket = ticketRepository.save(existingTicket);
                
                return ticketMapper.toDTO(updatedTicket);
            })
            .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id " + ticketId));
    }
}