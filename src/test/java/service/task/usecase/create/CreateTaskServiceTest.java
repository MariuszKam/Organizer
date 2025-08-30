package service.task.usecase.create;

import com.organizer.model.task.*;
import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.Username;
import com.organizer.service.task.adapter.InMemoryTaskStore;
import com.organizer.service.task.port.GeneratorId;
import com.organizer.service.task.port.TaskStore;
import com.organizer.service.task.usecase.create.CreateTaskResult;
import com.organizer.service.task.usecase.create.CreateTaskService;
import com.organizer.service.task.usecase.create.CreateTaskUseCase;
import com.organizer.service.task.usecase.create.command.CreateBasicTaskCommand;
import com.organizer.service.task.usecase.create.command.CreateFullTaskCommand;
import com.organizer.service.user.adapter.InMemoryUserStore;
import com.organizer.service.user.port.UserStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Use Case: Create Task")
class CreateTaskServiceTest {

    private static final TaskId FIXED_ID =
            TaskId.of("11111111-1111-1111-1111-111111111111");

    private TaskStore taskStore;
    private UserStore userStore;
    private CreateTaskUseCase service;

    @BeforeEach
    void setUp() {
        taskStore = new InMemoryTaskStore();
        userStore = new InMemoryUserStore();
        GeneratorId idGen = () -> FIXED_ID;
        service = new CreateTaskService(taskStore, userStore, idGen);
    }

    @Nested
    @DisplayName("Validation (generic)")
    class ValidationGeneric {

        @Test
        @DisplayName("should return MISSING_COMMAND when command is null")
        void shouldReturnMissingCommandWhenCommandIsNull() {
            CreateTaskResult result = service.handle(null);
            assertEquals(CreateTaskResult.Error.MISSING_COMMAND, result,
                    String.format("Expected MISSING_COMMAND but got: %s", result));
            assertTrue(taskStore.findAll().isEmpty(), "Store should remain unchanged on error");
        }
    }

    @Nested
    @DisplayName("Basic path (name + description)")
    class BasicPath {

        @Test
        @DisplayName("should return MISSING_TASK_NAME when name is null")
        void shouldReturnMissingTaskNameWhenNameIsNull() {
            CreateBasicTaskCommand cmd = new CreateBasicTaskCommand(null, "desc");
            CreateTaskResult result = service.handle(cmd);
            assertEquals(CreateTaskResult.Error.MISSING_TASK_NAME, result);
            assertTrue(taskStore.findAll().isEmpty(), "Should not save on error");
        }

        @Test
        @DisplayName("should return MISSING_TASK_DESCRIPTION when description is null")
        void shouldReturnMissingTaskDescriptionWhenDescriptionIsNull() {
            CreateBasicTaskCommand cmd = new CreateBasicTaskCommand("Task A", null);
            CreateTaskResult result = service.handle(cmd);
            assertEquals(CreateTaskResult.Error.MISSING_TASK_DESCRIPTION, result);
            assertTrue(taskStore.findAll().isEmpty(), "Should not save on error");
        }

        @Test
        @DisplayName("should return INVALID_TASK_NAME_FORMAT when TaskName VO rejects input")
        void shouldReturnInvalidTaskNameFormatWhenVoRejectsName() {
            CreateBasicTaskCommand cmd = new CreateBasicTaskCommand("x".repeat(51), "desc");
            CreateTaskResult result = service.handle(cmd);
            assertEquals(CreateTaskResult.Error.INVALID_TASK_NAME_FORMAT, result);
            assertTrue(taskStore.findAll().isEmpty(), "Should not save on VO error");
        }

        @Test
        @DisplayName("should return INVALID_TASK_DESCRIPTION_FORMAT when TaskDescription VO rejects input")
        void shouldReturnInvalidTaskDescriptionFormatWhenVoRejectsDesc() {
            CreateBasicTaskCommand cmd = new CreateBasicTaskCommand("Task A", "x".repeat(501));
            CreateTaskResult result = service.handle(cmd);
            assertEquals(CreateTaskResult.Error.INVALID_TASK_DESCRIPTION_FORMAT, result);
            assertTrue(taskStore.findAll().isEmpty(), "Should not save on VO error");
        }

        @Test
        @DisplayName("should create task with defaults (MEDIUM, TODO) and return same generated ID")
        void shouldCreateBasicTaskWithDefaultsAndReturnSameId() {
            CreateBasicTaskCommand cmd = new CreateBasicTaskCommand("Task A", "Short description");

            CreateTaskResult result = service.handle(cmd);
            assertInstanceOf(CreateTaskResult.Ok.class, result,
                    String.format("Expected Ok but got: %s", result));

            CreateTaskResult.Ok ok = (CreateTaskResult.Ok) result;
            assertEquals(FIXED_ID, ok.taskId(), "Returned id must match GeneratorId");

            Task saved = taskStore.findById(FIXED_ID).orElseThrow();
            assertEquals(TaskName.of("Task A"), saved.getName());
            assertEquals(TaskDescription.of("Short description"), saved.getDescription());
            assertEquals(TaskPriority.MEDIUM, saved.getPriority(), "Default priority = MEDIUM");
            assertEquals(TaskStatus.TODO, saved.getStatus(), "Default status = TODO");
            assertNull(saved.getAssignedUser(), "Basic path should not assign a user");
        }
    }

