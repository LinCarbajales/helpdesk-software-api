package dev.lin.helpdesk_software_api.Subject;

import dev.lin.helpdesk_software_api.Implementations.IReadOnlyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(path = "${api-endpoint}/subjects")
public class SubjectController {
    
    private final IReadOnlyService<SubjectResponseDTO> subjectService;

    public SubjectController(IReadOnlyService<SubjectResponseDTO> subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<SubjectResponseDTO> getAllSubjects() {
        return subjectService.getAllEntities();
    }
}