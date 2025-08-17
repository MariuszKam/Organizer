package model.user;

import com.organizer.model.user.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Email Value Object Tests")
class EmailTest {

    private final String emailAddress = "example@org.com";

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should throw when email is null")
        void shouldThrowWhenNull() {
            assertThrows(NullPointerException.class, () -> new Email(null),
                    "Email creation should throw NullPointerException when value is null");
        }

        @Test
        @DisplayName("should throw when email is empty")
        void shouldThrowWhenEmpty() {
            assertThrows(IllegalArgumentException.class, () -> new Email(""),
                    "Email creation should throw IllegalArgumentException when empty");
        }

        @Test
        @DisplayName("should throw when email format is invalid (missing @)")
        void shouldThrowWhenInvalidFormat() {
            assertThrows(IllegalArgumentException.class, () -> new Email("invalid-email"),
                    "Email creation should throw IllegalArgumentException for invalid format");
        }

        @Test
        @DisplayName("should throw when email format is incorrect (bad domain)")
        void shouldThrowWhenIncorrectFormat() {
            assertThrows(IllegalArgumentException.class, () -> new Email("invalid-email@.com"),
                    "Email creation should throw IllegalArgumentException for incorrect format");
        }
    }

    @Nested
    @DisplayName("Normalization")
    class NormalizationTests {

        @Test
        @DisplayName("should lowercase email address")
        void shouldLowercaseEmail() {
            Email email = new Email("ABC@ORG.COM");
            assertEquals("abc@org.com", email.emailAddress(),
                    String.format("Expected email address to be lowercased: '%s'", email.emailAddress()));
        }

        @Test
        @DisplayName("should create valid email")
        void shouldCreateValidEmail() {
            Email validEmail = new Email(emailAddress);
            assertEquals(emailAddress, validEmail.emailAddress(),
                    String.format("Expected email address to be '%s'", emailAddress));
        }
    }

    @Nested
    @DisplayName("Equality & HashCode")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("should be equal for same email values")
        void shouldBeEqualForSameEmail() {
            Email email1 = new Email(emailAddress);
            Email email2 = new Email(emailAddress);
            assertEquals(email1, email2,
                    String.format("Expected emails to be equal: '%s' and '%s'",
                            email1.emailAddress(), email2.emailAddress()));
        }

        @Test
        @DisplayName("should not be equal for different email values")
        void shouldNotBeEqualForDifferentEmails() {
            Email email1 = new Email(emailAddress);
            Email email2 = new Email("example2@org.com");
            assertNotEquals(email1, email2,
                    String.format("Expected emails to be different: '%s' and '%s'",
                            email1.emailAddress(), email2.emailAddress()));
        }

        @Test
        @DisplayName("should have equal hashCodes for equal emails")
        void shouldHaveEqualHashCodes() {
            Email email1 = new Email(emailAddress);
            Email email2 = new Email(emailAddress);
            assertEquals(email1.hashCode(), email2.hashCode(),
                    String.format("Expected hash codes to be equal for emails: '%s' and '%s'",
                            email1.emailAddress(), email2.emailAddress()));
        }

        @Test
        @DisplayName("should have different hashCodes for different emails")
        void shouldHaveDifferentHashCodes() {
            Email email1 = new Email(emailAddress);
            Email email2 = new Email("example2@org.com");
            assertNotEquals(email1.hashCode(), email2.hashCode(),
                    String.format("Expected hash codes to be different for emails: '%s' and '%s'",
                            email1.emailAddress(), email2.emailAddress()));
        }
    }

    @Nested
    @DisplayName("Other Behavior")
    class OtherBehaviorTests {

        @Test
        @DisplayName("should mask email address in toString()")
        void shouldMaskEmail() {
            Email email = new Email(emailAddress);
            String maskedEmail = email.toString();
            assertTrue(maskedEmail.startsWith("e***@"),
                    String.format("Expected masked email to start with 'e***@', but was '%s'", maskedEmail));
            assertTrue(maskedEmail.endsWith("@org.com"),
                    String.format("Expected masked email to end with '@org.com', but was '%s'", maskedEmail));
        }
    }
}