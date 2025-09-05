package dev.lin.helpdesk_software_api.SolvedTicket;

import dev.lin.helpdesk_software_api.Employee.EmployeeEntity;
import dev.lin.helpdesk_software_api.Employee.EmployeeRole;
import dev.lin.helpdesk_software_api.Subject.SubjectEntity;
import dev.lin.helpdesk_software_api.Ticket.TicketEntity;
import dev.lin.helpdesk_software_api.Ticket.TicketMapper;
import dev.lin.helpdesk_software_api.dtos.TicketResponseDTO;
import dev.lin.helpdesk_software_api.Ticket.TicketStatus;
import dev.lin.helpdesk_software_api.dtos.SolvedTicketResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SolvedTicketMapper Tests")
class SolvedTicketMapperTest {

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private SolvedTicketMapper solvedTicketMapper;

    private SolvedTicketEntity solvedTicketEntity;
    private TicketEntity ticketEntity;
    private EmployeeEntity requesterEntity;
    private EmployeeEntity attendeeEntity;

    @BeforeEach
    void setUp() {
        requesterEntity = new EmployeeEntity("Requester Name", EmployeeRole.REQUESTER);
        requesterEntity.setId(1L);

        attendeeEntity = new EmployeeEntity("Attendee Name", EmployeeRole.TECHNICIAN);
        attendeeEntity.setId(2L);

        SubjectEntity subject = new SubjectEntity("Test Subject");
        subject.setId(1L);

        ticketEntity = new TicketEntity(requesterEntity, subject, "Test description");
        ticketEntity.setId(1L);
        ticketEntity.setStatus(TicketStatus.ATTENDED);
        ticketEntity.setCreatedAt(LocalDateTime.now().minusDays(1));
        ticketEntity.setUpdatedAt(LocalDateTime.now().minusDays(1));

        solvedTicketEntity = new SolvedTicketEntity(ticketEntity, attendeeEntity);
        solvedTicketEntity.setId(1L);
        solvedTicketEntity.setSolvedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should correctly map SolvedTicketEntity to SolvedTicketResponseDTO")
    void toDTO_ShouldMapCorrectly() {
        // Arrange
        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO(
            ticketEntity.getId(),
            ticketEntity.getRequester().getId(),
            ticketEntity.getSubject().getId(),
            ticketEntity.getDescription(),
            ticketEntity.getStatus(),
            ticketEntity.getCreatedAt(),
            ticketEntity.getUpdatedAt()
        );

        when(ticketMapper.toDTO(ticketEntity)).thenReturn(ticketResponseDTO);

        // Act
        SolvedTicketResponseDTO result = solvedTicketMapper.toDTO(solvedTicketEntity);

        // Assert
        assertThat(result.id(), is(equalTo(solvedTicketEntity.getId())));
        assertThat(result.ticket().id(), is(equalTo(ticketEntity.getId())));
        assertThat(result.attendeeId(), is(equalTo(attendeeEntity.getId())));
        assertThat(result.solvedAt(), is(equalTo(solvedTicketEntity.getSolvedAt())));
    }

    @Test
    @DisplayName("Should return null when SolvedTicketEntity is null")
    void toDTO_ShouldReturnNull_WhenEntityIsNull() {
        // Act
        SolvedTicketResponseDTO result = solvedTicketMapper.toDTO(null);

        // Assert
        assertThat(result, is(nullValue()));
    }
}
