package dev.lin.helpdesk_software_api.Ticket;

import dev.lin.helpdesk_software_api.Employee.EmployeeEntity;
import dev.lin.helpdesk_software_api.SolvedTicket.SolvedTicketEntity;
import dev.lin.helpdesk_software_api.Subject.SubjectEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("TicketEntity Tests")
class TicketEntityTest {

    private TicketEntity ticketEntity;
    private EmployeeEntity mockRequester;
    private SubjectEntity mockSubject;

    @BeforeEach
    void setUp() {
        mockRequester = new EmployeeEntity("John Doe", null);
        mockSubject = new SubjectEntity("Test Subject");
        ticketEntity = new TicketEntity();
    }

    @Nested
    @DisplayName("Constructor and Getters/Setters Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty TicketEntity with default constructor")
        void shouldCreateEmptyTicketEntity() {
            TicketEntity ticket = new TicketEntity();

            assertThat(ticket.getId(), is(nullValue()));
            assertThat(ticket.getRequester(), is(nullValue()));
            assertThat(ticket.getSubject(), is(nullValue()));
            assertThat(ticket.getDescription(), is(nullValue()));
            assertThat(ticket.getSolvedTicket(), is(nullValue()));
            assertThat(ticket.getStatus(), is(nullValue()));
            assertThat(ticket.getCreatedAt(), is(nullValue()));
            assertThat(ticket.getUpdatedAt(), is(nullValue()));
        }

        @Test
        @DisplayName("Should create TicketEntity with specified fields")
        void shouldCreateTicketEntityWithFields() {
            String description = "Test ticket description";
            TicketEntity ticket = new TicketEntity(mockRequester, mockSubject, description);

            assertThat(ticket.getRequester(), is(equalTo(mockRequester)));
            assertThat(ticket.getSubject(), is(equalTo(mockSubject)));
            assertThat(ticket.getDescription(), is(equalTo(description)));
            assertThat(ticket.getSolvedTicket(), is(nullValue()));
        }

        @Test
        @DisplayName("Should set and get all properties correctly")
        void ticketEntity_ShouldSetAndGetProperties() {
            // Arrange
            Long id = 1L;
            String description = "Another test description";
            TicketStatus status = TicketStatus.OPEN;
            LocalDateTime createdAt = LocalDateTime.now();
            LocalDateTime updatedAt = createdAt.plusHours(1);
            SolvedTicketEntity solvedTicket = new SolvedTicketEntity();

            // Act
            ticketEntity.setId(id);
            ticketEntity.setRequester(mockRequester);
            ticketEntity.setSubject(mockSubject);
            ticketEntity.setDescription(description);
            ticketEntity.setStatus(status);
            ticketEntity.setCreatedAt(createdAt);
            ticketEntity.setUpdatedAt(updatedAt);
            ticketEntity.setSolvedTicket(solvedTicket);

            // Assert
            assertEquals(id, ticketEntity.getId());
            assertEquals(mockRequester, ticketEntity.getRequester());
            assertEquals(mockSubject, ticketEntity.getSubject());
            assertEquals(description, ticketEntity.getDescription());
            assertEquals(status, ticketEntity.getStatus());
            assertEquals(createdAt, ticketEntity.getCreatedAt());
            assertEquals(updatedAt, ticketEntity.getUpdatedAt());
            assertEquals(solvedTicket, ticketEntity.getSolvedTicket());
        }
    }

    @Nested
    @DisplayName("Lifecycle Callbacks Tests")
    class LifecycleTests {

        @Test
        @DisplayName("Should initialize createdAt, updatedAt and status on creation")
        void onCreate_ShouldSetInitialValues() {
            // Given & When
            ReflectionTestUtils.invokeMethod(ticketEntity, "onCreate");

            // Then
            assertThat(ticketEntity.getCreatedAt(), is(notNullValue()));
            assertThat(ticketEntity.getUpdatedAt(), is(notNullValue()));
            assertThat(ticketEntity.getStatus(), is(equalTo(TicketStatus.OPEN)));
        }

        @Test
        @DisplayName("Should update updatedAt on update and keep createdAt unchanged")
        void onUpdate_ShouldUpdateUpdatedAt() {
            // Given
            LocalDateTime initialTime = LocalDateTime.now().minusMinutes(5);
            ticketEntity.setCreatedAt(initialTime);
            ticketEntity.setUpdatedAt(initialTime);
            LocalDateTime beforeUpdate = LocalDateTime.now();

            // When
            ReflectionTestUtils.invokeMethod(ticketEntity, "onUpdate");

            // Then
            assertThat(ticketEntity.getCreatedAt(), is(equalTo(initialTime)));
            assertThat(ticketEntity.getUpdatedAt(), is(notNullValue()));
            assertThat(ticketEntity.getUpdatedAt(), is(greaterThanOrEqualTo(beforeUpdate)));
        }
    }
}
