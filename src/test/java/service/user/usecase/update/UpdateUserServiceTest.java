package service.user.usecase.update;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.Username;
import com.organizer.service.user.adapter.InMemoryUserStore;
import com.organizer.service.user.port.UserStore;
import com.organizer.service.user.usecase.update.UpdateUserCommand;
import com.organizer.service.user.usecase.update.UpdateUserResult;
import com.organizer.service.user.usecase.update.UpdateUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DisplayName("Use Case: Update User")
public class UpdateUserServiceTest {

    private static final String NEW_USERNAME = "newUsername";
    private static final String NEW_EMAIL = "newone@org.com";

    private UpdateUserService service;
    private UserStore userStore;
    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User(Username.of("existingUser"), Email.of("exists@org.com"));
        userStore = new InMemoryUserStore();
        userStore.save(existingUser);
        service = new UpdateUserService(userStore);
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should return MISSING_COMMAND when command is null")
        void shouldReturnMissingCommandWhenCommandIsNull() {
            UpdateUserResult result = service.handle(null);
            assertEquals(UpdateUserResult.Error.MISSING_COMMAND, result, "Expected MISSING_COMMAND error");
        }

        @Test
        @DisplayName("should return NO_FIELDS_PROVIDED when no fields to update are provided")
        void shouldReturnNoFieldsProvidedWhenNoFieldsToUpdateAreProvided() {
            UpdateUserCommand command = new UpdateUserCommand(existingUser.getId().toString(), Optional.empty(), Optional.empty());
            UpdateUserResult result = service.handle(command);
            assertEquals(UpdateUserResult.Error.NO_FIELDS_PROVIDED, result, "Expected NO_FIELDS_PROVIDED error");
        }

        @Test
        @DisplayName("should return MISSING_USER_ID when user ID is missing")
        void shouldReturnMissingUserIdWhenUserIdIsMissing() {
            UpdateUserCommand command = new UpdateUserCommand(null, Optional.of(NEW_USERNAME), Optional.of(NEW_EMAIL));
            UpdateUserResult result = service.handle(command);
            assertEquals(UpdateUserResult.Error.MISSING_USER_ID, result, "Expected MISSING_USER_ID error");
        }

        @Test
        @DisplayName("should return INVALID_USER_ID_FORMAT when user ID format is invalid")
        void shouldReturnInvalidUserIdFormatWhenUserIdFormatIsInvalid() {
            UpdateUserCommand command = new UpdateUserCommand("invalid-uuid", Optional.of(NEW_USERNAME), Optional.of(NEW_EMAIL));
            UpdateUserResult result = service.handle(command);
            assertEquals(UpdateUserResult.Error.INVALID_USER_ID_FORMAT, result, "Expected INVALID_USER_ID_FORMAT error");
        }

        @Test
        @DisplayName("should return USER_NOT_FOUND when user does not exist")
        void shouldReturnUserNotFoundWhenUserDoesNotExist() {
            UpdateUserCommand command = new UpdateUserCommand("00055000-0000-0000-0000-000000000000", Optional.of(NEW_USERNAME), Optional.of(NEW_EMAIL));
            UpdateUserResult result = service.handle(command);
            assertEquals(UpdateUserResult.Error.USER_NOT_FOUND, result, "Expected USER_NOT_FOUND error");
        }

        @Test
        @DisplayName("should return NO_CHANGES when username, and email already exists")
        void shouldReturnNoChangesWhenUsernameAndEmailAlreadyExists() {
            UpdateUserCommand command = new UpdateUserCommand(
                    existingUser.getId().toString(),
                    Optional.of(existingUser.getUsername().username()),
                    Optional.of(existingUser.getEmail().emailAddress()
                    ));
            UpdateUserResult result = service.handle(command);
            assertEquals(UpdateUserResult.Error.NO_CHANGES, result, "Expected NO_CHANGES error");
        }

        @Test
        @DisplayName("should return USERNAME_ALREADY_EXISTS when new username already exists")
        void shouldReturnUsernameAlreadyExistsWhenNewUsernameAlreadyExists() {
            User anotherUser = new User(Username.of(NEW_USERNAME), Email.of("another@org.com"));
            userStore.save(anotherUser);
            UpdateUserCommand command = new UpdateUserCommand(existingUser.getId().toString(), Optional.of(NEW_USERNAME), Optional.empty());
            UpdateUserResult result = service.handle(command);
            assertEquals(UpdateUserResult.Error.USERNAME_ALREADY_EXISTS, result, "Expected USERNAME_ALREADY_EXISTS error");
        }

