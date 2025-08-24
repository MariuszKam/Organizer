package service.user.usecase.read;

import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.Username;
import com.organizer.service.user.adapter.InMemoryUserStore;
import com.organizer.service.user.port.UserStore;
import com.organizer.service.user.usecase.read.ReadUserResult;
import com.organizer.service.user.usecase.read.ReadUserService;
import com.organizer.service.user.usecase.read.command.ReadUserByIdCommand;
import com.organizer.service.user.usecase.read.command.ReadUserForLoginCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DisplayName("Use Case: Read User")
class ReadUserServiceTest {

    private static final String EXISTING_USERNAME = "existingUser";
    private static final String EXISTING_EMAIL = "exists@org.com";

    private ReadUserService service;
    private UserStore userStore;
    private User existingUser;

    @BeforeEach
    void setUp() {
        userStore = new InMemoryUserStore();
        existingUser = new User(Username.of(EXISTING_USERNAME), Email.of(EXISTING_EMAIL));
        userStore.save(existingUser);
        service = new ReadUserService(userStore);
    }

    @Nested
    @DisplayName("Validation - generic")
    class GenericValidationTests {

        @Test
        @DisplayName("should return MISSING_COMMAND when command is null")
        void shouldReturnMissingCommandWhenCommandIsNull() {
            ReadUserResult result = service.handle(null);
            assertEquals(ReadUserResult.Error.MISSING_COMMAND, result,
                    String.format("Expected MISSING_COMMAND but got: %s", result));
        }
    }

    @Nested
    @DisplayName("Read by ID")
    class ReadByIdTests {

        @Test
        @DisplayName("should return MISSING_USER_ID when id is null")
        void shouldReturnMissingUserIdWhenIdIsNull() {
            ReadUserResult result = service.handle(new ReadUserByIdCommand(null));
            assertEquals(ReadUserResult.Error.MISSING_USER_ID, result,
                    String.format("Expected MISSING_USER_ID but got: %s", result));
        }

        @Test
        @DisplayName("should return INVALID_USER_ID when id format is invalid")
        void shouldReturnInvalidUserIdWhenIdFormatIsInvalid() {
            ReadUserResult result = service.handle(new ReadUserByIdCommand("invalid-uuid"));
            assertEquals(ReadUserResult.Error.INVALID_USER_ID_FORMAT, result,
                    String.format("Expected INVALID_USER_ID but got: %s", result));
        }

        @Test
        @DisplayName("should return USER_NOT_FOUND when user does not exist")
        void shouldReturnUserNotFoundWhenUserDoesNotExist() {
            ReadUserResult result = service.handle(new ReadUserByIdCommand("00055000-0000-0000-0000-000000000000"));
            assertEquals(ReadUserResult.Error.USER_NOT_FOUND, result,
                    String.format("Expected USER_NOT_FOUND but got: %s", result));
        }

        @Test
        @DisplayName("should return Ok with the user when id exists")
        void shouldReturnOkWhenIdExists() {
            ReadUserResult result = service.handle(new ReadUserByIdCommand(existingUser.getId().toString()));
            assertInstanceOf(ReadUserResult.Ok.class, result,
                    String.format("Expected Ok but got: %s", result));
            ReadUserResult.Ok ok = (ReadUserResult.Ok) result;
            assertEquals(existingUser.getId(), ok.user().getId(), "Returned user should have the requested id");
        }
    }

    @Nested
    @DisplayName("Read for Login (username + email)")
    class ReadForLoginTests {

        @Test
        @DisplayName("should return NO_PROVIDED_PARAMETERS when both username and email are null")
        void shouldReturnNoProvidedParametersWhenBothNull() {
            ReadUserResult result = service.handle(new ReadUserForLoginCommand(null, null));
            assertEquals(ReadUserResult.Error.NO_PROVIDED_PARAMETERS, result,
                    String.format("Expected NO_PROVIDED_PARAMETERS but got: %s", result));
        }

        @Test
        @DisplayName("should return MISSING_USERNAME when username is null")
        void shouldReturnMissingUsernameWhenUsernameIsNull() {
            ReadUserResult result = service.handle(new ReadUserForLoginCommand(null, EXISTING_EMAIL));
            assertEquals(ReadUserResult.Error.MISSING_USERNAME, result,
                    String.format("Expected MISSING_USERNAME but got: %s", result));
        }

