package dev.lin.helpdesk_software_api.Ticket;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.lin.helpdesk_software_api.dtos.CombinedTicketDTO;
import dev.lin.helpdesk_software_api.dtos.TicketEditDTO;
import dev.lin.helpdesk_software_api.dtos.TicketRequestDTO;
import dev.lin.helpdesk_software_api.dtos.TicketResponseDTO;
import dev.lin.helpdesk_software_api.dtos.TicketStatusUpdateDTO;

import java.util.List;

@RestController
@RequestMapping(path = "${api-endpoint}/tickets")
public class TicketController {

    private final ITicketService ticketService;

    public TicketController(
        ITicketService ticketService
    ) {
        this.ticketService = ticketService;
    }

    @GetMapping("")
    public List<TicketResponseDTO> getAllTickets() {
        return ticketService.getAllEntities();
    }

    @GetMapping("/{id}")
    public TicketResponseDTO getTicketById(@PathVariable Long id) {
        return ticketService.showById(id);
    }

    @GetMapping("/combined")
    public ResponseEntity<List<CombinedTicketDTO>> getAllCombinedTickets() {
        List<CombinedTicketDTO> combinedTickets = ticketService.getAllCombinedTickets();
        return ResponseEntity.ok(combinedTickets);
    }

    @PostMapping("")
    public ResponseEntity<TicketResponseDTO> storeEntity(@Valid @RequestBody TicketRequestDTO dtoRequest) {
        TicketResponseDTO entityStored = ticketService.storeEntity(dtoRequest);
        if (entityStored == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(entityStored);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponseDTO> updateTicketStatus(
        @PathVariable Long id,
        @Valid @RequestBody TicketStatusUpdateDTO dtoRequest
    ) {

        TicketResponseDTO updatedTicket = ticketService.updateTicketStatus(id, dtoRequest);
    
        if (updatedTicket == null) {
            return ResponseEntity.badRequest().build();
        }
    
        return ResponseEntity.ok(updatedTicket);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> editTicket(@PathVariable Long id, @RequestBody TicketEditDTO dtoRequest) {
        TicketResponseDTO updatedTicket = ticketService.updateTicket(id, dtoRequest);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}