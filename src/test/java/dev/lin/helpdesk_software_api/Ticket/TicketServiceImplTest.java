package dev.lin.helpdesk_software_api.Ticket;

import dev.lin.helpdesk_software_api.Employee.EmployeeEntity;
import dev.lin.helpdesk_software_api.Employee.EmployeeRepository;
import dev.lin.helpdesk_software_api.SolvedTicket.SolvedTicketEntity;
import dev.lin.helpdesk_software_api.SolvedTicket.SolvedTicketRepository;
import dev.lin.helpdesk_software_api.Subject.SubjectEntity;
import dev.lin.helpdesk_software_api.Subject.SubjectRepository;
import dev.lin.helpdesk_software_api.dtos.TicketRequestDTO;
import dev.lin.helpdesk_software_api.dtos.TicketResponseDTO;
import dev.lin.helpdesk_software_api.dtos.TicketUpdateRequestDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private SolvedTicketRepository solvedTicketRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeEntity mockRequester;

    private EmployeeEntity mockAttendee;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private TicketEntity ticketEntity;
    private TicketRequestDTO ticketRequestDTO;
    private TicketResponseDTO ticketResponseDTO;
    private SubjectEntity subjectEntity;
    private TicketUpdateRequestDTO updateRequest;

    @BeforeEach
    void setUp() {
        subjectEntity = new SubjectEntity();
        subjectEntity.setId(1L);

        // Se han creado los mocks para los empleados
        mockRequester = new EmployeeEntity("Requester Name", null);
        mockRequester.setId(1L);
        mockAttendee = new EmployeeEntity("Attendee Name", null);
        mockAttendee.setId(2L);

        ticketEntity = new TicketEntity();
        ticketEntity.setId(1L);
        // Se ha cambiado a un objeto EmployeeEntity
        ticketEntity.setRequester(mockRequester);
        ticketEntity.setSubject(subjectEntity);
        ticketEntity.setDescription("Test description");
        ticketEntity.setStatus(TicketStatus.OPEN);
        ticketEntity.setCreatedAt(LocalDateTime.now());
        ticketEntity.setUpdatedAt(LocalDateTime.now());

        ticketRequestDTO = new TicketRequestDTO(1L, 1L, "Test description");
        ticketResponseDTO = new TicketResponseDTO(1L, 1L, 1L, "Test description", 
            TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());

        updateRequest = new TicketUpdateRequestDTO(TicketStatus.ATTENDED, 2L);
    }

    @Test
    void getAllEntities_ShouldReturnOrderedTickets() {
        // Arrange
        List<TicketEntity> ticketEntities = Arrays.asList(ticketEntity);
        when(ticketRepository.findAllByOrderByCreatedAtAsc()).thenReturn(ticketEntities);
        when(ticketMapper.toDTO(any(TicketEntity.class))).thenReturn(ticketResponseDTO);

        // Act
        List<TicketResponseDTO> result = ticketService.getAllEntities();

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(1));
        verify(ticketRepository, times(1)).findAllByOrderByCreatedAtAsc();
        verify(ticketMapper, times(1)).toDTO(ticketEntity);
    }

    @Test
    void storeEntity_WithValidRequest_ShouldSaveAndReturnTicket() {
        // Arrange
        when(ticketMapper.toEntity(ticketRequestDTO)).thenReturn(ticketEntity);
        when(ticketRepository.save(ticketEntity)).thenReturn(ticketEntity);
        when(ticketMapper.toDTO(ticketEntity)).thenReturn(ticketResponseDTO);

        // Act
        TicketResponseDTO result = ticketService.storeEntity(ticketRequestDTO);

        // Assert
        assertThat(result, equalTo(ticketResponseDTO));
        verify(ticketMapper, times(1)).toEntity(ticketRequestDTO);
        verify(ticketRepository, times(1)).save(ticketEntity);
        verify(ticketMapper, times(1)).toDTO(ticketEntity);
    }

    @Test
    void updateTicketStatus_WithValidTransition_ShouldUpdateStatusAndCreateSolvedTicket() {
        // Arrange
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketEntity));
        // Se ha añadido esta línea para mockear la búsqueda del empleado
        when(employeeRepository.findById(updateRequest.attendeeId())).thenReturn(Optional.of(mockAttendee));
        when(ticketRepository.save(ticketEntity)).thenReturn(ticketEntity);
        when(ticketMapper.toDTO(ticketEntity)).thenReturn(ticketResponseDTO);
        when(solvedTicketRepository.save(any(SolvedTicketEntity.class))).thenReturn(new SolvedTicketEntity());

        // Act
        TicketResponseDTO result = ticketService.updateTicketStatus(1L, updateRequest);

        // Assert
        assertThat(result, equalTo(ticketResponseDTO));
        assertThat(ticketEntity.getStatus(), equalTo(TicketStatus.ATTENDED));
        verify(ticketRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findById(updateRequest.attendeeId());
        verify(ticketRepository, times(1)).save(ticketEntity);
        verify(solvedTicketRepository, times(1)).save(any(SolvedTicketEntity.class));
        verify(ticketMapper, times(1)).toDTO(ticketEntity);
    }

    @Test
    void updateTicketStatus_WithInvalidTransition_ShouldReturnNull() {
        // Arrange
        ticketEntity.setStatus(TicketStatus.ATTENDED); // Already attended
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketEntity));

        // Act
        TicketResponseDTO result = ticketService.updateTicketStatus(1L, updateRequest);

        // Assert
        assertThat(result, nullValue());
        verify(ticketRepository, times(1)).findById(1L);
        verify(employeeRepository, never()).findById(anyLong());
        verify(ticketRepository, never()).save(any());
        verify(solvedTicketRepository, never()).save(any());
    }

    @Test
    void updateTicketStatus_WithNonAttendedStatus_ShouldReturnNull() {
        // Arrange
        TicketUpdateRequestDTO invalidUpdate = new TicketUpdateRequestDTO(TicketStatus.OPEN, 2L);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketEntity));

        // Act
        TicketResponseDTO result = ticketService.updateTicketStatus(1L, invalidUpdate);

        // Assert
        assertThat(result, nullValue());
        verify(ticketRepository, times(1)).findById(1L);
        verify(employeeRepository, never()).findById(anyLong());
        verify(ticketRepository, never()).save(any());
        verify(solvedTicketRepository, never()).save(any());
    }

    @Test
    void updateTicketStatus_WhenTicketNotFound_ShouldReturnNull() {
        // Arrange
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        TicketResponseDTO result = ticketService.updateTicketStatus(1L, updateRequest);

        // Assert
        assertThat(result, nullValue());
        verify(ticketRepository, times(1)).findById(1L);
        verify(employeeRepository, never()).findById(anyLong());
        verify(ticketRepository, never()).save(any());
        verify(solvedTicketRepository, never()).save(any());
    }

    @Test
    void updateTicketStatus_WithInvalidStatusTransition_ShouldReturnNull() {
        // Arrange
        // Create a scenario where transition is not allowed
        ticketEntity.setStatus(TicketStatus.ATTENDED); // Can't transition from ATTENDED to ATTENDED
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketEntity));

        // Act
        TicketResponseDTO result = ticketService.updateTicketStatus(1L, updateRequest);

        // Assert
        assertThat(result, nullValue());
        verify(ticketRepository, times(1)).findById(1L);
        verify(employeeRepository, never()).findById(anyLong());
        verify(ticketRepository, never()).save(any());
        verify(solvedTicketRepository, never()).save(any());
    }
}