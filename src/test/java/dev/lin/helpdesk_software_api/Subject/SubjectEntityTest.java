package dev.lin.helpdesk_software_api.Subject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("SubjectEntity Tests")
class SubjectEntityTest {

    private SubjectEntity subjectEntity;

    @BeforeEach
    void setUp() {
        subjectEntity = new SubjectEntity();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create an empty SubjectEntity with the default constructor")
        void shouldCreateEmptySubjectEntity() {
            // Given & When
            SubjectEntity emptySubject = new SubjectEntity();

            // Then
            assertThat(emptySubject.getId(), is(nullValue()));
            assertThat(emptySubject.getName(), is(nullValue()));
        }

        @Test
        @DisplayName("Should create SubjectEntity with name using the parameterized constructor")
        void shouldCreateSubjectEntityWithFields() {
            // Given
            String subjectName = "Test Subject";

            // When
            SubjectEntity newSubject = new SubjectEntity(subjectName);

            // Then
            assertThat(newSubject.getName(), is(equalTo(subjectName)));
            assertThat(newSubject.getId(), is(nullValue()));
        }
    }

    @Nested
    @DisplayName("Getters and Setters Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get all properties correctly")
        void subjectEntity_ShouldSetAndGetProperties() {
            // Arrange
            Long id = 1L;
            String name = "New Subject Name";

            // Act
            subjectEntity.setId(id);
            subjectEntity.setName(name);

            // Assert
            assertThat(subjectEntity.getId(), is(equalTo(id)));
            assertThat(subjectEntity.getName(), is(equalTo(name)));
        }
    }
}
