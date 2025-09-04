package dev.lin.helpdesk_software_api.Subject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.lin.helpdesk_software_api.dtos.SubjectResponseDTO;
import dev.lin.helpdesk_software_api.exceptions.SubjectNotFoundException;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("SubjectServiceImpl Tests")
class SubjectServiceImplTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    private SubjectEntity subjectEntity1;
    private SubjectEntity subjectEntity2;

    @BeforeEach
    void setUp() {
        subjectEntity1 = new SubjectEntity("Software Issue");
        subjectEntity1.setId(1L);

        subjectEntity2 = new SubjectEntity("Hardware Issue");
        subjectEntity2.setId(2L);
    }

    @Test
    @DisplayName("Should return a list of all subjects")
    void testGetAllEntities_ShouldReturnAllSubjects() {
        // Arrange
        when(subjectRepository.findAll()).thenReturn(Arrays.asList(subjectEntity1, subjectEntity2));

        // Act
        List<SubjectResponseDTO> result = subjectService.getAllEntities();

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(2));
        assertThat(result.get(0).name(), is(equalTo("Software Issue")));
        assertThat(result.get(1).name(), is(equalTo("Hardware Issue")));
    }

    @Test
    @DisplayName("Should return an empty list when no subjects are found")
    void testGetAllEntities_ShouldReturnEmptyList() {
        // Arrange
        when(subjectRepository.findAll()).thenReturn(List.of());

        // Act
        List<SubjectResponseDTO> result = subjectService.getAllEntities();

        // Assert
        assertThat(result, is(empty()));
    }

    @Test
    @DisplayName("Should return the subject by id when found")
    void testShowById_ShouldReturnSubjectWhenFound() {
        // Arrange
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subjectEntity1));

        // Act
        SubjectResponseDTO result = subjectService.showById(1L);

        // Assert
        assertThat(result.id(), is(equalTo(1L)));
        assertThat(result.name(), is(equalTo("Software Issue")));
    }

    @Test
    @DisplayName("Should throw SubjectNotFoundException when subject is not found")
    void testShowById_ShouldThrowExceptionWhenNotFound() {
        // Arrange
        when(subjectRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SubjectNotFoundException.class, () -> subjectService.showById(99L));
    }
}