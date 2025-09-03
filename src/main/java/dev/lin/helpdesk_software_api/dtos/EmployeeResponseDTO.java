package dev.lin.helpdesk_software_api.dtos;

import dev.lin.helpdesk_software_api.Employee.EmployeeRole;

public record EmployeeResponseDTO(Long id, String name, EmployeeRole role) {
}