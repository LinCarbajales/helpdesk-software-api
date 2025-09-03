package dev.lin.helpdesk_software_api.Ticket;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.lin.helpdesk_software_api.dtos.TicketRequestDTO;
import dev.lin.helpdesk_software_api.dtos.TicketResponseDTO;

@WebMvcTest(controllers = TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    private TicketServiceImpl ticketServiceImpl;
    private TicketResponseDTO ticketResponseDTO1;
    private TicketResponseDTO ticketResponseDTO2;
    private TicketRequestDTO ticketRequestDTO;
    private TicketUpdateRequestDTO updateRequestDTO;

    @BeforeEach
    void setUp() {
        // Datos de prueba
        ticketResponseDTO1 = new TicketResponseDTO(
            1L, 10L, 20L, "Problema con la impresora",
            TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now()
        );
        ticketResponseDTO2 = new TicketResponseDTO(
            2L, 11L, 21L, "Lentitud del sistema",
            TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now()
        );
        ticketRequestDTO = new TicketRequestDTO(1L, 2L, "Fallo en el software");
        updateRequestDTO = new TicketUpdateRequestDTO(TicketStatus.ATTENDED, 3L);
    }

    @Test
    @DisplayName("Should return all tickets")
    void testIndex_ShouldReturnAllTickets() throws Exception {
        List<TicketResponseDTO> tickets = Arrays.asList(ticketResponseDTO1, ticketResponseDTO2);
        String json = mapper.writeValueAsString(tickets);

        when(ticketServiceImpl.getAllEntities()).thenReturn(tickets);

        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/tickets"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(json)));
    }

    @Test
    void testStore_ShouldReturnStatus201() throws Exception {
        String json = mapper.writeValueAsString(ticketRequestDTO);

        when(ticketServiceImpl.storeEntity(any(TicketRequestDTO.class))).thenReturn(ticketResponseDTO1);

        MockHttpServletResponse response = mockMvc
                .perform(post("/api/v1/tickets").content(json).contentType("application/json"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        assertThat(response.getContentAsString(), containsString(ticketResponseDTO1.description()));
    }

    @Test
    void testStoreTicket_ShouldReturnStatus400_IfDescriptionIsBlank() throws Exception {
        TicketRequestDTO dto = new TicketRequestDTO(1L, 1L, "");
        String json = mapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/tickets").content(json).contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testStoreTicket_ShouldReturnNoContent_IfServiceDoesNotReturnAnyValue() throws Exception {
        String json = mapper.writeValueAsString(ticketRequestDTO);

        when(ticketServiceImpl.storeEntity(any(TicketRequestDTO.class))).thenReturn(null);

        mockMvc.perform(post("/api/v1/tickets").content(json).contentType("application/json"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateTicketStatus_ShouldReturnStatus200_And_UpdateTicket() throws Exception {
        Long ticketId = 1L;
        TicketResponseDTO updatedDTO = new TicketResponseDTO(
            1L, 10L, 20L, "Problema con la impresora",
            TicketStatus.ATTENDED, LocalDateTime.now(), LocalDateTime.now()
        );

        when(ticketServiceImpl.updateTicketStatus(any(Long.class), any(TicketUpdateRequestDTO.class)))
            .thenReturn(updatedDTO);

        MockHttpServletResponse response = mockMvc.perform(patch("/api/v1/tickets/{id}", ticketId)
                .content(mapper.writeValueAsString(updateRequestDTO))
                .contentType("application/json"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        assertThat(response.getContentAsString(), containsString(updatedDTO.status().name()));
    }
}