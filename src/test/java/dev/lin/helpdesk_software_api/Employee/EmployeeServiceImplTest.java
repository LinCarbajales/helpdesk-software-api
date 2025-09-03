package dev.lin.helpdesk_software_api.Employee;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import dev.lin.helpdesk_software_api.dtos.EmployeeResponseDTO;
import dev.lin.helpdesk_software_api.exceptions.EmployeeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeEntity employee1;
    private EmployeeEntity employee2;
    private EmployeeResponseDTO employeeResponseDTO1;
    private EmployeeResponseDTO employeeResponseDTO2;

    @BeforeEach
    void setUp() {
        employee1 = new EmployeeEntity("John Doe", EmployeeRole.REQUESTER);
        employee1.setId(1L);

        employee2 = new EmployeeEntity("Jane Smith", EmployeeRole.TECHNICIAN);
        employee2.setId(2L);

        employeeResponseDTO1 = new EmployeeResponseDTO(1L, "John Doe", EmployeeRole.REQUESTER);
        employeeResponseDTO2 = new EmployeeResponseDTO(2L, "Jane Smith", EmployeeRole.TECHNICIAN);
    }

    @Test
    @DisplayName("Should return a list of all employees")
    void getAllEntities_ShouldReturnAllEmployees() {
        // Arrange
        List<EmployeeEntity> mockEntities = Arrays.asList(employee1, employee2);
        when(employeeRepository.findAll()).thenReturn(mockEntities);
        when(employeeMapper.toDTOList(mockEntities)).thenReturn(Arrays.asList(employeeResponseDTO1, employeeResponseDTO2));

        // Act
        List<EmployeeResponseDTO> result = employeeService.getAllEntities();

        // Assert
        assertThat(result, hasSize(2));
        assertThat(result.get(0).id(), is(equalTo(1L)));
        assertThat(result.get(1).id(), is(equalTo(2L)));
        verify(employeeRepository, times(1)).findAll();
        verify(employeeMapper, times(1)).toDTOList(mockEntities);
    }

    @Test
    @DisplayName("Should return an employee when found by ID")
    void showById_ShouldReturnEmployeeWhenFound() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        when(employeeMapper.toDTO(employee1)).thenReturn(employeeResponseDTO1);

        // Act
        EmployeeResponseDTO result = employeeService.showById(1L);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.id(), is(equalTo(1L)));
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeMapper, times(1)).toDTO(employee1);
    }

    @Test
    @DisplayName("Should throw EmployeeNotFoundException when employee not found by ID")
    void showById_ShouldThrowExceptionWhenNotFound() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.showById(1L));
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeMapper, never()).toDTO(any());
    }
}