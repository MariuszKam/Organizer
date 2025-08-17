package model.user;

import com.organizer.model.user.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserId Value Object Tests")
class UserIdTest {

    @Nested
    @DisplayName("Creation")
    class CreationTests {

        @Test
        @DisplayName("should create a new UserId with random UUID")
        void shouldCreateUserId() {
            UserId userId = UserId.newId();
            assertNotNull(userId, "UserId should not be null after creation");
        }

        @Test
        @DisplayName("should create UserId from valid string")
        void shouldCreateUserIdFromString() {
            String userIdString = "123e4567-e89b-12d3-a456-426614174000";
            UserId userId = UserId.of(userIdString);
            assertEquals(userIdString, userId.toString(), "UserId string representation should match input string");
        }

        @Test
        @DisplayName("should convert UserId with spaces in string")
        void shouldConvertWithSpaces() {
            String id = "123e4567-e89b-12d3-a456-426614174000";
            String userIdString = " " + id + " ";
            UserId userId = UserId.of(userIdString);
            assertEquals(id, userId.toString(), "UserId string representation should match trimmed input string");
        }

        @Test
        @DisplayName("should parse from string back to same UserId")
        void shouldParseFromStringBackToSameId() {
            UserId userId = UserId.newId();
            String userIdString = userId.toString();

            UserId convertedUserId = UserId.of(userIdString);
            assertEquals(userId, convertedUserId, "Converted UserId should equal original UserId");
        }
    }

    @Nested
    @DisplayName("Equality")
    class EqualityTests {

        @Test
        @DisplayName("should not be equal for different UUIDs")
        void shouldNotBeEqualForDifferentUserIds() {
            UserId userId1 = UserId.newId();
            UserId userId2 = UserId.newId();

            assertNotEquals(userId1, userId2, "UserIds should not be equal");
        }

    }

    @Nested
    @DisplayName("Exception")
    class ExceptionTests {

        @Test
        @DisplayName("should throw exception for null UUID")
        void shouldThrowExceptionForNullUuid() {
            assertThrows(NullPointerException.class,() -> new UserId(null),
                    "UserId creation should throw NullPointerException when value is null");
        }

        @Test
        @DisplayName("should throw exception for NIL UUID")
        void shouldThrowExceptionForNilUuid() {
            UUID uuid = new UUID(0L, 0L);
            assertThrows(IllegalArgumentException.class, () -> new UserId(uuid),
                    "UserId creation should throw IllegalArgumentException when value is NIL UUID");
        }

        @Test
        @DisplayName("should throw exception for null string")
        void shouldThrowExceptionForNullString() {
            assertThrows(NullPointerException.class, () -> UserId.of(null),
                    "UserId creation from string should throw NullPointerException when string is null");
        }

        @Test
        @DisplayName("should throw exception for invalid string")
        void shouldThrowExceptionForInvalidString() {
            String invalidUserIdString = "invalid-uuid-string";
            assertThrows(IllegalArgumentException.class, () -> UserId.of(invalidUserIdString),
                    "UserId creation from invalid string should throw IllegalArgumentException");
        }

    }

}