    @Nested
    @DisplayName("Full path (name, description, priority, status, username)")
    class FullPath {

        private static final String ASSIGNEE_USERNAME = "assignee";
        private static final String ASSIGNEE_EMAIL = "assignee@org.com";

        @Test
        @DisplayName("should return MISSING_TASK_PRIORITY when priority is null")
        void shouldReturnMissingTaskPriorityWhenPriorityNull() {
            CreateFullTaskCommand cmd = new CreateFullTaskCommand("Task A", "desc", null, "TODO", "user");
            CreateTaskResult result = service.handle(cmd);
            assertEquals(CreateTaskResult.Error.MISSING_TASK_PRIORITY, result);
            assertTrue(taskStore.findAll().isEmpty(), "Should not save on error");
        }

        @Test
        @DisplayName("should return INVALID_TASK_PRIORITY_NAME when priority is not recognized")
        void shouldReturnInvalidTaskPriorityNameWhenUnknown() {
            CreateFullTaskCommand cmd = new CreateFullTaskCommand("Task A", "desc", "URGENT", "TODO", "user");
            CreateTaskResult result = service.handle(cmd);
            assertEquals(CreateTaskResult.Error.INVALID_TASK_PRIORITY_NAME, result);
            assertTrue(taskStore.findAll().isEmpty(), "Should not save on error");
        }

        @Test
        @DisplayName("should return MISSING_TASK_STATUS when status is null")
        void shouldReturnMissingTaskStatusWhenStatusNull() {
            CreateFullTaskCommand cmd = new CreateFullTaskCommand("Task A", "desc", "MEDIUM", null, "user");
            CreateTaskResult result = service.handle(cmd);
            assertEquals(CreateTaskResult.Error.MISSING_TASK_STATUS, result);
            assertTrue(taskStore.findAll().isEmpty(), "Should not save on error");
        }

        @Test
        @DisplayName("should return INVALID_TASK_STATUS_NAME when status is not recognized")
        void shouldReturnInvalidTaskStatusNameWhenUnknown() {
            CreateFullTaskCommand cmd = new CreateFullTaskCommand("Task A", "desc", "MEDIUM", "BLOCKED", "user");
            CreateTaskResult result = service.handle(cmd);
            assertEquals(CreateTaskResult.Error.INVALID_TASK_STATUS_NAME, result);
            assertTrue(taskStore.findAll().isEmpty(), "Should not save on error");
        }

        @Test
        @DisplayName("should return MISSING_USERNAME when username is null")
        void shouldReturnMissingUsernameWhenNull() {
            CreateFullTaskCommand cmd = new CreateFullTaskCommand("Task A", "desc", "HIGH", "IN_PROGRESS", null);
            CreateTaskResult result = service.handle(cmd);
            assertEquals(CreateTaskResult.Error.MISSING_USERNAME, result);
            assertTrue(taskStore.findAll().isEmpty(), "Should not save on error");
        }

        @Test
        @DisplayName("should return INVALID_USERNAME_FORMAT when username VO rejects value")
        void shouldReturnInvalidUsernameFormatWhenVoRejects() {
            CreateFullTaskCommand cmd = new CreateFullTaskCommand("Task A", "desc", "HIGH", "IN_PROGRESS", "ab");
            CreateTaskResult result = service.handle(cmd);
            assertEquals(CreateTaskResult.Error.INVALID_USERNAME_FORMAT, result);
            assertTrue(taskStore.findAll().isEmpty(), "Should not save on error");
        }

        @Test
        @DisplayName("should return NON_EXISTING_USER when assignee not found")
        void shouldReturnNonExistingUserWhenNotFound() {
            CreateFullTaskCommand cmd = new CreateFullTaskCommand("Task A", "desc", "HIGH", "IN_PROGRESS", "unknownUser");
            CreateTaskResult result = service.handle(cmd);
            assertEquals(CreateTaskResult.Error.NON_EXISTING_USER, result);
            assertTrue(taskStore.findAll().isEmpty(), "Should not save on error");
        }

        @Test
        @DisplayName("should create task with given priority/status/user and return same generated ID")
        void shouldCreateFullTaskSuccessfully() {
            User existing = new User(Username.of(ASSIGNEE_USERNAME), Email.of(ASSIGNEE_EMAIL));
            userStore.save(existing);

            CreateFullTaskCommand cmd = new CreateFullTaskCommand("Task A", "desc", "HIGH", "IN_PROGRESS", ASSIGNEE_USERNAME);

            CreateTaskResult result = service.handle(cmd);
            assertInstanceOf(CreateTaskResult.Ok.class, result,
                    String.format("Expected Ok but got: %s", result));

            CreateTaskResult.Ok ok = (CreateTaskResult.Ok) result;
            assertEquals(FIXED_ID, ok.taskId(), "Returned id must match GeneratorId");

            Task saved = taskStore.findById(FIXED_ID).orElseThrow();
            assertEquals(TaskName.of("Task A"), saved.getName());
            assertEquals(TaskDescription.of("desc"), saved.getDescription());
            assertEquals(TaskPriority.HIGH, saved.getPriority());
            assertEquals(TaskStatus.IN_PROGRESS, saved.getStatus());
            assertNotNull(saved.getAssignedUser(), "Assignee should be set");
            assertEquals(existing.getId(), saved.getAssignedUser().getId(), "Assigned user should match existing user");
        }
    }
}
