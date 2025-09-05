package dev.lin.helpdesk_software_api.Employee;

import org.springframework.stereotype.Component;

import dev.lin.helpdesk_software_api.dtos.EmployeeResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {

    public EmployeeResponseDTO toDTO(EmployeeEntity employee) {
        return new EmployeeResponseDTO(
            employee.getId(),
            employee.getName(),
            employee.getRole()
        );
    }

    public List<EmployeeResponseDTO> toDTOList(List<EmployeeEntity> employees) {
        return employees.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}