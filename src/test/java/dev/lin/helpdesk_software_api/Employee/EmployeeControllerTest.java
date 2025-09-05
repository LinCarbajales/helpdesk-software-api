package dev.lin.helpdesk_software_api.Employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lin.helpdesk_software_api.dtos.EmployeeResponseDTO;
import dev.lin.helpdesk_software_api.exceptions.EmployeeNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IEmployeeService IemployeeService;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("Should return all employees")
    void testGetAllEmployees_ShouldReturnAllEmployees() throws Exception {
        // Arrange
        EmployeeResponseDTO employee1 = new EmployeeResponseDTO(1L, "John Doe", EmployeeRole.REQUESTER);
        EmployeeResponseDTO employee2 = new EmployeeResponseDTO(2L, "Jane Smith", EmployeeRole.TECHNICIAN);
        List<EmployeeResponseDTO> mockEmployees = List.of(employee1, employee2);
        String json = mapper.writeValueAsString(mockEmployees);

        when(IemployeeService.getAllEntities()).thenReturn(mockEmployees);
        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        // Assert
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(json)));
    }

    @Test
    @DisplayName("Should return an employee by ID")
    void testGetEmployeeById_ShouldReturnCorrectEmployee() throws Exception {
        // Arrange
        Long employeeId = 1L;
        EmployeeResponseDTO mockEmployee = new EmployeeResponseDTO(employeeId, "John Doe", EmployeeRole.REQUESTER);
        String json = mapper.writeValueAsString(mockEmployee);

        when(IemployeeService.showById(employeeId)).thenReturn(mockEmployee);

        // Act & Assert
        mockMvc.perform(get("/api/v1/employees/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("Should return 404 Not Found when employee is not found by ID")
    void testGetEmployeeById_ShouldReturnNotFoundForInvalidId() throws Exception {
        // Arrange
        Long nonExistentId = 99L;
        when(IemployeeService.showById(nonExistentId))
                .thenThrow(new EmployeeNotFoundException(nonExistentId));

        // Act & Assert
        mockMvc.perform(get("/api/v1/employees/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }
}
