package dev.lin.helpdesk_software_api.Subject;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.lin.helpdesk_software_api.dtos.SubjectResponseDTO;
import dev.lin.helpdesk_software_api.exceptions.SubjectNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SubjectController.class)
public class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ISubjectService subjectService;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("Should return all subjects")
    void testIndex_ShouldReturnAllSubjects() throws Exception {
        // Mock data con DTOs de respuesta
        SubjectResponseDTO subject1 = new SubjectResponseDTO(1L, "Hardware Issue");
        SubjectResponseDTO subject2 = new SubjectResponseDTO(2L, "Software Issue");
        List<SubjectResponseDTO> mockSubjects = List.of(subject1, subject2);
        String json = mapper.writeValueAsString(mockSubjects);

        when(subjectService.getAllEntities()).thenReturn(mockSubjects);
        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/subjects"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(json)));
    }

    @Test
    @DisplayName("Should return a specific subject by ID")
    void testShowById_ShouldReturnCorrectSubject() throws Exception {
        // Arrange
        Long subjectId = 1L;
        SubjectResponseDTO mockSubject = new SubjectResponseDTO(subjectId, "Hardware Issue");
        String json = mapper.writeValueAsString(mockSubject);

        when(subjectService.showById(subjectId)).thenReturn(mockSubject);

        // Act & Assert
        mockMvc.perform(get("/api/v1/subjects/{id}", subjectId))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("Should return 404 Not Found when subject is not found by ID")
    void testShowById_ShouldReturnNotFoundForInvalidId() throws Exception {
        // Arrange
        Long nonExistentId = 99L;
        when(subjectService.showById(nonExistentId))
                .thenThrow(new SubjectNotFoundException("Subject not found. Id " + nonExistentId + " does not exist."));

        // Act & Assert
        mockMvc.perform(get("/api/v1/subjects/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return an empty list when no subjects exist")
    void testIndex_ShouldReturnEmptyListWhenNoSubjectsExist() throws Exception {
        // Arrange
        when(subjectService.getAllEntities()).thenReturn(List.of());
        String json = "[]";

        // Act & Assert
        mockMvc.perform(get("/api/v1/subjects"))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }
}