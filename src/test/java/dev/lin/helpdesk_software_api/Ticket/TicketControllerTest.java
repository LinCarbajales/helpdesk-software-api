package dev.lin.helpdesk_software_api.Ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lin.helpdesk_software_api.Implementations.IGenericService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IGenericService<TicketResponseDTO, TicketRequestDTO> ticketService;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("Should return all tickets")
    void testIndex_ShouldReturnAllTickets() throws Exception {
        // Mock data utilizando los DTOs de respuesta
        TicketResponseDTO ticket1 = new TicketResponseDTO(1L, 4L, 2L, "Se cuelga el Windows.", TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());
        TicketResponseDTO ticket2 = new TicketResponseDTO(2L, 1L, 1L, "No va el micrófono.", TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());
        List<TicketResponseDTO> mockTickets = List.of(ticket1, ticket2);
        String json = mapper.writeValueAsString(mockTickets);

        when(ticketService.getAllEntities()).thenReturn(mockTickets);
        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/tickets"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(json)));
    }

    @Test
    @DisplayName("Should return status 201 when creating a new ticket")
    void testStore_ShouldReturnStatus201() throws Exception {
        // Datos para la petición, utilizando el DTO de petición
        TicketRequestDTO dto = new TicketRequestDTO(9L, 3L, "Se cuelga Internet.");
        // Datos para la respuesta, utilizando el DTO de respuesta
        TicketResponseDTO createdTicket = new TicketResponseDTO(10L, 9L, 3L, "Se cuelga Internet.", TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());
        String jsonRequest = mapper.writeValueAsString(dto);
        String jsonResponse = mapper.writeValueAsString(createdTicket);

        when(ticketService.storeEntity(dto)).thenReturn(createdTicket);
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/tickets")
                        .content(jsonRequest)
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        assertThat(response.getContentAsString(), is(equalTo(jsonResponse)));
    }

    @Test
    @DisplayName("Should return status 400 if description is blank")
    void testStoreTicket_ShouldReturnStatus400_IfDescriptionIsBlank() throws Exception {
        TicketRequestDTO dto = new TicketRequestDTO(9L, 3L, "");
        String json = mapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/tickets")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return status 204 if service does not return any value")
    void testStoreTicket_ShouldReturnNoContent_IfServiceDoesNotReturnAnyValue() throws Exception {
        TicketRequestDTO dto = new TicketRequestDTO(9L, 3L, "Se cuelga Internet.");
        String json = mapper.writeValueAsString(dto);

        when(ticketService.storeEntity(dto)).thenReturn(null);
        mockMvc.perform(post("/api/v1/tickets")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isNoContent());
    }
}