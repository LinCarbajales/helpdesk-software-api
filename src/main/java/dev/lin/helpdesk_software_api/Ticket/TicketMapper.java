package dev.lin.helpdesk_software_api.Ticket;

import dev.lin.helpdesk_software_api.Employee.EmployeeEntity;
import dev.lin.helpdesk_software_api.Employee.EmployeeRepository;
import dev.lin.helpdesk_software_api.Subject.SubjectEntity;
import dev.lin.helpdesk_software_api.Subject.SubjectRepository;
import dev.lin.helpdesk_software_api.dtos.TicketRequestDTO;
import dev.lin.helpdesk_software_api.dtos.TicketResponseDTO;
import dev.lin.helpdesk_software_api.exceptions.EmployeeNotFoundException;
import dev.lin.helpdesk_software_api.exceptions.SubjectNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class TicketMapper {

    private final EmployeeRepository employeeRepository;
    private final SubjectRepository subjectRepository;

    public TicketMapper(EmployeeRepository employeeRepository, SubjectRepository subjectRepository) {
        this.employeeRepository = employeeRepository;
        this.subjectRepository = subjectRepository;
    }

    public TicketEntity toEntity(TicketRequestDTO dtoRequest) {
    EmployeeEntity requester = employeeRepository.findById(dtoRequest.requesterId())
            .orElseThrow(() -> new EmployeeNotFoundException(dtoRequest.requesterId()));
        
        SubjectEntity subject = subjectRepository.findById(dtoRequest.subjectId())
                .orElseThrow(() -> new SubjectNotFoundException("Subject not found with id " + dtoRequest.subjectId()));

        TicketEntity ticket = new TicketEntity(
            requester,
            subject,
            dtoRequest.description()
        );
        return ticket;
    }
    
    public TicketResponseDTO toDTO(TicketEntity ticket) {
        return new TicketResponseDTO(
            ticket.getId(),
            ticket.getRequester().getId(),
            ticket.getSubject().getId(),
            ticket.getDescription(),
            ticket.getStatus(),
            ticket.getCreatedAt(),
            ticket.getUpdatedAt()
        );
    }
}