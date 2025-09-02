package dev.lin.helpdesk_software_api.Ticket;

import dev.lin.helpdesk_software_api.Implementations.IGenericService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "${api-endpoint}/tickets")
public class TicketController {

    private final IGenericService<TicketResponseDTO, TicketRequestDTO> ticketService; // Mantenemos esta inyección
    private final TicketServiceImpl ticketServiceImpl; // <-- Inyectamos la implementación concreta

    public TicketController(
        IGenericService<TicketResponseDTO, TicketRequestDTO> ticketService,
        TicketServiceImpl ticketServiceImpl
    ) {
        this.ticketService = ticketService;
        this.ticketServiceImpl = ticketServiceImpl;
    }

    @GetMapping("")
    public List<TicketResponseDTO> getAllTickets() {
        return ticketService.getAllEntities();
    }

    @GetMapping("/{id}")
    public TicketResponseDTO getTicketById(@PathVariable Long id) {
        return ticketServiceImpl.showById(id);
    }

    @PostMapping("")
    public ResponseEntity<TicketResponseDTO> storeEntity(@Valid @RequestBody TicketRequestDTO dtoRequest) {
        TicketResponseDTO entityStored = ticketService.storeEntity(dtoRequest);
        if (entityStored == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(entityStored);
    }

    @PatchMapping("/{ticketId}")
    public ResponseEntity<TicketResponseDTO> updateTicketStatus(
        @PathVariable Long ticketId,
        @Valid @RequestBody TicketUpdateRequestDTO dtoRequest
    ) {

        TicketResponseDTO updatedTicket = ticketServiceImpl.updateTicketStatus(ticketId, dtoRequest);
    
        if (updatedTicket == null) {
            return ResponseEntity.badRequest().build();
        }
    
        return ResponseEntity.ok(updatedTicket);
    }
}