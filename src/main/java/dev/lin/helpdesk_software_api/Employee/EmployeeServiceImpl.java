package dev.lin.helpdesk_software_api.Employee;

import dev.lin.helpdesk_software_api.dtos.EmployeeResponseDTO;
import dev.lin.helpdesk_software_api.exceptions.EmployeeNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public List<EmployeeResponseDTO> getAllEntities() {
        return employeeMapper.toDTOList(employeeRepository.findAll());
    }

    @Override
    public EmployeeResponseDTO showById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toDTO)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
}