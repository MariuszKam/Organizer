package model.user;

import com.organizer.model.user.Username;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UsernameTest {

    private final String validUsername = "testuser";

    @Test
    public void testUsernameWhenNull() {
        assertThrows(NullPointerException.class, () ->
                new Username(null));
    }

    @Test
    public void testUsernameWhenEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
                new Username(""));
    }

    @Test
    public void testUsernameWhenInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () ->
                new Username("invalid-user"));
    }

    @Test
    public void testUsernameWhenValid() {
        Username validUser = Username.of(validUsername);
        assertEquals(validUsername, validUser.username(), String.format("Expected username address to be '%s'", validUsername));
    }

    @Test
    public void testUsernameEquality() {
        Username validUser1 = Username.of(validUsername);
        Username validUser2 = Username.of(validUsername);
        assertEquals(validUser1, validUser2,
                String.format("Expected usernames to be equal: '%s' and '%s'", validUser1.username(),
                        validUser2.username()));
    }

    @Test
    public void testUsernameInequality() {
        Username validUser1 = Username.of(validUsername);
        Username validUser2 = Username.of("testuserb");
        assertNotEquals(validUser1, validUser2,
                String.format("Expected usernames to be different: '%s' and '%s'", validUser1.username(),
                        validUser2.username()));
    }

    @Test
    public void testUsernameHashCode() {
        Username validUser1 = Username.of(validUsername);
        Username validUser2 = Username.of(validUsername);
        assertEquals(validUser1.hashCode(), validUser2.hashCode(),
                String.format("Expected hash codes to be equal for usernames: '%s' and '%s", validUser1.username(),
                        validUser2.username()));
    }

    @Test
    public void testUsernameHashCodeInequality() {
        Username validUser1 = Username.of(validUsername);
        Username validUser2 = Username.of("testuserb");
        assertNotEquals(validUser1.hashCode(), validUser2.hashCode(),
                String.format("Expected hash codes to be different for usernames: '%s' and '%s'",
                        validUser1.username(),
                        validUser2.username()));
    }

    @Test
    public void testUpperLowerEquality() {
        Username userUpper = Username.of("TESTUSER");
        assertEquals("testuser", userUpper.username(),
                String.format("Expected username to be lowercased: '%s'", userUpper.username()));
    }

    @Test
    public void testTooShortUsername() {
        assertThrows(IllegalArgumentException.class, () ->
                new Username("abc"));
    }

    @Test
    public void testTooLongUsername() {
        assertThrows(IllegalArgumentException.class, () ->
                new Username("abcdefghija")); // 10 characters is the maximum
    }
}
