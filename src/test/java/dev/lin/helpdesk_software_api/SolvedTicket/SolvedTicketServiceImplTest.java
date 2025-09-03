package dev.lin.helpdesk_software_api.SolvedTicket;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import dev.lin.helpdesk_software_api.Employee.EmployeeEntity;
import dev.lin.helpdesk_software_api.Subject.SubjectEntity;
import dev.lin.helpdesk_software_api.exceptions.SolvedTicketNotFoundException;
import dev.lin.helpdesk_software_api.Ticket.TicketEntity;
import dev.lin.helpdesk_software_api.dtos.SolvedTicketResponseDTO;
import dev.lin.helpdesk_software_api.dtos.TicketResponseDTO;

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
class SolvedTicketServiceImplTest {

    @Mock
    private SolvedTicketRepository solvedTicketRepository;

    @Mock
    private SolvedTicketMapper solvedTicketMapper;

    @InjectMocks
    private SolvedTicketServiceImpl solvedTicketService;

    private SolvedTicketEntity solvedTicket1;
    private SolvedTicketResponseDTO solvedTicketResponseDTO1;
    private EmployeeEntity mockAttendee;

    @BeforeEach
    void setUp() {
        SubjectEntity subject1 = new SubjectEntity("Hardware Issue");
        TicketEntity ticket1 = new TicketEntity(null, subject1, "Ticket 1");
        ticket1.setId(1L);

        mockAttendee = new EmployeeEntity("John Doe", null);
        mockAttendee.setId(2L);
        
        // Se ha modificado el constructor para usar EmployeeEntity en lugar de Long
        solvedTicket1 = new SolvedTicketEntity(ticket1, mockAttendee);
        solvedTicket1.setId(1L);

        TicketResponseDTO ticketResponseDTO1 = new TicketResponseDTO(1L, 1L, 1L, "Ticket 1", null, null, null);
        // Se ha modificado el constructor del DTO para usar el ID del empleado
        solvedTicketResponseDTO1 = new SolvedTicketResponseDTO(1L, ticketResponseDTO1, 2L, null);
    }

    @Test
    @DisplayName("Should return a list of all solved tickets")
    void getAllEntities_ShouldReturnAllSolvedTickets() {
        // Arrange
        List<SolvedTicketEntity> mockEntities = Arrays.asList(solvedTicket1);
        when(solvedTicketRepository.findAll()).thenReturn(mockEntities);
        when(solvedTicketMapper.toDTO(solvedTicket1)).thenReturn(solvedTicketResponseDTO1);

        // Act
        List<SolvedTicketResponseDTO> result = solvedTicketService.getAllEntities();

        // Assert
        assertThat(result, hasSize(1));
        assertThat(result.get(0).id(), is(equalTo(1L)));
        verify(solvedTicketRepository, times(1)).findAll();
        verify(solvedTicketMapper, times(1)).toDTO(solvedTicket1);
    }

    @Test
    @DisplayName("Should return a solved ticket when found by ID")
    void showById_ShouldReturnSolvedTicketWhenFound() {
        // Arrange
        when(solvedTicketRepository.findById(1L)).thenReturn(Optional.of(solvedTicket1));
        when(solvedTicketMapper.toDTO(solvedTicket1)).thenReturn(solvedTicketResponseDTO1);

        // Act
        SolvedTicketResponseDTO result = solvedTicketService.showById(1L);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.id(), is(equalTo(1L)));
        verify(solvedTicketRepository, times(1)).findById(1L);
        verify(solvedTicketMapper, times(1)).toDTO(solvedTicket1);
    }

    @Test
    @DisplayName("Should throw SolvedTicketNotFoundException when solved ticket not found by ID")
    void showById_ShouldThrowExceptionWhenNotFound() {
        // Arrange
        when(solvedTicketRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SolvedTicketNotFoundException.class, () -> solvedTicketService.showById(1L));
        verify(solvedTicketRepository, times(1)).findById(1L);
        verify(solvedTicketMapper, never()).toDTO(any());
    }
}