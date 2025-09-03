package dev.lin.helpdesk_software_api.SolvedTicket;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.lin.helpdesk_software_api.dtos.SolvedTicketResponseDTO;

import java.util.List;

@RestController
@RequestMapping(path = "${api-endpoint}/solved_tickets")
public class SolvedTicketController {

    private final SolvedTicketService solvedTicketService;

    public SolvedTicketController(SolvedTicketService solvedTicketService) {
        this.solvedTicketService = solvedTicketService;
    }

    @GetMapping
    public List<SolvedTicketResponseDTO> getAllSolvedTickets() {
        return solvedTicketService.getAllEntities();
    }

    @GetMapping("/{id}")
    public SolvedTicketResponseDTO getSolvedTicketById(@PathVariable Long id) {
        return solvedTicketService.showById(id);
    }
}