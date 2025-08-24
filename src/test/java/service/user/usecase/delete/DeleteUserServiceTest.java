package service.user.usecase.delete;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.Username;
import com.organizer.service.user.adapter.InMemoryUserStore;
import com.organizer.service.user.port.UserStore;
import com.organizer.service.user.usecase.delete.DeleteUserCommand;
import com.organizer.service.user.usecase.delete.DeleteUserResult;
import com.organizer.service.user.usecase.delete.DeleteUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Use Case: Delete User")
class DeleteUserServiceTest {

    private static final String EXISTING_USERNAME = "existingUser";
    private static final String EXISTING_EMAIL = "exists@org.com";

    private UserStore userStore;
    private DeleteUserService service;
    private User existingUser;

    @BeforeEach
    void setUp() {
        userStore = new InMemoryUserStore();
        existingUser = new User(Username.of(EXISTING_USERNAME), Email.of(EXISTING_EMAIL));
        userStore.save(existingUser);

        service = new DeleteUserService(userStore);
    }

    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("should return MISSING_COMMAND when command is null")
        void shouldReturnMissingCommandWhenCommandIsNull() {
            DeleteUserResult result = service.handle(null);
            assertEquals(DeleteUserResult.Error.MISSING_COMMAND, result,
                    String.format("Expected MISSING_COMMAND but got: %s", result));
        }

        @Test
        @DisplayName("should return MISSING_USER_ID when id is null")
        void shouldReturnMissingUserIdWhenIdIsNull() {
            DeleteUserResult result = service.handle(new DeleteUserCommand(null));
            assertEquals(DeleteUserResult.Error.MISSING_USER_ID, result,
                    String.format("Expected MISSING_USER_ID but got: %s", result));
        }

        @Test
        @DisplayName("should return INVALID_USER_ID_FORMAT when id is malformed")
        void shouldReturnInvalidUserIdFormatWhenIdIsMalformed() {
            DeleteUserResult result = service.handle(new DeleteUserCommand("not-a-uuid"));
            assertEquals(DeleteUserResult.Error.INVALID_USER_ID_FORMAT, result,
                    String.format("Expected INVALID_USER_ID_FORMAT but got: %s", result));
        }
    }

    @Nested
    @DisplayName("Not found / Success / Idempotency")
    class DeleteFlow {

        @Test
        @DisplayName("should return USER_NOT_FOUND when user does not exist")
        void shouldReturnUserNotFoundWhenUserDoesNotExist() {
            String missingId = UUID.randomUUID().toString();
            DeleteUserResult result = service.handle(new DeleteUserCommand(missingId));
            assertEquals(DeleteUserResult.Error.USER_NOT_FOUND, result,
                    String.format("Expected USER_NOT_FOUND but got: %s", result));
        }

        @Test
        @DisplayName("should delete existing user and return Ok")
        void shouldDeleteUserWhenIdExistsAndReturnOk() {
            String id = existingUser.getId().toString();

            DeleteUserResult result = service.handle(new DeleteUserCommand(id));
            assertInstanceOf(DeleteUserResult.Ok.class, result,
                    String.format("Expected Ok but got: %s", result));
            DeleteUserResult.Ok ok = (DeleteUserResult.Ok) result;
            assertEquals(existingUser.getId(), ok.userId(), "Ok should carry deleted user's id");

            assertTrue(userStore.findById(existingUser.getId()).isEmpty(),
                    "User should be removed from byId index");
            assertFalse(userStore.existsByUsername(existingUser.getUsername()),
                    "Username index should not contain deleted user");
            assertFalse(userStore.existsByEmail(existingUser.getEmail()),
                    "Email index should not contain deleted user");
        }

        @Test
        @DisplayName("should return USER_NOT_FOUND when deleting the same user twice")
        void shouldReturnUserNotFoundWhenDeletingSameUserTwice() {
            String id = existingUser.getId().toString();

            DeleteUserResult first = service.handle(new DeleteUserCommand(id));
            assertInstanceOf(DeleteUserResult.Ok.class, first,
                    String.format("First delete should be Ok but got: %s", first));

            DeleteUserResult second = service.handle(new DeleteUserCommand(id));
            assertEquals(DeleteUserResult.Error.USER_NOT_FOUND, second,
                    String.format("Second delete should be USER_NOT_FOUND but got: %s", second));
        }

        @Test
        @DisplayName("should allow reusing username/email after delete")
        void shouldAllowReusingUsernameAndEmailAfterDelete() {
            String id = existingUser.getId().toString();

            DeleteUserResult del = service.handle(new DeleteUserCommand(id));
            assertInstanceOf(DeleteUserResult.Ok.class, del,
                    String.format("Expected Ok on delete but got: %s", del));

            User recreated = new User(Username.of(EXISTING_USERNAME), Email.of(EXISTING_EMAIL));
            assertDoesNotThrow(() -> userStore.save(recreated),
                    "Saving user with previously used username/email should be allowed after delete");

            assertTrue(userStore.existsByUsername(recreated.getUsername()),
                    "Username index should contain recreated user");
            assertTrue(userStore.existsByEmail(recreated.getEmail()),
                    "Email index should contain recreated user");
            assertTrue(userStore.findById(recreated.getId()).isPresent(),
                    "byId index should contain recreated user");
        }
    }
}
