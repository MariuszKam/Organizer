package model.user;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private final Email testEmail = new Email("example@org.com");

    @Test
    public void testUserCreation() {
        User user = new User(1L, "testUser", testEmail);
        assertEquals(1L, user.getId(), String.format("User ID should be 1, but was %d", user.getId()));
        assertEquals("testUser", user.getUsername(), String.format("Username should be 'testUser', but was '%s'", user.getUsername()));
        assertEquals(user.getEmail(), testEmail, String.format("Email should be '%s', but was '%s'", testEmail, user.getEmail()));
    }

    @Test
    public void testUserWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> new User(null, "testUser", testEmail),
                "User creation should throw IllegalArgumentException when ID is null");
    }

    @Test
    public void testUserWithEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> new User(1L, "", testEmail),
                     "User creation should throw IllegalArgumentException when username is empty");
    }

    @Test
    public void testUserWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> new User(1L, null, testEmail),
                     "User creation should throw IllegalArgumentException when username is null");
    }

    @Test
    public void testUserWithNullEmail() {
        assertThrows(IllegalArgumentException.class, () -> new User(1L, "testUser", null),
                     "User creation should throw IllegalArgumentException when email is null");
    }

    @Test
    public void testUserEquality() {
        User user1 = new User(1L, "testUser", testEmail);
        User user2 = new User(1L, "testUser", testEmail);
        assertEquals(user1, user2, String.format("Users with the same ID should be equal, but they are not: %s != %s", user1, user2));
    }

    @Test
    public void testUserInequality() {
        User user1 = new User(1L, "testUser", testEmail);
        User user2 = new User(2L, "testUser2", new Email("example2@org.com"));
        assertNotEquals(user1, user2, String.format("Users with different IDs should not be equal: %s == %s", user1, user2));
    }

    @Test
    public void testUserHashCode() {
        User user1 = new User(1L, "testUser", testEmail);
        User user2 = new User(1L, "testUser", testEmail);
        assertEquals(user1.hashCode(), user2.hashCode(), String.format("Hash codes should be equal for users with the same ID: %d != %d", user1.hashCode(), user2.hashCode()));
    }

    @Test
    public void testUserHashCodeInequality() {
        User user1 = new User(1L, "testUser", testEmail);
        User user2 = new User(2L, "testUser2", new Email("example2@org.com"));
        assertNotEquals(user1.hashCode(), user2.hashCode(), String.format("Hash codes should not be equal for users with different IDs: %d == %d", user1.hashCode(), user2.hashCode()));
    }
}
