package dev.lin.helpdesk_software_api.Ticket;
 
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping(path = "${api-endpoint}/tickets")
public class TicketController {
    
    @GetMapping("")
    public TicketEntity index() {

        TicketEntity ticket1 = new TicketEntity(1L, 4L, 2L, "No arranca el Excel",
                       "Pending");

        // class -> json = serializar . json -> class = deserializar
        return ticket1;
    }
    
}
