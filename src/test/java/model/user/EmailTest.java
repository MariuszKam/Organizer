package model.user;

import com.organizer.model.user.Email;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    private final String emailAddress = "example@org.com";

    @Test
    public void testEmailWhenNull() {
        assertThrows(NullPointerException.class, () ->
                new Email(null));
    }

    @Test
    public void testEmailWhenEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
                new Email(""));
    }

    @Test
    public void testEmailWhenInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () ->
                new Email("invalid-email"));
    }

    @Test
    public void testEmailWhenValid() {
        Email validEmail = new Email(emailAddress);
        assertEquals(emailAddress, validEmail.emailAddress(), String.format("Expected email address to be '%s'", emailAddress));
    }

    @Test
    public void testEmailEquality() {
        Email email1 = new Email(emailAddress);
        Email email2 = new Email(emailAddress);
        assertEquals(email1, email2,
                     String.format("Expected emails to be equal: '%s' and '%s'", email1.emailAddress(),
                                   email2.emailAddress()));
    }

    @Test
    public void testEmailInequality() {
        Email email1 = new Email(emailAddress);
        Email email2 = new Email("example2@org.com");
        assertNotEquals(email1, email2,
                        String.format("Expected emails to be different: '%s' and '%s'", email1.emailAddress(),
                                      email2.emailAddress()));
    }

    @Test
    public void testEmailHashCode() {
        Email email1 = new Email(emailAddress);
        Email email2 = new Email(emailAddress);
        assertEquals(email1.hashCode(), email2.hashCode(),
                     String.format("Expected hash codes to be equal for emails: '%s' and '%s", email1.emailAddress(),
                                   email2.emailAddress()));
    }

    @Test
    public void testEmailHashCodeInequality() {
        Email email1 = new Email(emailAddress);
        Email email2 = new Email("example2@org.com");
        assertNotEquals(email1.hashCode(), email2.hashCode(),
                        String.format("Expected hash codes to be different for emails: '%s' and '%s'",
                                      email1.emailAddress(),
                                      email2.emailAddress()));
    }

    @Test
    public void testUpperLowerEquality() {
        Email email = Email.of("ABC@ORG.COM");
        assertEquals("abc@org.com", email.emailAddress(),
                String.format("Expected email address to be lowercased: '%s'", email.emailAddress()));
    }

    @Test
    public void testInCorrectFormat() {
        assertThrows(IllegalArgumentException.class, () ->
                new Email("invalid-email@.com"));
    }

    @Test
    public void testMaskingEmail() {
        Email email = new Email(emailAddress);
        String maskedEmail = email.toString();
        assertTrue(maskedEmail.startsWith("e***@"),
                String.format("Expected masked email to start with 'e***@', but was '%s'", maskedEmail));
        assertTrue(maskedEmail.endsWith("@org.com"),
                String.format("Expected masked email to end with '@org.com', but was '%s'", maskedEmail));
    }
}
