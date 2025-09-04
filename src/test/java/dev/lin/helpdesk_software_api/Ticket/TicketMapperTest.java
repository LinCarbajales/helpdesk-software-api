package dev.lin.helpdesk_software_api.Ticket;

import dev.lin.helpdesk_software_api.Employee.EmployeeEntity;
import dev.lin.helpdesk_software_api.Employee.EmployeeRepository;
import dev.lin.helpdesk_software_api.Employee.EmployeeRole;
import dev.lin.helpdesk_software_api.Subject.SubjectEntity;
import dev.lin.helpdesk_software_api.Subject.SubjectRepository;
import dev.lin.helpdesk_software_api.dtos.TicketRequestDTO;
import dev.lin.helpdesk_software_api.dtos.TicketResponseDTO;
import dev.lin.helpdesk_software_api.exceptions.EmployeeNotFoundException;
import dev.lin.helpdesk_software_api.exceptions.SubjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TicketMapper Tests")
class TicketMapperTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private TicketMapper ticketMapper;

    private EmployeeEntity requesterEntity;
    private SubjectEntity subjectEntity;
    private TicketEntity ticketEntity;

    @BeforeEach
    void setUp() {
        requesterEntity = new EmployeeEntity("John Doe", EmployeeRole.REQUESTER);
        requesterEntity.setId(1L);

        subjectEntity = new SubjectEntity("Software Issue");
        subjectEntity.setId(1L);

        ticketEntity = new TicketEntity(requesterEntity, subjectEntity, "Cannot log in to the system");
        ticketEntity.setId(1L);
        ticketEntity.setStatus(TicketStatus.OPEN);
        ticketEntity.setCreatedAt(LocalDateTime.now());
        ticketEntity.setUpdatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("toEntity Method Tests")
    class ToEntityTests {

        @Test
        @DisplayName("Should correctly map TicketRequestDTO to TicketEntity")
        void toEntity_ShouldMapCorrectly_WhenRepositoriesReturnData() {
            // Arrange
            TicketRequestDTO requestDTO = new TicketRequestDTO(1L, 1L, "Cannot log in to the system");
            when(employeeRepository.findById(1L)).thenReturn(Optional.of(requesterEntity));
            when(subjectRepository.findById(1L)).thenReturn(Optional.of(subjectEntity));

            // Act
            TicketEntity result = ticketMapper.toEntity(requestDTO);

            // Assert
            assertThat(result.getRequester(), is(equalTo(requesterEntity)));
            assertThat(result.getSubject(), is(equalTo(subjectEntity)));
            assertThat(result.getDescription(), is(equalTo(requestDTO.description())));
        }

        @Test
        @DisplayName("Should throw EmployeeNotFoundException when requester is not found")
        void toEntity_ShouldThrowEmployeeNotFoundException_WhenRequesterNotFound() {
            // Arrange
            TicketRequestDTO requestDTO = new TicketRequestDTO(99L, 1L, "Test description");
            when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EmployeeNotFoundException.class, () -> ticketMapper.toEntity(requestDTO));
        }

        @Test
        @DisplayName("Should throw SubjectNotFoundException when subject is not found")
        void toEntity_ShouldThrowSubjectNotFoundException_WhenSubjectNotFound() {
            // Arrange
            TicketRequestDTO requestDTO = new TicketRequestDTO(1L, 99L, "Test description");
            when(employeeRepository.findById(1L)).thenReturn(Optional.of(requesterEntity));
            when(subjectRepository.findById(99L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(SubjectNotFoundException.class, () -> ticketMapper.toEntity(requestDTO));
        }
    }

    @Nested
    @DisplayName("toDTO Method Tests")
    class ToDTOTests {

        @Test
        @DisplayName("Should correctly map TicketEntity to TicketResponseDTO")
        void toDTO_ShouldMapCorrectly() {
            // Act
            TicketResponseDTO result = ticketMapper.toDTO(ticketEntity);

            // Assert
            assertThat(result.id(), is(equalTo(ticketEntity.getId())));
            assertThat(result.requesterId(), is(equalTo(ticketEntity.getRequester().getId())));
            assertThat(result.subjectId(), is(equalTo(ticketEntity.getSubject().getId())));
            assertThat(result.description(), is(equalTo(ticketEntity.getDescription())));
            assertThat(result.status(), is(equalTo(ticketEntity.getStatus())));
            assertThat(result.createdAt(), is(equalTo(ticketEntity.getCreatedAt())));
            assertThat(result.updatedAt(), is(equalTo(ticketEntity.getUpdatedAt())));
        }
    }
}