        @Test
        @DisplayName("should return MISSING_EMAIL when email is null")
        void shouldReturnMissingEmailWhenEmailIsNull() {
            ReadUserResult result = service.handle(new ReadUserForLoginCommand(EXISTING_USERNAME, null));
            assertEquals(ReadUserResult.Error.MISSING_EMAIL, result,
                    String.format("Expected MISSING_EMAIL but got: %s", result));
        }

        @Test
        @DisplayName("should return INVALID_USERNAME_FORMAT when username is malformed")
        void shouldReturnInvalidUsernameFormatWhenUsernameMalformed() {
            ReadUserResult result = service.handle(new ReadUserForLoginCommand("ab", "valid@org.com"));
            assertEquals(ReadUserResult.Error.INVALID_USERNAME_FORMAT, result,
                    String.format("Expected INVALID_USERNAME_FORMAT but got: %s", result));
        }

        @Test
        @DisplayName("should return INVALID_EMAIL_FORMAT when email is malformed")
        void shouldReturnInvalidEmailFormatWhenEmailMalformed() {
            ReadUserResult result = service.handle(new ReadUserForLoginCommand(EXISTING_USERNAME, "invalid-email"));
            assertEquals(ReadUserResult.Error.INVALID_EMAIL_FORMAT, result,
                    String.format("Expected INVALID_EMAIL_FORMAT but got: %s", result));
        }

        @Test
        @DisplayName("should return USERNAME_NOT_FOUND when username does not exist")
        void shouldReturnUsernameNotFoundWhenUsernameDoesNotExist() {
            ReadUserResult result = service.handle(new ReadUserForLoginCommand("unknownUser", EXISTING_EMAIL));
            assertEquals(ReadUserResult.Error.USERNAME_NOT_FOUND, result,
                    String.format("Expected USERNAME_NOT_FOUND but got: %s", result));
        }

        @Test
        @DisplayName("should return EMAIL_NOT_FOUND when email does not exist")
        void shouldReturnEmailNotFoundWhenEmailDoesNotExist() {
            ReadUserResult result = service.handle(new ReadUserForLoginCommand(EXISTING_USERNAME, "unknown@org.com"));
            assertEquals(ReadUserResult.Error.EMAIL_NOT_FOUND, result,
                    String.format("Expected EMAIL_NOT_FOUND but got: %s", result));
        }

        @Test
        @DisplayName("should return MISMATCH when username and email belong to different users")
        void shouldReturnMismatchWhenUsernameAndEmailBelongToDifferentUsers() {
            // given another user with different email
            User another = new User(Username.of("anotherUser"), Email.of("another@org.com"));
            userStore.save(another);

            ReadUserResult result = service.handle(new ReadUserForLoginCommand(EXISTING_USERNAME, "another@org.com"));
            assertEquals(ReadUserResult.Error.MISMATCH, result,
                    String.format("Expected MISMATCH but got: %s", result));
        }

        @Test
        @DisplayName("should return Ok when username and email match the same user")
        void shouldReturnOkWhenUsernameAndEmailMatchSameUser() {
            ReadUserResult result = service.handle(new ReadUserForLoginCommand(EXISTING_USERNAME, EXISTING_EMAIL));
            assertInstanceOf(ReadUserResult.Ok.class, result,
                    String.format("Expected Ok but got: %s", result));
            ReadUserResult.Ok ok = (ReadUserResult.Ok) result;
            assertEquals(existingUser.getId(), ok.user().getId(), "Returned user should match the login pair");
        }

        @Test
        @DisplayName("should accept canonicalization (case/trim) via VO and still return Ok")
        void shouldAcceptCanonicalizationAndReturnOk() {
            ReadUserResult result = service.handle(new ReadUserForLoginCommand("  " + EXISTING_USERNAME.toUpperCase() + "  ",
                    "  " + EXISTING_EMAIL.toUpperCase() + "  "));
            assertInstanceOf(ReadUserResult.Ok.class, result,
                    String.format("Expected Ok but got: %s", result));
            ReadUserResult.Ok ok = (ReadUserResult.Ok) result;
            assertEquals(existingUser.getId(), ok.user().getId(), "VO normalization should allow matching the same user");
        }
    }
}
