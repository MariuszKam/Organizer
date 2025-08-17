package model.task;

import com.organizer.model.task.TaskDescription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskDescription Value Object Tests")
class TaskDescriptionTest {

    private static final String VALID_DESCRIPTION = "This task involves implementing authentication with JWT.";

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should throw when description is null")
        void shouldThrowWhenNull() {
            assertThrows(NullPointerException.class, () -> new TaskDescription(null),
                    "TaskDescription creation should throw NullPointerException when value is null");
        }

        @Test
        @DisplayName("should throw when description is empty")
        void shouldThrowWhenEmpty() {
            assertThrows(IllegalArgumentException.class, () -> new TaskDescription(""),
                    "TaskDescription creation should throw IllegalArgumentException when empty");
        }

        @Test
        @DisplayName("should throw when description exceeds 500 chars")
        void shouldThrowWhenTooLong() {
            String tooLong = "x".repeat(501);
            assertThrows(IllegalArgumentException.class, () -> new TaskDescription(tooLong),
                    "TaskDescription creation should throw IllegalArgumentException when longer than 500 chars");
        }
    }

    @Nested
    @DisplayName("Behavior")
    class BehaviorTests {

        @Test
        @DisplayName("should accept valid description")
        void shouldAcceptValidDescription() {
            TaskDescription desc = TaskDescription.of(VALID_DESCRIPTION);
            assertEquals(VALID_DESCRIPTION, desc.description(),
                    String.format("Expected valid description to be '%s', but was '%s'", VALID_DESCRIPTION, desc.description()));
        }
    }

    @Nested
    @DisplayName("Equality & HashCode")
    class EqualityTests {

        @Test
        @DisplayName("should be equal for same value")
        void shouldBeEqualForSameValue() {
            TaskDescription d1 = TaskDescription.of(VALID_DESCRIPTION);
            TaskDescription d2 = TaskDescription.of(VALID_DESCRIPTION);
            assertEquals(d1, d2, "TaskDescription instances with same value should be equal");
            assertEquals(d1.hashCode(), d2.hashCode(), "TaskDescription instances with same value should have same hashCode");
        }

        @Test
        @DisplayName("should not be equal for different values")
        void shouldNotBeEqualForDifferentValues() {
            TaskDescription d1 = TaskDescription.of(VALID_DESCRIPTION);
            TaskDescription d2 = TaskDescription.of("Different description");
            assertNotEquals(d1, d2, "TaskDescription instances with different values should not be equal");
            assertNotEquals(d1.hashCode(), d2.hashCode(), "TaskDescription instances with different values should have different hashCodes");
        }
    }
}

