package dev.lin.helpdesk_software_api.Ticket;

import dev.lin.helpdesk_software_api.Implementations.IGenericService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(path = "${api-endpoint}/tickets")
public class TicketController {

    private final IGenericService<TicketResponseDTO, TicketRequestDTO> ticketService;

    public TicketController(IGenericService<TicketResponseDTO, TicketRequestDTO> ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("")
    public List<TicketResponseDTO> getAllTickets() {
        return ticketService.getAllEntities();
    }

    @PostMapping("")
    public ResponseEntity<TicketResponseDTO> storeEntity(@Valid @RequestBody TicketRequestDTO dtoRequest) {

        if (dtoRequest.description().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        TicketResponseDTO entityStored = ticketService.storeEntity(dtoRequest);

        if (entityStored == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(entityStored);
    }
}