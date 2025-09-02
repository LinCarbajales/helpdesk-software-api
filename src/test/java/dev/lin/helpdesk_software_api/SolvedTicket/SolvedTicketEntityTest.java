package dev.lin.helpdesk_software_api.SolvedTicket;

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
        TicketEntity ticket = new TicketEntity(1L, subject, "Test ticket");
        SolvedTicketEntity solvedTicket = new SolvedTicketEntity(ticket, 2L);

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
        TicketEntity ticket = new TicketEntity(1L, subject, "Another test ticket");
        ticket.setId(1L);

        SolvedTicketEntity solvedTicket = new SolvedTicketEntity(ticket, 2L);
        solvedTicket.setId(ticket.getId());
        solvedTicket.setSolvedAt(LocalDateTime.of(2025, 1, 1, 10, 0));

        // Assert
        assertThat(solvedTicket.getId(), is(equalTo(1L)));
        assertThat(solvedTicket.getTicket(), is(equalTo(ticket)));
        assertThat(solvedTicket.getAttendeeId(), is(equalTo(2L)));
        assertThat(solvedTicket.getSolvedAt(), is(equalTo(LocalDateTime.of(2025, 1, 1, 10, 0))));
    }
}