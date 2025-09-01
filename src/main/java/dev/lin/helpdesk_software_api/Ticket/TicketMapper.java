package dev.lin.helpdesk_software_api.Ticket;

import org.springframework.stereotype.Component;
import dev.lin.helpdesk_software_api.Subject.*;

@Component
public class TicketMapper {

    private final SubjectRepository subjectRepository;

    public TicketMapper(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public TicketEntity toEntity(TicketRequestDTO dtoRequest) {
        SubjectEntity subject = subjectRepository.findById(dtoRequest.subjectId())
            .orElseThrow(() -> new RuntimeException("Subject not found with id " + dtoRequest.subjectId()));

        TicketEntity ticket = new TicketEntity(
            dtoRequest.requesterId(),
            subject,
            dtoRequest.description()
        );
        return ticket;
    }
    
    public TicketResponseDTO toDTO(TicketEntity ticket) {
        return new TicketResponseDTO(
            ticket.getId(),
            ticket.getRequesterId(),
            ticket.getSubjectId().getId(),
            ticket.getDescription(),
            ticket.getStatus(),
            ticket.getCreatedAt(),
            ticket.getUpdatedAt()
        );
    }
}