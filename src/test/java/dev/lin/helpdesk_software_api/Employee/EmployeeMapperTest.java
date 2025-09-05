package dev.lin.helpdesk_software_api.Employee;

import dev.lin.helpdesk_software_api.dtos.EmployeeResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("EmployeeMapper Tests")
class EmployeeMapperTest {

    private EmployeeMapper employeeMapper;
    private EmployeeEntity employee1;
    private EmployeeEntity employee2;

    @BeforeEach
    void setUp() {
        employeeMapper = new EmployeeMapper();

        employee1 = new EmployeeEntity("John Doe", EmployeeRole.REQUESTER);
        employee1.setId(1L);

        employee2 = new EmployeeEntity("Jane Smith", EmployeeRole.TECHNICIAN);
        employee2.setId(2L);
    }

    @Test
    @DisplayName("Should correctly map a single EmployeeEntity to EmployeeResponseDTO")
    void toDTO_ShouldMapCorrectly() {
        // Act
        EmployeeResponseDTO result = employeeMapper.toDTO(employee1);

        // Assert
        assertThat(result.id(), is(equalTo(employee1.getId())));
        assertThat(result.name(), is(equalTo(employee1.getName())));
        assertThat(result.role(), is(equalTo(employee1.getRole())));
    }

    @Test
    @DisplayName("Should correctly map a list of EmployeeEntity to a list of EmployeeResponseDTO")
    void toDTOList_ShouldMapListCorrectly() {
        // Arrange
        List<EmployeeEntity> employees = Arrays.asList(employee1, employee2);

        // Act
        List<EmployeeResponseDTO> result = employeeMapper.toDTOList(employees);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(2));
        assertThat(result.get(0).id(), is(equalTo(employee1.getId())));
        assertThat(result.get(1).id(), is(equalTo(employee2.getId())));
    }
}
