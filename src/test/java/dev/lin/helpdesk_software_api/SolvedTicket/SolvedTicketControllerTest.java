package dev.lin.helpdesk_software_api.SolvedTicket;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import dev.lin.helpdesk_software_api.Implementations.IReadOnlyService;
import dev.lin.helpdesk_software_api.exceptions.SolvedTicketNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SolvedTicketControllerTest {

    @Mock
    private IReadOnlyService<SolvedTicketResponseDTO> solvedTicketService;

    @InjectMocks
    private SolvedTicketController solvedTicketController;

    private SolvedTicketResponseDTO solvedTicketResponseDTO1;

    @BeforeEach
    void setUp() {
        solvedTicketResponseDTO1 = new SolvedTicketResponseDTO(1L, null, 2L, null);
    }

    @Test
    @DisplayName("Should return a list of all solved tickets")
    void getAllSolvedTickets_ShouldReturnListOfSolvedTickets() {
        // Arrange
        List<SolvedTicketResponseDTO> mockSolvedTickets = Arrays.asList(solvedTicketResponseDTO1);
        when(solvedTicketService.getAllEntities()).thenReturn(mockSolvedTickets);

        // Act
        List<SolvedTicketResponseDTO> result = solvedTicketController.getAllSolvedTickets();

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(1));
        assertThat(result.get(0).id(), is(equalTo(1L)));
        verify(solvedTicketService, times(1)).getAllEntities();
    }

    @Test
    @DisplayName("Should return a solved ticket when found by ID")
    void getSolvedTicketById_ShouldReturnSolvedTicketWhenFound() {
        // Arrange
        when(solvedTicketService.showById(1L)).thenReturn(solvedTicketResponseDTO1);

        // Act
        SolvedTicketResponseDTO result = solvedTicketController.getSolvedTicketById(1L);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.id(), is(equalTo(1L)));
        verify(solvedTicketService, times(1)).showById(1L);
    }

    @Test
    @DisplayName("Should throw SolvedTicketNotFoundException when solved ticket not found by ID")
    void getSolvedTicketById_ShouldThrowExceptionWhenNotFound() {
        // Arrange
        when(solvedTicketService.showById(1L)).thenThrow(new SolvedTicketNotFoundException("Not found"));

        // Act & Assert
        assertThrows(SolvedTicketNotFoundException.class, () -> solvedTicketController.getSolvedTicketById(1L));
        verify(solvedTicketService, times(1)).showById(1L);
    }
}