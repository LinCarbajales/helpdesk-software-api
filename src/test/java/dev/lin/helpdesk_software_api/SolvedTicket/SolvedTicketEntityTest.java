package dev.lin.helpdesk_software_api.SolvedTicket;

import dev.lin.helpdesk_software_api.Employee.EmployeeEntity;
import dev.lin.helpdesk_software_api.Subject.SubjectEntity;
import dev.lin.helpdesk_software_api.Ticket.TicketEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SolvedTicketEntityTest {

    @Test
    @DisplayName("Should set solvedAt date on entity creation")
    void onPersist_ShouldSetSolvedAtDate() {
        // Arrange
        SubjectEntity subject = new SubjectEntity("Test Subject");
        // El primer argumento del constructor de TicketEntity es ahora un objeto EmployeeEntity
        EmployeeEntity mockRequester = new EmployeeEntity("Test Requester", null);
        TicketEntity ticket = new TicketEntity(mockRequester, subject, "Test ticket");
        // El segundo argumento del constructor de SolvedTicketEntity es ahora un objeto EmployeeEntity
        EmployeeEntity mockAttendee = new EmployeeEntity("Test Attendee", null);
        SolvedTicketEntity solvedTicket = new SolvedTicketEntity(ticket, mockAttendee);

        // Act
        ReflectionTestUtils.invokeMethod(solvedTicket, "onPersist");
        
        // Assert
        assertThat(solvedTicket.getSolvedAt(), is(notNullValue()));
        assertThat(solvedTicket.getSolvedAt(), is(lessThanOrEqualTo(LocalDateTime.now())));
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void solvedTicketEntity_ShouldSetAndGetProperties() {
        // Arrange
        SubjectEntity subject = new SubjectEntity("Another Subject");
        EmployeeEntity mockRequester = new EmployeeEntity("Another Requester", null);
        TicketEntity ticket = new TicketEntity(mockRequester, subject, "Another test ticket");
        ticket.setId(1L);

        EmployeeEntity mockAttendee = new EmployeeEntity("Another Attendee", null);
        SolvedTicketEntity solvedTicket = new SolvedTicketEntity(ticket, mockAttendee);
        solvedTicket.setId(ticket.getId());
        solvedTicket.setSolvedAt(LocalDateTime.of(2025, 1, 1, 10, 0));

        // Assert
        assertThat(solvedTicket.getId(), is(equalTo(1L)));
        assertThat(solvedTicket.getTicket(), is(equalTo(ticket)));
        // Se ha cambiado de getAttendeeId() a getAttendee()
        assertThat(solvedTicket.getAttendee(), is(equalTo(mockAttendee)));
        assertThat(solvedTicket.getSolvedAt(), is(equalTo(LocalDateTime.of(2025, 1, 1, 10, 0))));
    }
}