package model.user;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.UserId;
import com.organizer.model.user.Username;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Aggregate Tests")
class UserTest {

    private static final Username TEST_USER = new Username("testUser");
    private static final Email TEST_EMAIL = new Email("example@org.com");

    @Nested
    @DisplayName("Creation")
    class CreationTests {

        @Test
        @DisplayName("should create user with generated UUID (non-null id)")
        void shouldCreateUserWithGeneratedId() {
            User user = new User(TEST_USER, TEST_EMAIL);
            assertNotNull(user.getId(), "User ID should be generated (non-null)");
            assertEquals(TEST_USER, user.getUsername(), "Username should match");
            assertEquals(TEST_EMAIL, user.getEmail(), "Email should match");
        }

        @Test
        @DisplayName("should create user with explicit UserId")
        void shouldCreateUserWithExplicitId() {
            UserId id = UserId.of("550e8400-e29b-41d4-a716-446655440000");
            User user = new User(id, TEST_USER, TEST_EMAIL);
            assertEquals(id, user.getId(), "User ID should match the explicit value");
        }

        @Test
        @DisplayName("should throw when id is null")
        void shouldThrowWhenIdIsNull() {
            assertThrows(NullPointerException.class, () -> new User(null, TEST_USER, TEST_EMAIL),
                    "User creation should throw NullPointerException when ID is null");
        }

        @Test
        @DisplayName("should throw when username is null")
        void shouldThrowWhenUsernameIsNull() {
            UserId id = UserId.newId();
            assertThrows(NullPointerException.class, () -> new User(id, null, TEST_EMAIL),
                    "User creation should throw NullPointerException when username is null");
        }

        @Test
        @DisplayName("should throw when email is null")
        void shouldThrowWhenEmailIsNull() {
            UserId id = UserId.newId();
            assertThrows(NullPointerException.class, () -> new User(id, TEST_USER, null),
                    "User creation should throw NullPointerException when email is null");
        }
    }

    @Nested
    @DisplayName("Equality & HashCode")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("should be equal for users with the same UserId")
        void shouldBeEqualForSameId() {
            UserId sameId = UserId.of("11111111-1111-1111-1111-111111111111");
            User user1 = new User(sameId, TEST_USER, TEST_EMAIL);
            User user2 = new User(sameId, TEST_USER, TEST_EMAIL);
            assertEquals(user1, user2, "Users with the same ID should be equal");
            assertEquals(user1.hashCode(), user2.hashCode(), "Hash codes should be equal for the same ID");
        }

        @Test
        @DisplayName("should not be equal for users with different UserIds")
        void shouldNotBeEqualForDifferentIds() {
            User user1 = new User(UserId.of("22222222-2222-2222-2222-222222222222"), TEST_USER, TEST_EMAIL);
            User user2 = new User(UserId.of("33333333-3333-3333-3333-333333333333"),
                    Username.of("testUserb"), Email.of("example2@org.com"));
            assertNotEquals(user1, user2, "Users with different IDs should not be equal");
            assertNotEquals(user1.hashCode(), user2.hashCode(), "Hash codes should differ for different IDs");
        }
    }

    @Nested
    @DisplayName("Change Username")
    class ChangeUsernameTests {

        @Test
        @DisplayName("should change username to a new value")
        void shouldChangeUsername() {
            User user = new User(TEST_USER, TEST_EMAIL);
            Username newUsername = new Username("newUser"); // znormalizuje do "newuser"
            user.changeUsername(newUsername);
            assertEquals(newUsername, user.getUsername(), "Username should be updated");
        }

        @Test
        @DisplayName("should throw when changing username to the same value")
        void shouldThrowWhenChangingToSameUsername() {
            User user = new User(TEST_USER, TEST_EMAIL);
            assertThrows(IllegalArgumentException.class, () -> user.changeUsername(TEST_USER),
                    "Changing username to the same value should throw IllegalArgumentException");
        }

        @Test
        @DisplayName("should throw when changing username to null")
        void shouldThrowWhenChangingUsernameToNull() {
            User user = new User(TEST_USER, TEST_EMAIL);
            assertThrows(NullPointerException.class, () -> user.changeUsername(null),
                    "Changing username to null should throw NullPointerException");
        }
    }

    @Nested
    @DisplayName("Change Email")
    class ChangeEmailTests {

        @Test
        @DisplayName("should change email to a new value")
        void shouldChangeEmail() {
            User user = new User(TEST_USER, TEST_EMAIL);
            Email newEmail = Email.of("example2@org.com");
            user.changeEmail(newEmail);
            assertEquals(newEmail, user.getEmail(), "Email should be updated");
        }

        @Test
        @DisplayName("should throw when changing email to the same value")
        void shouldThrowWhenChangingToSameEmail() {
            User user = new User(TEST_USER, TEST_EMAIL);
            assertThrows(IllegalArgumentException.class, () -> user.changeEmail(TEST_EMAIL),
                    "Changing email to the same value should throw IllegalArgumentException");
        }

        @Test
        @DisplayName("should throw when changing email to null")
        void shouldThrowWhenChangingEmailToNull() {
            User user = new User(TEST_USER, TEST_EMAIL);
            assertThrows(NullPointerException.class, () -> user.changeEmail(null),
                    "Changing email to null should throw NullPointerException");
        }
    }
}
