package service.user.usecase.create;

import com.organizer.service.user.adapter.InMemoryUserStore;
import com.organizer.service.user.adapter.UUIDGenerator;
import com.organizer.service.user.port.IdGenerator;
import com.organizer.service.user.port.UserStore;
import com.organizer.service.user.usecase.create.CreateUserCommand;
import com.organizer.service.user.usecase.create.CreateUserResult;
import com.organizer.service.user.usecase.create.CreateUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Use Case: Create User")
class CreateUserServiceTest {

    private static final String VALID_USERNAME = "validUser";
    private static final String VALID_EMAIL = "example@org.com";

    private UserStore userStore;
    private IdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        userStore = new InMemoryUserStore();
        idGenerator = new UUIDGenerator();
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Nested
        @DisplayName("Command Validation")
        class CommandValidationTests {

            @Test
            @DisplayName("should return MISSING_COMMAND when command is null")
            void shouldReturnMissingCommandWhenCommandIsNull() {
                CreateUserCommand command = null;
                CreateUserService service = new CreateUserService(userStore, idGenerator);
                CreateUserResult result = service.handle(command);
                assertEquals(CreateUserResult.Error.MISSING_COMMAND, result,
                        String.format("Expected MISSING_COMMAND but got: %s", result));
            }

        }

        @Nested
        @DisplayName("Username Validation")
        class UsernameValidationTests {

            @Test
            @DisplayName("should return USERNAME_ALREADY_EXISTS when username already exists")
            void shouldReturnUsernameAlreadyExistsWhenUsernameExists() {
                CreateUserCommand command1 = new CreateUserCommand(VALID_USERNAME, VALID_EMAIL);
                CreateUserCommand command2 = new CreateUserCommand(VALID_USERNAME, "example2@org.com");
                CreateUserService service = new CreateUserService(userStore, idGenerator);
                service.handle(command1);
                CreateUserResult result2 = service.handle(command2);
                assertEquals(CreateUserResult.Error.USERNAME_ALREADY_EXISTS, result2,
                        String.format("Expected USERNAME_ALREADY_EXISTS but got: %s", result2));
            }


            @Test
            @DisplayName("should return INVALID_USERNAME_FORMAT when username format is wrong")
            void shouldReturnInvalidUsernameWhenFormatIsWrong() {
                CreateUserCommand command = new CreateUserCommand("ab", VALID_EMAIL);
                CreateUserService service = new CreateUserService(userStore, idGenerator);
                CreateUserResult result = service.handle(command);
                assertEquals(CreateUserResult.Error.INVALID_USERNAME_FORMAT, result,
                        String.format("Expected INVALID_USERNAME_FORMAT but got: %s", result));
            }

            @Test
            @DisplayName("should return MISSING_USERNAME when username is null")
            void shouldReturnMissingUsernameWhenUsernameIsNull() {
                CreateUserCommand command = new CreateUserCommand(null, VALID_EMAIL);
                CreateUserService service = new CreateUserService(userStore, idGenerator);
                CreateUserResult result = service.handle(command);
                assertEquals(CreateUserResult.Error.MISSING_USERNAME, result,
                        String.format("Expected MISSING_USERNAME but got: %s", result));
            }

        }

        @Nested
        @DisplayName("Email Validation")
        class EmailValidationTests {

            @Test
            @DisplayName("should return EMAIL_ALREADY_EXISTS when email already exists")
            void shouldReturnEmailAlreadyExistsWhenEmailExists() {
                CreateUserCommand command1 = new CreateUserCommand(VALID_USERNAME, VALID_EMAIL);
                CreateUserCommand command2 = new CreateUserCommand("anotherUser", VALID_EMAIL);
                CreateUserService service = new CreateUserService(userStore, idGenerator);
                service.handle(command1);
                CreateUserResult result2 = service.handle(command2);
                assertEquals(CreateUserResult.Error.EMAIL_ALREADY_EXISTS, result2,
                        String.format("Expected EMAIL_ALREADY_EXISTS but got: %s", result2));
            }

            @Test
            @DisplayName("should return MISSING_EMAIL when email is null")
            void shouldReturnMissingEmailWhenEmailIsNull() {
                CreateUserCommand command = new CreateUserCommand(VALID_USERNAME, null);
                CreateUserService service = new CreateUserService(userStore, idGenerator);
                CreateUserResult result = service.handle(command);
                assertEquals(CreateUserResult.Error.MISSING_EMAIL, result,
                        String.format("Expected MISSING_EMAIL but got: %s", result));
            }

            @Test
            @DisplayName("should return INVALID_EMAIL_FORMAT when email format is wrong")
            void shouldReturnInvalidEmailWhenFormatIsWrong() {
                CreateUserCommand command = new CreateUserCommand(VALID_USERNAME, "invalid-email");
                CreateUserService service = new CreateUserService(userStore, idGenerator);
                CreateUserResult result = service.handle(command);
                assertEquals(CreateUserResult.Error.INVALID_EMAIL_FORMAT, result,
                        String.format("Expected INVALID_EMAIL_FORMAT but got: %s", result));
            }

        }

    }

    @Nested
    @DisplayName("Successful Creation")
    class SuccessfulCreationTests {

        @Test
        @DisplayName("should create user successfully with valid command")
        void shouldCreateUserSuccessfullyWithValidCommand() {
            CreateUserCommand command = new CreateUserCommand(VALID_USERNAME, VALID_EMAIL);
            CreateUserService service = new CreateUserService(userStore, idGenerator);
            CreateUserResult result = service.handle(command);
            assertInstanceOf(CreateUserResult.Ok.class, result,
                    String.format("Expected result to be instance of Ok but got: %s", result));
        }

        @Test
        @DisplayName("should match generated userId in result")
        void shouldMatchGeneratedUserIdInResult() {
            CreateUserCommand command = new CreateUserCommand(VALID_USERNAME, VALID_EMAIL);
            CreateUserService service = new CreateUserService(userStore, idGenerator);
            CreateUserResult result = service.handle(command);
            assertInstanceOf(CreateUserResult.Ok.class, result,
                    String.format("Expected result to be instance of Ok but got: %s", result));
            CreateUserResult.Ok okResult = (CreateUserResult.Ok) result;
            assertNotNull(userStore.findById(okResult.userId()), "User should be found in store by generated userId");
        }
    }
}
