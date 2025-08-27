package dev.lin.helpdesk_software_api.Ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public TicketEntity createTicket(TicketEntity ticket) {
        return ticketRepository.save(ticket);
    }
    
    public List<TicketEntity> getAllTickets() {
        return ticketRepository.findAll();
    }
}