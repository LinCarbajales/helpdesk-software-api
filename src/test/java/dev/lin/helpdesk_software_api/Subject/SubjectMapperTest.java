package dev.lin.helpdesk_software_api.Subject;

import dev.lin.helpdesk_software_api.dtos.SubjectResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("SubjectMapper Tests")
class SubjectMapperTest {

    private SubjectEntity subjectEntity;

    @BeforeEach
    void setUp() {
        subjectEntity = new SubjectEntity("Software Issue");
        subjectEntity.setId(1L);
    }

    @Test
    @DisplayName("Should correctly map SubjectEntity to SubjectResponseDTO")
    void toDTO_ShouldMapCorrectly() {
        // Act
        SubjectResponseDTO result = SubjectMapper.toDTO(subjectEntity);

        // Assert
        assertThat(result.id(), is(equalTo(subjectEntity.getId())));
        assertThat(result.name(), is(equalTo(subjectEntity.getName())));
    }
}
