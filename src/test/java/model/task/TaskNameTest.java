package model.task;

import com.organizer.model.task.TaskName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskName Value Object Tests")
class TaskNameTest {

    private static final String VALID_NAME = "Implement login feature";

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should throw when name is null")
        void shouldThrowWhenNull() {
            assertThrows(NullPointerException.class, () -> new TaskName(null),
                    "TaskName creation should throw NullPointerException when value is null");
        }

        @Test
        @DisplayName("should throw when name is empty")
        void shouldThrowWhenEmpty() {
            assertThrows(IllegalArgumentException.class, () -> new TaskName(""),
                    "TaskName creation should throw IllegalArgumentException when empty");
        }

        @Test
        @DisplayName("should throw when name exceeds 50 chars")
        void shouldThrowWhenTooLong() {
            String tooLong = "x".repeat(51);
            assertThrows(IllegalArgumentException.class, () -> new TaskName(tooLong),
                    "TaskName creation should throw IllegalArgumentException when longer than 50 chars");
        }
    }

    @Nested
    @DisplayName("Behavior")
    class BehaviorTests {

        @Test
        @DisplayName("should trim spaces")
        void shouldTrimSpaces() {
            TaskName name = new TaskName("   " + VALID_NAME + "   ");
            assertEquals(VALID_NAME, name.name(),
                    String.format("Expected trimmed name to be '%s', but was '%s'", VALID_NAME, name.name()));
        }

        @Test
        @DisplayName("should accept valid name within 50 chars")
        void shouldAcceptValidName() {
            TaskName name = TaskName.of(VALID_NAME);
            assertEquals(VALID_NAME, name.name(),
                    String.format("Expected valid name to be '%s', but was '%s'", VALID_NAME, name.name()));
        }
    }

    @Nested
    @DisplayName("Equality & HashCode")
    class EqualityTests {

        @Test
        @DisplayName("should be equal for same value")
        void shouldBeEqualForSameValue() {
            TaskName n1 = TaskName.of(VALID_NAME);
            TaskName n2 = TaskName.of(VALID_NAME);
            assertEquals(n1, n2, "TaskNames with same value should be equal");
            assertEquals(n1.hashCode(), n2.hashCode(), "Hash codes should match for equal TaskNames");
        }

        @Test
        @DisplayName("should not be equal for different value")
        void shouldNotBeEqualForDifferentValue() {
            TaskName n1 = TaskName.of(VALID_NAME);
            TaskName n2 = TaskName.of("Another task");
            assertNotEquals(n1, n2, "TaskNames with different values should not be equal");
            assertNotEquals(n1.hashCode(), n2.hashCode(), "Hash codes should differ for TaskNames with different values");
        }
    }
}
