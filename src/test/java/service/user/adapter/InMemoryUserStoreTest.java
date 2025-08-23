package service.user.adapter;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.Username;
import com.organizer.service.user.adapter.InMemoryUserStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemoryUserStore Tests")
class InMemoryUserStoreTest {

    private static final User TEST_USER_EXISTED = new User(new Username("testuser"), new Email("examaple@org.com"));
    private static final User TEST_USER_NOT_EXISTED = new User(new Username("notexist"), new Email("notsave@org.com"));

    @Nested
    @DisplayName("Save User Tests")
    class SaveUserTests {

        @Test
        @DisplayName("Should save user successfully")
        void shouldSaveUserSuccessfully() {
            InMemoryUserStore userStore = new InMemoryUserStore();
            userStore.save(TEST_USER_EXISTED);
            assertTrue(userStore.existsByUsername(TEST_USER_EXISTED.getUsername()));
            assertTrue(userStore.existsByEmail(TEST_USER_EXISTED.getEmail()));
        }

    }

    @Nested
    @DisplayName("Existence Check Tests")
    class ExistenceCheckTests {

        private InMemoryUserStore userStore;

        @BeforeEach
        void setUp() {
            userStore = new InMemoryUserStore();
            userStore.save(TEST_USER_EXISTED);
        }

        @Nested
        @DisplayName("Exists By Username Tests")
        class ExistsByUsernameTests {

            @Test
            @DisplayName("Should confirm existence by username")
            void shouldConfirmExistenceByUsername() {
                assertTrue(userStore.existsByUsername(TEST_USER_EXISTED.getUsername()));
            }

            @Test
            @DisplayName("Should return false for non-existing username")
            void shouldReturnFalseForNonExistingUsername() {
                assertFalse(userStore.existsByUsername(TEST_USER_NOT_EXISTED.getUsername()));
            }

        }

        @Nested
        @DisplayName("Exists By Email Tests")
        class ExistsByEmailTests {

            @Test
            @DisplayName("Should confirm existence by email")
            void shouldConfirmExistenceByEmail() {
                assertTrue(userStore.existsByEmail(TEST_USER_EXISTED.getEmail()));
            }

            @Test
            @DisplayName("Should return false for non-existing email")
            void shouldReturnFalseForNonExistingEmail() {
                assertFalse(userStore.existsByEmail(TEST_USER_NOT_EXISTED.getEmail()));
            }

        }

        @Nested
        @DisplayName("Exists By Id Tests")
        class ExistsByIdTests {

            @Test
            @DisplayName("Should confirm existence by user ID")
            void shouldConfirmExistenceById() {
                assertTrue(userStore.findById(TEST_USER_EXISTED.getId()).isPresent());
            }

            @Test
            @DisplayName("Should return false for non-existing user ID")
            void shouldReturnFalseForNonExistingId() {
                assertFalse(userStore.findById(TEST_USER_NOT_EXISTED.getId()).isPresent());
            }
        }

        @Nested
        @DisplayName("Find All Users Tests")
        class FindAllUsersTests {

            @Test
            @DisplayName("Should return all saved users")
            void shouldReturnAllSavedUsers() {
                List<User> users = userStore.findAll();
                assertTrue(users.contains(TEST_USER_EXISTED));
                assertFalse(users.contains(TEST_USER_NOT_EXISTED));
            }

            @Test
            @DisplayName("should return immutable list of users")
            void shouldReturnImmutableListOfUsers() {
                List<User> users = userStore.findAll();
                assertThrows(UnsupportedOperationException.class, () -> users.add(TEST_USER_NOT_EXISTED),
                        "The returned list should be immutable");
            }
        }

    }
}
