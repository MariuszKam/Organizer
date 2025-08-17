package model.project;

import com.organizer.model.project.ProjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProjectId Value Object Tests")
class ProjectIdTest {

    @Nested
    @DisplayName("Creation")
    class CreationTests {

        @Test
        @DisplayName("should create a new ProjectId with random UUID")
        void shouldCreateProjectId() {
            ProjectId projectId = ProjectId.newId();
            assertNotNull(projectId, "ProjectId should not be null after creation");
        }

        @Test
        @DisplayName("should create ProjectId from valid string")
        void shouldCreateProjectIdFromString() {
            String id = "123e4567-e89b-12d3-a456-426614174000";
            ProjectId projectId = ProjectId.of(id);
            assertEquals(id, projectId.toString(), "ProjectId string representation should match input string");
        }

        @Test
        @DisplayName("should convert ProjectId with spaces in string")
        void shouldConvertWithSpaces() {
            String id = "123e4567-e89b-12d3-a456-426614174000";
            ProjectId projectId = ProjectId.of(" " + id + " ");
            assertEquals(id, projectId.toString(), "ProjectId string representation should match trimmed input string");
        }

        @Test
        @DisplayName("should parse from string back to same ProjectId")
        void shouldParseFromStringBackToSameId() {
            ProjectId original = ProjectId.newId();
            ProjectId parsed = ProjectId.of(original.toString());
            assertEquals(original, parsed, "Converted ProjectId should equal original ProjectId");
        }
    }

    @Nested
    @DisplayName("Equality")
    class EqualityTests {

        @Test
        @DisplayName("should not be equal for different UUIDs")
        void shouldNotBeEqualForDifferentProjectIds() {
            ProjectId id1 = ProjectId.newId();
            ProjectId id2 = ProjectId.newId();
            assertNotEquals(id1, id2, "ProjectIds should not be equal");
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should throw exception for null UUID")
        void shouldThrowExceptionForNullUuid() {
            assertThrows(NullPointerException.class, () -> new ProjectId(null),
                    "ProjectId creation should throw NullPointerException when value is null");
        }

        @Test
        @DisplayName("should throw exception for NIL UUID (00000000-0000-0000-0000-000000000000)")
        void shouldThrowExceptionForNilUuid() {
            UUID nil = new UUID(0L, 0L);
            assertThrows(IllegalArgumentException.class, () -> new ProjectId(nil),
                    "ProjectId creation should throw IllegalArgumentException when value is NIL UUID");
        }

        @Test
        @DisplayName("should throw exception for null string")
        void shouldThrowExceptionForNullString() {
            assertThrows(NullPointerException.class, () -> ProjectId.of(null),
                    "ProjectId creation from string should throw NullPointerException when string is null");
        }

        @Test
        @DisplayName("should throw exception for invalid string")
        void shouldThrowExceptionForInvalidString() {
            assertThrows(IllegalArgumentException.class, () -> ProjectId.of("not-a-uuid"),
                    "ProjectId creation from invalid string should throw IllegalArgumentException");
        }
    }
}
