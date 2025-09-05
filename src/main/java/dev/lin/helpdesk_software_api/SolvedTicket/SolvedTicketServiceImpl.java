package dev.lin.helpdesk_software_api.SolvedTicket;

import dev.lin.helpdesk_software_api.dtos.SolvedTicketResponseDTO;
import dev.lin.helpdesk_software_api.exceptions.SolvedTicketNotFoundException;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolvedTicketServiceImpl implements ISolvedTicketService {

    private final SolvedTicketRepository solvedTicketRepository;
    private final SolvedTicketMapper solvedTicketMapper;

    public SolvedTicketServiceImpl(
        SolvedTicketRepository solvedTicketRepository,
        SolvedTicketMapper solvedTicketMapper
    ) {
        this.solvedTicketRepository = solvedTicketRepository;
        this.solvedTicketMapper = solvedTicketMapper;
    }

    @Override
    public List<SolvedTicketResponseDTO> getAllEntities() {
        return solvedTicketRepository.findAll()
                .stream()
                .map(solvedTicketMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SolvedTicketResponseDTO showById(Long id) {
        return solvedTicketRepository.findById(id)
            .map(solvedTicketMapper::toDTO)
            .orElseThrow(() -> new SolvedTicketNotFoundException("Solved ticket not found. Id " + id + " does not exist."));
    }
}