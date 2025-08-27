package dev.lin.helpdesk_software_api.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;
    
    public List<SubjectEntity> getAllSubjects() {
        return subjectRepository.findAll();
    }
}