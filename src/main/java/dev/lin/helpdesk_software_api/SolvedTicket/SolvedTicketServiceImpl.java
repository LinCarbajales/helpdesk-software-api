package dev.lin.helpdesk_software_api.SolvedTicket;

import dev.lin.helpdesk_software_api.Implementations.IReadOnlyService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class SolvedTicketServiceImpl implements IReadOnlyService<SolvedTicketResponseDTO> {

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
    public Optional<SolvedTicketResponseDTO> showById(Long id) {
        return solvedTicketRepository.findById(id)
            .map(solvedTicketMapper::toDTO);
    }
}