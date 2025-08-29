package dev.lin.helpdesk_software_api.Subject;

import org.springframework.stereotype.Component;

@Component
public class SubjectMapper {

    public static SubjectResponseDTO toDTO(SubjectEntity subject) {
        return new SubjectResponseDTO(subject.getId(), subject.getName());
    }
}