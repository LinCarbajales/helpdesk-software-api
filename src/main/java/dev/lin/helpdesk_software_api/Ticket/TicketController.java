package dev.lin.helpdesk_software_api.Ticket;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; // Importa PostMapping
import org.springframework.web.bind.annotation.RequestBody; // Importa RequestBody
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus; // Importa ResponseStatus
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(path = "${api-endpoint}/tickets")
public class TicketController {
    
    @Autowired
    private TicketService ticketService;
    
    // Método para crear un ticket de prueba y devolverlo
    @GetMapping("/create-and-get")
    public TicketEntity createAndGetTicket() {
        TicketEntity newTicket = new TicketEntity(
            4L, 
            2L, 
            "No arranca el Excel",
            "Pending"
        );
        return ticketService.createTicket(newTicket);
    }
    
    // Método para obtener todos los tickets
    @GetMapping("")
    public List<TicketEntity> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketEntity createTicket(@RequestBody TicketEntity newTicket) {
        return ticketService.createTicket(newTicket);
    }
}