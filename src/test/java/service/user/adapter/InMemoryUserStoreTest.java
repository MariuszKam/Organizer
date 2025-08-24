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

        @Test
        @DisplayName("Should throw exception when saving null user")
        void shouldThrowExceptionWhenSavingNullUser() {
            InMemoryUserStore userStore = new InMemoryUserStore();
            assertThrows(NullPointerException.class, () -> userStore.save(null));
        }

        @Test
        @DisplayName("Should throw exception when saving user with existing username")
        void shouldThrowExceptionWhenSavingUserWithExistingUsername() {
            InMemoryUserStore userStore = new InMemoryUserStore();
            userStore.save(TEST_USER_EXISTED);
            User duplicateUsernameUser = new User(TEST_USER_EXISTED.getUsername(), new Email("newemail@org.com"));
            assertThrows(IllegalArgumentException.class, () -> userStore.save(duplicateUsernameUser));
        }

        @Test
        @DisplayName("Should throw exception when saving user with existing email")
        void shouldThrowExceptionWhenSavingUserWithExistingEmail() {
            InMemoryUserStore userStore = new InMemoryUserStore();
            userStore.save(TEST_USER_EXISTED);
            User duplicateEmailUser = new User(new Username("newuser"), TEST_USER_EXISTED.getEmail());
            assertThrows(IllegalArgumentException.class, () -> userStore.save(duplicateEmailUser));
        }

        @Test
        @DisplayName("Should update user username successfully")
        void shouldUpdateUsernameSuccessfully() {
            InMemoryUserStore userStore = new InMemoryUserStore();
            userStore.save(TEST_USER_EXISTED);
            User updatedUser = new User(TEST_USER_EXISTED.getId(), new Username("updateduser"), TEST_USER_EXISTED.getEmail());
            userStore.save(updatedUser);
            User newUser = userStore.findById(TEST_USER_EXISTED.getId()).orElse(null);
            assertNotNull(newUser, "Updated user should be found");
            assertEquals(updatedUser, newUser, "Updated user should match the saved user");
            assertEquals(updatedUser.getUsername(), newUser.getUsername(), "Username should be updated");
            assertEquals(updatedUser.getEmail(), newUser.getEmail(), "Email should remain unchanged");
        }

        @Test
        @DisplayName("Should update user email successfully")
        void shouldUpdateEmailSuccessfully() {
            InMemoryUserStore userStore = new InMemoryUserStore();
            userStore.save(TEST_USER_EXISTED);
            User updatedUser = new User(TEST_USER_EXISTED.getId(), TEST_USER_EXISTED.getUsername(), new Email("newmail@org.com"));
            userStore.save(updatedUser);
            User newUser = userStore.findById(TEST_USER_EXISTED.getId()).orElse(null);
            assertNotNull(newUser, "Updated user should be found");
            assertEquals(updatedUser, newUser, "Updated user should match the saved user");
            assertEquals(updatedUser.getUsername(), newUser.getUsername(), "Username should remain unchanged");
            assertEquals(updatedUser.getEmail(), newUser.getEmail(), "Email should be updated");
        }

        @Test
        @DisplayName("Should update user email, and username successfully")
        void shouldUpdateUserSuccessfully() {
            InMemoryUserStore userStore = new InMemoryUserStore();
            userStore.save(TEST_USER_EXISTED);
            User updatedUser = new User(TEST_USER_EXISTED.getId(), new Username("updateduser"), new Email("newone@org.com"));
            userStore.save(updatedUser);
            User newUser = userStore.findById(TEST_USER_EXISTED.getId()).orElse(null);
            assertNotNull(newUser, "Updated user should be found");
            assertEquals(updatedUser, newUser, "Updated user should match the saved user");
            assertEquals(updatedUser.getUsername(), newUser.getUsername(), "Username should be updated");
            assertEquals(updatedUser.getEmail(), newUser.getEmail(), "Email should be updated");
        }

        @Test
        @DisplayName("Should not change store when updating user with same data")
        void shouldNotChangeStoreWhenUpdatingUserWithSameData() {
            InMemoryUserStore userStore = new InMemoryUserStore();
            userStore.save(TEST_USER_EXISTED);
            userStore.save(TEST_USER_EXISTED); // Save the same user again
            List<User> users = userStore.findAll();
            assertEquals(1, users.size(), "Store should still contain only one user");
            assertEquals(TEST_USER_EXISTED, users.getFirst(), "The user in the store should match the original user");
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
            @DisplayName("Should find User by Username")
            void shouldReturnUserByUsername() {
                assertTrue(userStore.findByUsername(TEST_USER_EXISTED.getUsername()).isPresent(),
                        "User should be found by username");
            }

            @Test
            @DisplayName("Should not find User by non-existing username")
            void shouldReturnEmptyOptionalForNonExistingUsername() {
                assertTrue(userStore.findByUsername(TEST_USER_NOT_EXISTED.getUsername()).isEmpty(),
                        "User should be not found by username");
            }

            @Test
            @DisplayName("Should confirm existence by username")
            void shouldConfirmExistenceByUsername() {
                assertTrue(userStore.existsByUsername(TEST_USER_EXISTED.getUsername()),
                        "User should be found by username");
            }

            @Test
            @DisplayName("Should return false for non-existing username")
            void shouldReturnFalseForNonExistingUsername() {
                assertFalse(userStore.existsByUsername(TEST_USER_NOT_EXISTED.getUsername()),
                        "User should not be found by username");
            }

        }

        @Nested
        @DisplayName("Exists By Email Tests")
        class ExistsByEmailTests {

            @Test
            @DisplayName("Should find User by Email")
            void shouldFindUserByEmail() {
                assertTrue(userStore.findByEmail(TEST_USER_EXISTED.getEmail()).isPresent(),
                        "User should be found by email");
            }

            @Test
            @DisplayName("Should not find User by non-existing Email")
            void shouldReturnEmptyOptionalForNonExistingEmail() {
                assertTrue(userStore.findByEmail(TEST_USER_NOT_EXISTED.getEmail()).isEmpty(),
                        "Should return empty optional for non-existing email");
            }

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

            @Test
            @DisplayName("Should return the correct user by ID")
            void shouldReturnTheCorrectUserById() {
                assertEquals(TEST_USER_EXISTED, userStore.findById(TEST_USER_EXISTED.getId()).orElse(null));
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

        @Nested
        @DisplayName("Remove User Tests")
        class RemoveUserTests {

            private InMemoryUserStore userStore;

            @BeforeEach
            void setUp() {
                userStore = new InMemoryUserStore();
                userStore.save(TEST_USER_EXISTED);
            }

            @Test
            @DisplayName("Should throw exception when removing null user")
            void shouldThrowExceptionWhenRemovingNullUser() {
                assertThrows(NullPointerException.class, () -> userStore.remove(null),
                        "Expected NullPointerException when removing null user");
            }

            @Test
            @DisplayName("Should remove existing user from all indexes")
            void shouldRemoveExistingUserFromAllIndexes() {
                assertDoesNotThrow(() -> userStore.remove(TEST_USER_EXISTED),
                        "Removing existing user should not throw");

                assertTrue(userStore.findById(TEST_USER_EXISTED.getId()).isEmpty(),
                        String.format("User with id %s should be removed from byId", TEST_USER_EXISTED.getId()));
                assertFalse(userStore.existsByUsername(TEST_USER_EXISTED.getUsername()),
                        String.format("Username %s should be free after removal", TEST_USER_EXISTED.getUsername()));
                assertFalse(userStore.existsByEmail(TEST_USER_EXISTED.getEmail()),
                        String.format("Email %s should be free after removal", TEST_USER_EXISTED.getEmail()));

                assertFalse(userStore.findAll().contains(TEST_USER_EXISTED),
                        "findAll should not contain removed user");
            }

            @Test
            @DisplayName("Should be idempotent when removing the same user twice")
            void shouldBeIdempotentWhenRemovingSameUserTwice() {
                assertDoesNotThrow(() -> userStore.remove(TEST_USER_EXISTED),
                        "First removal should not throw");

                assertDoesNotThrow(() -> userStore.remove(TEST_USER_EXISTED),
                        "Second removal should be a no-op (idempotent)");

                assertTrue(userStore.findById(TEST_USER_EXISTED.getId()).isEmpty(),
                        "User should remain absent from byId after second removal");
                assertFalse(userStore.existsByUsername(TEST_USER_EXISTED.getUsername()),
                        "Username should remain free after second removal");
                assertFalse(userStore.existsByEmail(TEST_USER_EXISTED.getEmail()),
                        "Email should remain free after second removal");
            }

            @Test
            @DisplayName("Should not modify store when removing non-existing user")
            void shouldNotModifyStoreWhenRemovingNonExistingUser() {
                int sizeBefore = userStore.findAll().size();

                assertDoesNotThrow(() -> userStore.remove(TEST_USER_NOT_EXISTED),
                        "Removing non-existing user should not throw");

                int sizeAfter = userStore.findAll().size();
                assertEquals(sizeBefore, sizeAfter,
                        "Removing non-existing user should not change store size");
                assertTrue(userStore.findById(TEST_USER_EXISTED.getId()).isPresent(),
                        "Existing user should remain in store");
            }

            @Test
            @DisplayName("Should allow reusing username/email after remove")
            void shouldAllowReusingUsernameAndEmailAfterRemove() {
                userStore.remove(TEST_USER_EXISTED);

                User recreated = new User(TEST_USER_EXISTED.getUsername(), TEST_USER_EXISTED.getEmail());
                assertDoesNotThrow(() -> userStore.save(recreated),
                        "Saving a user with previously used username/email should be allowed after removal");

                assertTrue(userStore.existsByUsername(recreated.getUsername()),
                        "Username index should contain recreated user");
                assertTrue(userStore.existsByEmail(recreated.getEmail()),
                        "Email index should contain recreated user");
                assertTrue(userStore.findById(recreated.getId()).isPresent(),
                        "byId index should contain recreated user");
            }
        }

    }
}
