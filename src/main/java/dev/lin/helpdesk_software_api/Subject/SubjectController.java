package dev.lin.helpdesk_software_api.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(path = "${api-endpoint}/subjects")
public class SubjectController {
    
    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public List<SubjectEntity> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

}