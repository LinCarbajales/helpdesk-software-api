package dev.lin.helpdesk_software_api.Ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lin.helpdesk_software_api.dtos.CombinedTicketDTO;
import dev.lin.helpdesk_software_api.dtos.TicketEditDTO;
import dev.lin.helpdesk_software_api.dtos.TicketRequestDTO;
import dev.lin.helpdesk_software_api.dtos.TicketResponseDTO;
import dev.lin.helpdesk_software_api.dtos.TicketStatusUpdateDTO;
import dev.lin.helpdesk_software_api.exceptions.TicketNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ITicketService ticketService;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("Should return all tickets")
    void testGetAllTickets_ShouldReturnAllTickets() throws Exception {
        // Arrange: Mock data for the response DTOs
        TicketResponseDTO ticket1 = new TicketResponseDTO(1L, 1L, 1L, "Test Description 1", TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());
        TicketResponseDTO ticket2 = new TicketResponseDTO(2L, 2L, 2L, "Test Description 2", TicketStatus.ATTENDED, LocalDateTime.now(), LocalDateTime.now());
        List<TicketResponseDTO> mockTickets = List.of(ticket1, ticket2);
        String json = mapper.writeValueAsString(mockTickets);

        when(ticketService.getAllEntities()).thenReturn(mockTickets);
        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/tickets"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        // Assert
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(json)));
    }

    @Test
    @DisplayName("Should return a specific ticket by ID")
    void testGetTicketById_ShouldReturnCorrectTicket() throws Exception {
        // Arrange
        Long ticketId = 1L;
        TicketResponseDTO mockTicket = new TicketResponseDTO(ticketId, 1L, 1L, "Test Description", TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());
        String json = mapper.writeValueAsString(mockTicket);

        when(ticketService.showById(ticketId)).thenReturn(mockTicket);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tickets/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("Should return 404 Not Found when ticket is not found by ID")
    void testGetTicketById_ShouldReturnNotFoundForInvalidId() throws Exception {
        // Arrange
        Long nonExistentId = 99L;
        when(ticketService.showById(nonExistentId))
                .thenThrow(new TicketNotFoundException("Ticket not found. Id " + nonExistentId + " does not exist."));

        // Act & Assert
        mockMvc.perform(get("/api/v1/tickets/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return a list of combined tickets")
    void testGetAllCombinedTickets_ShouldReturnCombinedTickets() throws Exception {
        // Arrange
        CombinedTicketDTO combinedTicket1 = new CombinedTicketDTO(1L, "Test description 1", "Requester 1", "Attendee 1", TicketStatus.ATTENDED, LocalDateTime.now(), LocalDateTime.now());
        List<CombinedTicketDTO> mockCombinedTickets = List.of(combinedTicket1);
        String json = mapper.writeValueAsString(mockCombinedTickets);

        when(ticketService.getAllCombinedTickets()).thenReturn(mockCombinedTickets);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tickets/combined"))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("Should create a new ticket")
    void testStoreEntity_ShouldCreateNewTicket() throws Exception {
        // Arrange
        TicketRequestDTO requestDTO = new TicketRequestDTO(1L, 1L, "New ticket description");
        TicketResponseDTO responseDTO = new TicketResponseDTO(1L, 1L, 1L, "New ticket description", TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());

        when(ticketService.storeEntity(any(TicketRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(responseDTO)));
    }

    @Test
    @DisplayName("Should update ticket status and return OK")
    void testUpdateTicketStatus_ShouldUpdateStatusAndReturnOk() throws Exception {
        // Arrange
        Long ticketId = 1L;
        TicketStatusUpdateDTO updateDTO = new TicketStatusUpdateDTO(TicketStatus.ATTENDED, 2L);
        TicketResponseDTO updatedTicket = new TicketResponseDTO(ticketId, 1L, 1L, "Test Description", TicketStatus.ATTENDED, LocalDateTime.now(), LocalDateTime.now());

        when(ticketService.updateTicketStatus(eq(ticketId), any(TicketStatusUpdateDTO.class))).thenReturn(updatedTicket);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/tickets/{id}/status", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(updatedTicket)));
    }

    @Test
    @DisplayName("Should return Bad Request when status update fails")
    void testUpdateTicketStatus_ShouldReturnBadRequestOnFailure() throws Exception {
        // Arrange
        Long ticketId = 1L;
        TicketStatusUpdateDTO updateDTO = new TicketStatusUpdateDTO(TicketStatus.ATTENDED, 2L);

        when(ticketService.updateTicketStatus(eq(ticketId), any(TicketStatusUpdateDTO.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/tickets/{id}/status", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should edit and update a ticket")
    void testEditTicket_ShouldEditAndReturnUpdatedTicket() throws Exception {
        // Arrange
        Long ticketId = 1L;
        TicketEditDTO editDTO = new TicketEditDTO("Edited description", 1L, 1L);
        TicketResponseDTO updatedTicket = new TicketResponseDTO(ticketId, 1L, 1L, "Edited description", TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());

        when(ticketService.updateTicket(eq(ticketId), any(TicketEditDTO.class))).thenReturn(updatedTicket);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/tickets/{id}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(editDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(updatedTicket)));
    }

    @Test
    @DisplayName("Should delete a ticket and return No Content")
    void testDeleteTicket_ShouldDeleteTicketAndReturnNoContent() throws Exception {
        // Arrange
        Long ticketId = 1L;
        doNothing().when(ticketService).deleteTicket(ticketId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/tickets/{id}", ticketId))
                .andExpect(status().isNoContent());
        
        // Verify that the delete method was called exactly once
        verify(ticketService, times(1)).deleteTicket(ticketId);
    }
    
    @Test
    @DisplayName("Should return 404 Not Found when deleting a non-existent ticket")
    void testDeleteTicket_ShouldReturnNotFoundForInvalidId() throws Exception {
        // Arrange
        Long nonExistentId = 99L;
        doThrow(new TicketNotFoundException("Ticket not found.")).when(ticketService).deleteTicket(nonExistentId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/tickets/{id}", nonExistentId))
                .andExpect(status().isNotFound());

        // Verify that the delete method was called
        verify(ticketService, times(1)).deleteTicket(nonExistentId);
    }
}
