package dev.lin.helpdesk_software_api.Ticket;

import dev.lin.helpdesk_software_api.Employee.EmployeeEntity;
import dev.lin.helpdesk_software_api.Subject.SubjectEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("TicketEntity Tests")
class TicketEntityTest {

    private TicketEntity ticketEntity;
    private SubjectEntity mockSubject;
    private EmployeeEntity mockEmployee;

    @BeforeEach
    void setUp() {
        mockSubject = new SubjectEntity();
        mockEmployee = new EmployeeEntity("Test User", null);
        ticketEntity = new TicketEntity();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty TicketEntity with default constructor")
        void shouldCreateEmptyTicketEntity() {
            // Given & When
            TicketEntity ticket = new TicketEntity();

            // Then
            assertThat(ticket.getId(), is(nullValue()));
            assertThat(ticket.getRequester(), is(nullValue()));
            assertThat(ticket.getSubject(), is(nullValue()));
            assertThat(ticket.getDescription(), is(nullValue()));
            assertThat(ticket.getStatus(), is(equalTo(TicketStatus.OPEN)));
            assertThat(ticket.getCreatedAt(), is(nullValue()));
            assertThat(ticket.getUpdatedAt(), is(nullValue()));
        }

        @Test
        @DisplayName("Should create TicketEntity with parameterized constructor")
        void shouldCreateTicketEntityWithParameters() {
            // Given
            String description = "Test description";
            LocalDateTime beforeCreation = LocalDateTime.now();

            // When
            // Se ha cambiado de Long a EmployeeEntity
            TicketEntity ticket = new TicketEntity(mockEmployee, mockSubject, description);

            // Then
            assertThat(ticket.getRequester(), is(equalTo(mockEmployee)));
            assertThat(ticket.getSubject(), is(equalTo(mockSubject)));
            assertThat(ticket.getDescription(), is(equalTo(description)));
            assertThat(ticket.getStatus(), is(equalTo(TicketStatus.OPEN)));
            assertThat(ticket.getCreatedAt(), is(notNullValue()));
            assertThat(ticket.getUpdatedAt(), is(notNullValue()));
            assertThat(ticket.getCreatedAt(), is(greaterThanOrEqualTo(beforeCreation)));
            assertThat(ticket.getUpdatedAt(), is(greaterThanOrEqualTo(beforeCreation)));
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get id correctly")
        void shouldSetAndGetId() {
            // Given
            Long id = 123L;

            // When
            ticketEntity.setId(id);

            // Then
            assertThat(ticketEntity.getId(), is(equalTo(id)));
        }

        @Test
        @DisplayName("Should set and get requester correctly")
        void shouldSetAndGetRequester() {
            // When
            // Se ha cambiado a un objeto EmployeeEntity
            ticketEntity.setRequester(mockEmployee);

            // Then
            assertThat(ticketEntity.getRequester(), is(equalTo(mockEmployee)));
        }

        @Test
        @DisplayName("Should set and get subject correctly")
        void shouldSetAndGetSubjectId() {
            // When
            ticketEntity.setSubject(mockSubject);

            // Then
            assertThat(ticketEntity.getSubject(), is(equalTo(mockSubject)));
        }

        @Test
        @DisplayName("Should set and get description correctly")
        void shouldSetAndGetDescription() {
            // Given
            String description = "Test ticket description";

            // When
            ticketEntity.setDescription(description);

            // Then
            assertThat(ticketEntity.getDescription(), is(equalTo(description)));
        }

        @Test
        @DisplayName("Should set and get status correctly")
        void shouldSetAndGetStatus() {
            // Given
            TicketStatus status = TicketStatus.ATTENDED;

            // When
            ticketEntity.setStatus(status);

            // Then
            assertThat(ticketEntity.getStatus(), is(equalTo(status)));
        }

        @Test
        @DisplayName("Should set and get createdAt correctly")
        void shouldSetAndGetCreatedAt() {
            // Given
            LocalDateTime createdAt = LocalDateTime.now();

            // When
            ticketEntity.setCreatedAt(createdAt);

            // Then
            assertThat(ticketEntity.getCreatedAt(), is(equalTo(createdAt)));
        }

        @Test
        @DisplayName("Should set and get updatedAt correctly")
        void shouldSetAndGetUpdatedAt() {
            // Given
            LocalDateTime updatedAt = LocalDateTime.now();

            // When
            ticketEntity.setUpdatedAt(updatedAt);

            // Then
            assertThat(ticketEntity.getUpdatedAt(), is(equalTo(updatedAt)));
        }
    }

    @Nested
    @DisplayName("JPA Lifecycle Tests")
    class JpaLifecycleTests {

        @Test
        @DisplayName("Should set timestamps on create")
        void shouldSetTimestampsOnCreate() {
            // Given
            LocalDateTime beforeCreate = LocalDateTime.now();

            // When
            ticketEntity.onCreate();

            // Then
            assertThat(ticketEntity.getCreatedAt(), is(notNullValue()));
            assertThat(ticketEntity.getUpdatedAt(), is(notNullValue()));
            assertThat(ticketEntity.getCreatedAt(), is(greaterThanOrEqualTo(beforeCreate)));
            assertThat(ticketEntity.getUpdatedAt(), is(greaterThanOrEqualTo(beforeCreate)));
        }

        @Test
        @DisplayName("Should update timestamp on update")
        void shouldUpdateTimestampOnUpdate() {
            // Given
            LocalDateTime initialTime = LocalDateTime.now().minusHours(1);
            ticketEntity.setCreatedAt(initialTime);
            ticketEntity.setUpdatedAt(initialTime);
            LocalDateTime beforeUpdate = LocalDateTime.now();

            // When
            ticketEntity.onUpdate();

            // Then
            assertThat(ticketEntity.getCreatedAt(), is(equalTo(initialTime))); // Should remain unchanged
            assertThat(ticketEntity.getUpdatedAt(), is(notNullValue()));
            assertThat(ticketEntity.getUpdatedAt(), is(greaterThanOrEqualTo(beforeUpdate)));
            assertThat(ticketEntity.getUpdatedAt(), is(greaterThan(initialTime)));
        }

        @Test
        @DisplayName("Should not modify createdAt on update")
        void shouldNotModifyCreatedAtOnUpdate() {
            // Given
            LocalDateTime originalCreatedAt = LocalDateTime.now().minusDays(1);
            ticketEntity.setCreatedAt(originalCreatedAt);
            ticketEntity.setUpdatedAt(originalCreatedAt);

            // When
            ticketEntity.onUpdate();

            // Then
            assertThat(ticketEntity.getCreatedAt(), is(equalTo(originalCreatedAt)));
        }
    }

    @Nested
    @DisplayName("Default Values Tests")
    class DefaultValuesTests {

        @Test
        @DisplayName("Should have OPEN status as default")
        void shouldHaveOpenStatusAsDefault() {
            // Given & When
            TicketEntity ticket = new TicketEntity();

            // Then
            assertThat(ticket.getStatus(), is(equalTo(TicketStatus.OPEN)));
        }
    }
}