package model.task;

import com.organizer.model.task.TaskId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskId Value Object Tests")
class TaskIdTest {

    @Nested
    @DisplayName("Creation")
    class CreationTests {

        @Test
        @DisplayName("should create a new TaskId with random UUID")
        void shouldCreateTaskId() {
            TaskId taskId = TaskId.newId();
            assertNotNull(taskId, "TaskId should not be null after creation");
        }

        @Test
        @DisplayName("should create TaskId from valid string")
        void shouldCreateTaskIdFromString() {
            String id = "123e4567-e89b-12d3-a456-426614174000";
            TaskId taskId = TaskId.of(id);
            assertEquals(id, taskId.toString(), "TaskId string representation should match input string");
        }

        @Test
        @DisplayName("should convert TaskId with spaces in string")
        void shouldConvertWithSpaces() {
            String id = "123e4567-e89b-12d3-a456-426614174000";
            TaskId taskId = TaskId.of(" " + id + " ");
            assertEquals(id, taskId.toString(), "TaskId string representation should match trimmed input string");
        }

        @Test
        @DisplayName("should parse from string back to same TaskId")
        void shouldParseFromStringBackToSameId() {
            TaskId original = TaskId.newId();
            TaskId parsed = TaskId.of(original.toString());
            assertEquals(original, parsed, "Converted TaskId should equal original TaskId");
        }
    }

    @Nested
    @DisplayName("Equality")
    class EqualityTests {

        @Test
        @DisplayName("should not be equal for different UUIDs")
        void shouldNotBeEqualForDifferentTaskIds() {
            TaskId id1 = TaskId.newId();
            TaskId id2 = TaskId.newId();
            assertNotEquals(id1, id2, "TaskIds should not be equal");
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should throw exception for null UUID")
        void shouldThrowExceptionForNullUuid() {
            assertThrows(NullPointerException.class, () -> new TaskId(null),
                    "TaskId creation should throw NullPointerException when value is null");
        }

        @Test
        @DisplayName("should throw exception for NIL UUID (00000000-0000-0000-0000-000000000000)")
        void shouldThrowExceptionForNilUuid() {
            UUID nil = new UUID(0L, 0L);
            assertThrows(IllegalArgumentException.class, () -> new TaskId(nil),
                    "TaskId creation should throw IllegalArgumentException when value is NIL UUID");
        }

        @Test
        @DisplayName("should throw exception for null string")
        void shouldThrowExceptionForNullString() {
            assertThrows(NullPointerException.class, () -> TaskId.of(null),
                    "TaskId creation from string should throw NullPointerException when string is null");
        }

        @Test
        @DisplayName("should throw exception for invalid string")
        void shouldThrowExceptionForInvalidString() {
            assertThrows(IllegalArgumentException.class, () -> TaskId.of("not-a-uuid"),
                    "TaskId creation from invalid string should throw IllegalArgumentException");
        }
    }
}
