package dev.lin.helpdesk_software_api.Ticket;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.lin.helpdesk_software_api.dtos.CombinedTicketDTO;
import dev.lin.helpdesk_software_api.dtos.TicketRequestDTO;
import dev.lin.helpdesk_software_api.dtos.TicketResponseDTO;

import java.util.List;

@RestController
@RequestMapping(path = "${api-endpoint}/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final TicketServiceImpl ticketServiceImpl;

    public TicketController(
        TicketService ticketService,
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