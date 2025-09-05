package dev.lin.helpdesk_software_api.Employee;

import dev.lin.helpdesk_software_api.SolvedTicket.SolvedTicketEntity;
import dev.lin.helpdesk_software_api.Ticket.TicketEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("EmployeeEntity Tests")
class EmployeeEntityTest {

    private EmployeeEntity employeeEntity;

    @BeforeEach
    void setUp() {
        employeeEntity = new EmployeeEntity();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create an empty EmployeeEntity with the default constructor")
        void shouldCreateEmptyEmployeeEntity() {
            // Given & When
            EmployeeEntity emptyEmployee = new EmployeeEntity();

            // Then
            assertThat(emptyEmployee.getId(), is(nullValue()));
            assertThat(emptyEmployee.getName(), is(nullValue()));
            assertThat(emptyEmployee.getRole(), is(nullValue()));
            assertThat(emptyEmployee.getCreatedTickets(), is(nullValue()));
            assertThat(emptyEmployee.getSolvedTickets(), is(nullValue()));
        }

        @Test
        @DisplayName("Should create EmployeeEntity with name and role using the parameterized constructor")
        void shouldCreateEmployeeEntityWithFields() {
            // Given
            String employeeName = "Jane Doe";
            EmployeeRole employeeRole = EmployeeRole.TECHNICIAN;

            // When
            EmployeeEntity newEmployee = new EmployeeEntity(employeeName, employeeRole);

            // Then
            assertThat(newEmployee.getName(), is(equalTo(employeeName)));
            assertThat(newEmployee.getRole(), is(equalTo(employeeRole)));
        }
    }

    @Nested
    @DisplayName("Getters and Setters Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get all properties correctly")
        void employeeEntity_ShouldSetAndGetProperties() {
            // Arrange
            Long id = 1L;
            String name = "Test Employee";
            EmployeeRole role = EmployeeRole.REQUESTER;
            List<TicketEntity> createdTickets = Collections.singletonList(new TicketEntity());
            List<SolvedTicketEntity> solvedTickets = Collections.singletonList(new SolvedTicketEntity());

            // Act
            employeeEntity.setId(id);
            employeeEntity.setName(name);
            employeeEntity.setRole(role);
            employeeEntity.setCreatedTickets(createdTickets);
            employeeEntity.setSolvedTickets(solvedTickets);

            // Assert
            assertThat(employeeEntity.getId(), is(equalTo(id)));
            assertThat(employeeEntity.getName(), is(equalTo(name)));
            assertThat(employeeEntity.getRole(), is(equalTo(role)));
            assertThat(employeeEntity.getCreatedTickets(), is(equalTo(createdTickets)));
            assertThat(employeeEntity.getSolvedTickets(), is(equalTo(solvedTickets)));
        }
    }
}