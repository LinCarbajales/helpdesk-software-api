package dev.lin.helpdesk_software_api.Subject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
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
        // Simular el comportamiento del repositorio:
        // cuando se llame a findAll(), devolver la lista de entidades de prueba.
        when(subjectRepository.findAll()).thenReturn(Arrays.asList(subjectEntity1, subjectEntity2));

        // Llamar al método del servicio que se está probando
        List<SubjectResponseDTO> result = subjectService.getAllEntities();

        // Verificar el resultado con Hamcrest
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(2));
        assertThat(result.get(0).name(), is(equalTo("Software Issue")));
        assertThat(result.get(1).name(), is(equalTo("Hardware Issue")));
    }

    @Test
    @DisplayName("Should return an empty list when no subjects are found")
    void testGetAllEntities_ShouldReturnEmptyList() {
        // Simular el comportamiento del repositorio:
        // cuando se llame a findAll(), devolver una lista vacía.
        when(subjectRepository.findAll()).thenReturn(List.of());

        List<SubjectResponseDTO> result = subjectService.getAllEntities();

        // Verificar el resultado con Hamcrest
        assertThat(result, is(empty()));
    }
}