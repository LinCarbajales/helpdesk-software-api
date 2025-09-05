package dev.lin.helpdesk_software_api.SolvedTicket;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lin.helpdesk_software_api.dtos.TicketResponseDTO;
import dev.lin.helpdesk_software_api.dtos.SolvedTicketResponseDTO;
import dev.lin.helpdesk_software_api.exceptions.SolvedTicketNotFoundException;
import dev.lin.helpdesk_software_api.Ticket.TicketStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SolvedTicketController.class)
public class SolvedTicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SolvedTicketServiceImpl solvedTicketService;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("Should return all solved tickets")
    void testGetAllSolvedTickets_ShouldReturnAllSolvedTickets() throws Exception {
        // Arrange
        TicketResponseDTO ticketDTO1 = new TicketResponseDTO(1L, 1L, 1L, "Ticket 1", TicketStatus.ATTENDED, LocalDateTime.now(), LocalDateTime.now());
        TicketResponseDTO ticketDTO2 = new TicketResponseDTO(2L, 2L, 2L, "Ticket 2", TicketStatus.ATTENDED, LocalDateTime.now(), LocalDateTime.now());

        SolvedTicketResponseDTO solvedTicket1 = new SolvedTicketResponseDTO(1L, ticketDTO1, 2L, LocalDateTime.now());
        SolvedTicketResponseDTO solvedTicket2 = new SolvedTicketResponseDTO(2L, ticketDTO2, 3L, LocalDateTime.now());
        List<SolvedTicketResponseDTO> mockSolvedTickets = List.of(solvedTicket1, solvedTicket2);
        String json = mapper.writeValueAsString(mockSolvedTickets);

        when(solvedTicketService.getAllEntities()).thenReturn(mockSolvedTickets);
        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/solved_tickets"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        // Assert
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(json)));
    }

    @Test
    @DisplayName("Should return a solved ticket by ID")
    void testGetSolvedTicketById_ShouldReturnCorrectSolvedTicket() throws Exception {
        // Arrange
        Long solvedTicketId = 1L;
        TicketResponseDTO ticketDTO = new TicketResponseDTO(1L, 1L, 1L, "Test Ticket", TicketStatus.ATTENDED, LocalDateTime.now(), LocalDateTime.now());
        SolvedTicketResponseDTO mockSolvedTicket = new SolvedTicketResponseDTO(solvedTicketId, ticketDTO, 2L, LocalDateTime.now());
        String json = mapper.writeValueAsString(mockSolvedTicket);

        when(solvedTicketService.showById(solvedTicketId)).thenReturn(mockSolvedTicket);

        // Act & Assert
        mockMvc.perform(get("/api/v1/solved_tickets/{id}", solvedTicketId))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("Should return 404 Not Found when solved ticket is not found by ID")
    void testGetSolvedTicketById_ShouldReturnNotFoundForInvalidId() throws Exception {
        // Arrange
        Long nonExistentId = 99L;
        when(solvedTicketService.showById(nonExistentId))
                .thenThrow(new SolvedTicketNotFoundException("Solved ticket not found. Id " + nonExistentId + " does not exist."));

        // Act & Assert
        mockMvc.perform(get("/api/v1/solved_tickets/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }
}
