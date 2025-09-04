package dev.lin.helpdesk_software_api.Ticket;

import dev.lin.helpdesk_software_api.Employee.EmployeeEntity;
import dev.lin.helpdesk_software_api.Employee.EmployeeRepository;
import dev.lin.helpdesk_software_api.SolvedTicket.SolvedTicketEntity;
import dev.lin.helpdesk_software_api.SolvedTicket.SolvedTicketRepository;
import dev.lin.helpdesk_software_api.Subject.SubjectEntity;
import dev.lin.helpdesk_software_api.Subject.SubjectRepository;
import dev.lin.helpdesk_software_api.dtos.*;
import dev.lin.helpdesk_software_api.exceptions.EmployeeNotFoundException;
import dev.lin.helpdesk_software_api.exceptions.SubjectNotFoundException;
import dev.lin.helpdesk_software_api.exceptions.TicketNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TicketServiceImpl Tests")
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

    @InjectMocks
    private TicketServiceImpl ticketService;

    private TicketEntity ticketEntity;
    private TicketRequestDTO ticketRequestDTO;
    private TicketResponseDTO ticketResponseDTO;
    private SubjectEntity subjectEntity;
    private EmployeeEntity requesterEntity;
    private EmployeeEntity attendeeEntity;

    @BeforeEach
    void setUp() {
        subjectEntity = new SubjectEntity("Test Subject");
        subjectEntity.setId(1L);

        requesterEntity = new EmployeeEntity("Requester Name", null);
        requesterEntity.setId(1L);

        attendeeEntity = new EmployeeEntity("Attendee Name", null);
        attendeeEntity.setId(2L);

        ticketEntity = new TicketEntity(requesterEntity, subjectEntity, "Test description");
        ticketEntity.setId(1L);
        ticketEntity.setStatus(TicketStatus.OPEN);

        ticketRequestDTO = new TicketRequestDTO(1L, 1L, "Test description");
        ticketResponseDTO = new TicketResponseDTO(1L, 1L, 1L, "Test description",
                TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());
    }

    @Nested
    @DisplayName("Read Operations Tests")
    class ReadOperationsTests {

        @Test
        @DisplayName("getAllEntities should return a list of ordered tickets")
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
        @DisplayName("showById should return the correct ticket when found")
        void showById_ShouldReturnCorrectTicketWhenFound() {
            // Arrange
            when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketEntity));
            when(ticketMapper.toDTO(ticketEntity)).thenReturn(ticketResponseDTO);

            // Act
            TicketResponseDTO result = ticketService.showById(1L);

            // Assert
            assertThat(result, is(equalTo(ticketResponseDTO)));
            verify(ticketRepository, times(1)).findById(1L);
            verify(ticketMapper, times(1)).toDTO(ticketEntity);
        }

        @Test
        @DisplayName("showById should throw TicketNotFoundException when ticket not found")
        void showById_ShouldThrowExceptionWhenNotFound() {
            // Arrange
            when(ticketRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(TicketNotFoundException.class, () -> ticketService.showById(99L));
            verify(ticketRepository, times(1)).findById(99L);
        }

        @Test
        @DisplayName("getAllCombinedTickets should return a list of combined tickets")
        void getAllCombinedTickets_ShouldReturnCombinedTickets() {
            // Arrange
            CombinedTicketDTO combinedTicketDTO = new CombinedTicketDTO(1L, "Test description", "Requester Name", "Attendee Name",
                TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());
            List<CombinedTicketDTO> combinedTickets = Arrays.asList(combinedTicketDTO);
            when(ticketRepository.findAllCombinedTickets()).thenReturn(combinedTickets);

            // Act
            List<CombinedTicketDTO> result = ticketService.getAllCombinedTickets();

            // Assert
            assertThat(result, is(notNullValue()));
            assertThat(result, hasSize(1));
            assertThat(result.get(0).description(), is(equalTo("Test description")));
            verify(ticketRepository, times(1)).findAllCombinedTickets();
        }
    }

    @Nested
    @DisplayName("Create and Update Operations Tests")
    class CreateUpdateOperationsTests {

        @Test
        @DisplayName("storeEntity should save and return a new ticket")
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
        @DisplayName("updateTicketStatus should update status and create solved ticket for valid transition")
        void updateTicketStatus_WithValidTransition_ShouldUpdateStatusAndCreateSolvedTicket() {
            // Arrange
            TicketStatusUpdateDTO updateRequest = new TicketStatusUpdateDTO(TicketStatus.ATTENDED, attendeeEntity.getId());
            when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketEntity));
            when(employeeRepository.findById(updateRequest.attendeeId())).thenReturn(Optional.of(attendeeEntity));
            when(ticketRepository.save(any(TicketEntity.class))).thenReturn(ticketEntity);
            when(ticketMapper.toDTO(any(TicketEntity.class))).thenReturn(ticketResponseDTO);

            // Act
            TicketResponseDTO result = ticketService.updateTicketStatus(1L, updateRequest);

            // Assert
            assertThat(result, equalTo(ticketResponseDTO));
            verify(ticketRepository, times(1)).findById(1L);
            verify(employeeRepository, times(1)).findById(updateRequest.attendeeId());
            verify(solvedTicketRepository, times(1)).save(any(SolvedTicketEntity.class));
            verify(ticketRepository, times(1)).save(ticketEntity);
            verify(ticketMapper, times(1)).toDTO(ticketEntity);
        }
        
        @Test
        @DisplayName("updateTicketStatus should return null when ticket is not found")
        void updateTicketStatus_WhenTicketNotFound_ShouldReturnNull() {
            // Arrange
            TicketStatusUpdateDTO updateRequest = new TicketStatusUpdateDTO(TicketStatus.ATTENDED, attendeeEntity.getId());
            when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

            // Act
            TicketResponseDTO result = ticketService.updateTicketStatus(1L, updateRequest);

            // Assert
            assertThat(result, nullValue());
            verify(ticketRepository, times(1)).findById(1L);
            verify(employeeRepository, never()).findById(anyLong());
        }

        @Test
        @DisplayName("updateTicketStatus should throw EmployeeNotFoundException when attendee not found")
        void updateTicketStatus_ShouldThrowExceptionWhenAttendeeNotFound() {
            // Arrange
            TicketStatusUpdateDTO updateRequest = new TicketStatusUpdateDTO(TicketStatus.ATTENDED, 99L);
            when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketEntity));
            when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EmployeeNotFoundException.class, () -> ticketService.updateTicketStatus(1L, updateRequest));
            verify(ticketRepository, times(1)).findById(1L);
            verify(employeeRepository, times(1)).findById(99L);
            verify(solvedTicketRepository, never()).save(any());
        }

        @Test
        @DisplayName("updateTicket should update a ticket correctly")
        void updateTicket_ShouldUpdateTicketCorrectly() {
            // Arrange
            TicketEditDTO editDTO = new TicketEditDTO("Updated description", 1L, 1L);
            when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketEntity));
            when(employeeRepository.findById(editDTO.requesterId())).thenReturn(Optional.of(requesterEntity));
            when(subjectRepository.findById(editDTO.subjectId())).thenReturn(Optional.of(subjectEntity));
            when(ticketRepository.save(any(TicketEntity.class))).thenReturn(ticketEntity);
            when(ticketMapper.toDTO(any(TicketEntity.class))).thenReturn(ticketResponseDTO);

            // Act
            TicketResponseDTO result = ticketService.updateTicket(1L, editDTO);

            // Assert
            assertThat(result, is(equalTo(ticketResponseDTO)));
            verify(ticketRepository, times(1)).findById(1L);
            verify(employeeRepository, times(1)).findById(editDTO.requesterId());
            verify(subjectRepository, times(1)).findById(editDTO.subjectId());
            verify(ticketRepository, times(1)).save(any(TicketEntity.class));
        }

        @Test
        @DisplayName("updateTicket should throw TicketNotFoundException when ticket not found")
        void updateTicket_ShouldThrowTicketNotFoundException() {
            // Arrange
            TicketEditDTO editDTO = new TicketEditDTO("Updated description", 1L, 1L);
            when(ticketRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(TicketNotFoundException.class, () -> ticketService.updateTicket(1L, editDTO));
            verify(ticketRepository, times(1)).findById(1L);
            verify(employeeRepository, never()).findById(anyLong());
        }

        @Test
        @DisplayName("updateTicket should throw EmployeeNotFoundException when requester not found")
        void updateTicket_ShouldThrowEmployeeNotFoundException() {
            // Arrange
            TicketEditDTO editDTO = new TicketEditDTO("Updated description", 99L, 1L);
            when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketEntity));
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EmployeeNotFoundException.class, () -> ticketService.updateTicket(1L, editDTO));
            verify(ticketRepository, times(1)).findById(1L);
            verify(employeeRepository, times(1)).findById(99L);
            verify(subjectRepository, never()).findById(anyLong());
        }

        @Test
        @DisplayName("updateTicket should throw SubjectNotFoundException when subject not found")
        void updateTicket_ShouldThrowSubjectNotFoundException() {
            // Arrange
            TicketEditDTO editDTO = new TicketEditDTO("Updated description", 1L, 99L);
            when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketEntity));
            when(employeeRepository.findById(1L)).thenReturn(Optional.of(requesterEntity));
            when(subjectRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(SubjectNotFoundException.class, () -> ticketService.updateTicket(1L, editDTO));
            verify(ticketRepository, times(1)).findById(1L);
            verify(employeeRepository, times(1)).findById(1L);
            verify(subjectRepository, times(1)).findById(99L);
        }
    }

    @Nested
    @DisplayName("Delete Operations Tests")
    class DeleteOperationsTests {

        @Test
        @DisplayName("deleteTicket should delete a ticket when found")
        void deleteTicket_ShouldDeleteTicketWhenFound() {
            // Arrange
            when(ticketRepository.existsById(1L)).thenReturn(true);
            doNothing().when(ticketRepository).deleteById(1L);

            // Act
            ticketService.deleteTicket(1L);

            // Assert
            verify(ticketRepository, times(1)).existsById(1L);
            verify(ticketRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("deleteTicket should throw TicketNotFoundException when ticket not found")
        void deleteTicket_ShouldThrowExceptionWhenNotFound() {
            // Arrange
            when(ticketRepository.existsById(anyLong())).thenReturn(false);

            // Act & Assert
            assertThrows(TicketNotFoundException.class, () -> ticketService.deleteTicket(99L));
            verify(ticketRepository, times(1)).existsById(99L);
            verify(ticketRepository, never()).deleteById(anyLong());
        }
    }
}
