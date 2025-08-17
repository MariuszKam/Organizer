package model.user;

import com.organizer.model.user.Username;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Username Value Object Tests")
class UsernameTest {

    private final String validUsername = "testuser"; // 8 chars, valid per domain

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should throw when username is null")
        void shouldThrowWhenNull() {
            assertThrows(NullPointerException.class, () -> new Username(null),
                    "Username creation should throw NullPointerException when value is null");
        }

        @Test
        @DisplayName("should throw when username is empty")
        void shouldThrowWhenEmpty() {
            assertThrows(IllegalArgumentException.class, () -> new Username(""),
                    "Username creation should throw IllegalArgumentException when value is empty");
        }

        @Test
        @DisplayName("should throw when username format is invalid (non-lowercase letters)")
        void shouldThrowWhenInvalidFormat() {
            assertThrows(IllegalArgumentException.class, () -> new Username("invalid-user"),
                    "Username creation should throw IllegalArgumentException for invalid format");
        }

        @Test
        @DisplayName("should throw when username is too short (< 4 chars)")
        void shouldThrowWhenTooShort() {
            assertThrows(IllegalArgumentException.class, () -> new Username("abc"),
                    "Username creation should throw IllegalArgumentException when too short");
        }

        @Test
        @DisplayName("should throw when username is too long (> 10 chars)")
        void shouldThrowWhenTooLong() {
            assertThrows(IllegalArgumentException.class, () -> new Username("abcdefghija"), // 11 chars
                    "Username creation should throw IllegalArgumentException when too long");
        }
    }

    @Nested
    @DisplayName("Normalization")
    class NormalizationTests {

        @Test
        @DisplayName("should lowercase input to domain format")
        void shouldLowercaseInput() {
            Username userUpper = Username.of("TESTUSER");
            assertEquals("testuser", userUpper.username(),
                    String.format("Expected username to be lowercased: '%s'", userUpper.username()));
        }

        @Test
        @DisplayName("should create valid username")
        void shouldCreateValidUsername() {
            Username validUser = Username.of(validUsername);
            assertEquals(validUsername, validUser.username(),
                    String.format("Expected username to be '%s'", validUsername));
        }
    }

    @Nested
    @DisplayName("Equality & HashCode")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("should be equal for same logical value")
        void shouldBeEqualForSameValue() {
            Username u1 = Username.of(validUsername);
            Username u2 = Username.of(validUsername);
            assertEquals(u1, u2, String.format(
                    "Expected usernames to be equal: '%s' and '%s'", u1.username(), u2.username()));
        }

        @Test
        @DisplayName("should not be equal for different values")
        void shouldNotBeEqualForDifferentValues() {
            Username u1 = Username.of(validUsername);
            Username u2 = Username.of("testuserb");
            assertNotEquals(u1, u2, String.format(
                    "Expected usernames to be different: '%s' and '%s'", u1.username(), u2.username()));
        }

        @Test
        @DisplayName("should have equal hashCodes for equal values")
        void shouldHaveEqualHashCodesForEqualValues() {
            Username u1 = Username.of(validUsername);
            Username u2 = Username.of(validUsername);
            assertEquals(u1.hashCode(), u2.hashCode(), String.format(
                    "Expected hash codes to be equal for usernames: '%s' and '%s'", u1.username(), u2.username()));
        }

        @Test
        @DisplayName("should have different hashCodes for different values")
        void shouldHaveDifferentHashCodesForDifferentValues() {
            Username u1 = Username.of(validUsername);
            Username u2 = Username.of("testuserb");
            assertNotEquals(u1.hashCode(), u2.hashCode(), String.format(
                    "Expected hash codes to be different for usernames: '%s' and '%s'", u1.username(), u2.username()));
        }
    }
}