        @Test
        @DisplayName("should return INVALID_USERNAME_FORMAT when new username format is invalid")
        void shouldReturnInvalidUsernameFormatWhenNewUsernameFormatIsInvalid() {
            UpdateUserCommand command = new UpdateUserCommand(existingUser.getId().toString(), Optional.of("ab"), Optional.empty());
            UpdateUserResult result = service.handle(command);
            assertEquals(UpdateUserResult.Error.INVALID_USERNAME_FORMAT, result, "Expected INVALID_USERNAME_FORMAT error");
        }

        @Test
        @DisplayName("should return EMAIL_ALREADY_EXISTS when new email already exists")
        void shouldReturnEmailAlreadyExistsWhenNewEmailAlreadyExists() {
            User anotherUser = new User(Username.of("anotherUser"), Email.of(NEW_EMAIL));
            userStore.save(anotherUser);
            UpdateUserCommand command = new UpdateUserCommand(existingUser.getId().toString(), Optional.empty(), Optional.of(NEW_EMAIL));
            UpdateUserResult result = service.handle(command);
            assertEquals(UpdateUserResult.Error.EMAIL_ALREADY_EXISTS, result, "Expected EMAIL_ALREADY_EXISTS error");
        }

        @Test
        @DisplayName("should return INVALID_EMAIL_FORMAT when new email format is invalid")
        void shouldReturnInvalidEmailFormatWhenNewEmailFormatIsInvalid() {
            UpdateUserCommand command = new UpdateUserCommand(existingUser.getId().toString(), Optional.empty(), Optional.of("invalid-email"));
            UpdateUserResult result = service.handle(command);
            assertEquals(UpdateUserResult.Error.INVALID_EMAIL_FORMAT, result, "Expected INVALID_EMAIL_FORMAT error");
        }
    }

    @Nested
    @DisplayName("Successful Update")
    class SuccessfulUpdateTests {

        @Test
        @DisplayName("should update username and email successfully")
        void shouldUpdateUsernameAndEmailSuccessfully() {
            UpdateUserCommand command = new UpdateUserCommand(
                    existingUser.getId().toString(),
                    Optional.of(NEW_USERNAME),
                    Optional.of(NEW_EMAIL));
            UpdateUserResult result = service.handle(command);
            assertInstanceOf(UpdateUserResult.Ok.class, result,
                    String.format("Expected Ok result but got: %s", result));
            UpdateUserResult.Ok okResult = (UpdateUserResult.Ok) result;
            User updatedUser = userStore.findById(okResult.userId()).orElseThrow();
            assertEquals(updatedUser.getUsername(), Username.of(NEW_USERNAME), "Username should be updated");
            assertEquals(updatedUser.getEmail(), Email.of(NEW_EMAIL), "Email should be updated");
        }

        @Test
        @DisplayName("should update only username successfully")
        void shouldUpdateOnlyUsernameSuccessfully() {
            UpdateUserCommand command = new UpdateUserCommand(
                    existingUser.getId().toString(),
                    Optional.of(NEW_USERNAME),
                    Optional.empty());
            UpdateUserResult result = service.handle(command);
            assertInstanceOf(UpdateUserResult.Ok.class, result,
                    String.format("Expected Ok result but got: %s", result));
            UpdateUserResult.Ok okResult = (UpdateUserResult.Ok) result;
            User updatedUser = userStore.findById(okResult.userId()).orElseThrow();
            assertEquals(updatedUser.getUsername(), Username.of(NEW_USERNAME), "Username should be updated");
            assertEquals(updatedUser.getEmail(), existingUser.getEmail(), "Email should remain unchanged");
        }

        @Test
        @DisplayName("should update only email successfully")
        void shouldUpdateOnlyEmailSuccessfully() {
            UpdateUserCommand command = new UpdateUserCommand(
                    existingUser.getId().toString(),
                    Optional.empty(),
                    Optional.of(NEW_EMAIL));
            UpdateUserResult result = service.handle(command);
            assertInstanceOf(UpdateUserResult.Ok.class, result,
                    String.format("Expected Ok result but got: %s", result));
            UpdateUserResult.Ok okResult = (UpdateUserResult.Ok) result;
            User updatedUser = userStore.findById(okResult.userId()).orElseThrow();
            assertEquals(updatedUser.getUsername(), existingUser.getUsername(), "Username should remain unchanged");
            assertEquals(updatedUser.getEmail(), Email.of(NEW_EMAIL), "Email should be updated");
        }

    }
}
