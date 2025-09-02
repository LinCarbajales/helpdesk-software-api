package dev.lin.helpdesk_software_api.Subject;

import dev.lin.helpdesk_software_api.Implementations.IReadOnlyService;
import dev.lin.helpdesk_software_api.exceptions.SubjectNotFoundException;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements IReadOnlyService<SubjectResponseDTO> {

    private final SubjectRepository subjectRepository;
    
    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }
    
    @Override
    public List<SubjectResponseDTO> getAllEntities() {
        return subjectRepository.findAll()
                                .stream()
                                .map(SubjectMapper::toDTO)
                                .collect(Collectors.toList());
    }

    @Override
    public SubjectResponseDTO showById(Long id) {
        return subjectRepository.findById(id)
                                .map(SubjectMapper::toDTO)
                                .orElseThrow(() -> new SubjectNotFoundException("Subject not found. Id " + id + " does not exist."));
    }

}