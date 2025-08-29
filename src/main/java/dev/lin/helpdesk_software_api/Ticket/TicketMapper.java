package dev.lin.helpdesk_software_api.Ticket;

import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public static TicketEntity toEntity(TicketRequestDTO dtoRequest) {
        TicketEntity ticket = new TicketEntity(
            dtoRequest.requesterId(),
            dtoRequest.subjectId(),
            dtoRequest.description()
        );
    return ticket;
}
    
    public static TicketResponseDTO toDTO(TicketEntity ticket) {
        return new TicketResponseDTO(
            ticket.getId(),
            ticket.getRequesterId(),
            ticket.getSubjectId(),
            ticket.getDescription(),
            ticket.getStatus(),
            ticket.getCreatedAt(),
            ticket.getUpdatedAt()
        );
    }
}