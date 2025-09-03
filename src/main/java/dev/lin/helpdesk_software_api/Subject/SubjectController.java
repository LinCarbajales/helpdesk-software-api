package dev.lin.helpdesk_software_api.Subject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.lin.helpdesk_software_api.dtos.SubjectResponseDTO;

import java.util.List;

@RestController
@RequestMapping(path = "${api-endpoint}/subjects")
public class SubjectController {
    
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<SubjectResponseDTO> getAllSubjects() {
        return subjectService.getAllEntities();
    }

    @GetMapping("/{id}")
    public SubjectResponseDTO getSubjectById(@PathVariable Long id) {
        return subjectService.showById(id);
    }
}