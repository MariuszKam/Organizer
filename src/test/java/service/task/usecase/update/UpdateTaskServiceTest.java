package service.task.usecase.update;

import com.organizer.model.task.*;
import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.Username;
import com.organizer.service.task.adapter.InMemoryTaskStore;
import com.organizer.service.task.port.TaskStore;
import com.organizer.service.task.usecase.update.UpdateTaskCommand;
import com.organizer.service.task.usecase.update.UpdateTaskResult;
import com.organizer.service.task.usecase.update.UpdateTaskService;
import com.organizer.service.task.usecase.update.UpdateTaskUseCase;
import com.organizer.service.user.adapter.InMemoryUserStore;
import com.organizer.service.user.port.UserStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Use Case: Update Task")
class UpdateTaskServiceTest {

    private static final TaskId EXISTING_ID =
            TaskId.of("00000000-0000-0000-0000-000000000001");
    private static final TaskId ANOTHER_ID =
            TaskId.of("00000000-0000-0000-0000-000000000002");

    private TaskStore taskStore;
    private UserStore userStore;
    private UpdateTaskUseCase service;

    @BeforeEach
    void setUp() {
        taskStore = new InMemoryTaskStore();
        userStore = new InMemoryUserStore();
        service = new UpdateTaskService(taskStore, userStore);
    }

    private void seedTask(User assignee) {
        Task t = new Task(EXISTING_ID, TaskName.of("Task A"), TaskDescription.of("desc"), TaskPriority.MEDIUM, TaskStatus.TODO, assignee);
        taskStore.save(t);
    }

    @Nested
    @DisplayName("Validation (generic)")
    class ValidationGeneric {

        @Test
        @DisplayName("should return MISSING_COMMAND when command is null")
        void shouldReturnMissingCommandWhenCommandIsNull() {
            UpdateTaskResult result = service.handle(null);
            assertEquals(UpdateTaskResult.Error.MISSING_COMMAND, result);
        }

        @Test
        @DisplayName("should return MISSING_TASK_ID when taskId is null")
        void shouldReturnMissingTaskIdWhenTaskIdIsNull() {
            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    null, Optional.empty(), Optional.empty(),
                    Optional.empty(), Optional.empty(), Optional.empty()
            );
            UpdateTaskResult result = service.handle(cmd);
            assertEquals(UpdateTaskResult.Error.MISSING_TASK_ID, result);
        }

        @Test
        @DisplayName("should return INVALID_TASK_ID_FORMAT when taskId has wrong format")
        void shouldReturnInvalidTaskIdFormat() {
            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    "not-a-uuid", Optional.of("Name-A"), Optional.empty(),
                    Optional.empty(), Optional.empty(), Optional.empty()
            );
            UpdateTaskResult result = service.handle(cmd);
            assertEquals(UpdateTaskResult.Error.INVALID_TASK_ID_FORMAT, result);
        }

        @Test
        @DisplayName("should return NON_EXISTING_TASK when task not found")
        void shouldReturnNonExistingTaskWhenTaskNotFound() {
            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    ANOTHER_ID.value().toString(), Optional.of("New"), Optional.empty(),
                    Optional.empty(), Optional.empty(), Optional.empty()
            );
            UpdateTaskResult result = service.handle(cmd);
            assertEquals(UpdateTaskResult.Error.NON_EXISTING_TASK, result);
        }

        @Test
        @DisplayName("should return NO_FIELDS_PROVIDED when all optionals are empty")
        void shouldReturnNoFieldsProvidedWhenAllEmpty() {
            seedTask(null);

            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    EXISTING_ID.value().toString(), Optional.empty(), Optional.empty(),
                    Optional.empty(), Optional.empty(), Optional.empty()
            );
            UpdateTaskResult result = service.handle(cmd);
            assertEquals(UpdateTaskResult.Error.NO_FIELDS_PROVIDED, result);
        }
    }

    @Nested
    @DisplayName("Name / Description")
    class NameDescription {

        @Test
        @DisplayName("should return INVALID_TASK_NAME_FORMAT when TaskName VO rejects")
        void shouldReturnInvalidTaskNameFormat() {
            seedTask(null);

            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    EXISTING_ID.value().toString(), Optional.of("x".repeat(51)), Optional.empty(),
                    Optional.empty(), Optional.empty(), Optional.empty()
            );
            UpdateTaskResult result = service.handle(cmd);
            assertEquals(UpdateTaskResult.Error.INVALID_TASK_NAME_FORMAT, result);
        }

        @Test
        @DisplayName("should return INVALID_TASK_DESCRIPTION_FORMAT when TaskDescription VO rejects")
        void shouldReturnInvalidTaskDescriptionFormat() {
            seedTask(null);

            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    EXISTING_ID.value().toString(), Optional.empty(), Optional.of("x".repeat(501)),
                    Optional.empty(), Optional.empty(), Optional.empty()
            );
            UpdateTaskResult result = service.handle(cmd);
            assertEquals(UpdateTaskResult.Error.INVALID_TASK_DESCRIPTION_FORMAT, result);
        }

        @Test
        @DisplayName("should update only name when only name provided")
        void shouldUpdateOnlyName() {
            seedTask(null);

            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    EXISTING_ID.value().toString(), Optional.of("Renamed"), Optional.empty(),
                    Optional.empty(), Optional.empty(), Optional.empty()
            );
            UpdateTaskResult result = service.handle(cmd);
            assertInstanceOf(UpdateTaskResult.Ok.class, result);

            Task saved = taskStore.findById(EXISTING_ID).orElseThrow();
            assertEquals(TaskName.of("Renamed"), saved.getName());
            assertEquals(TaskDescription.of("desc"), saved.getDescription());
            assertEquals(TaskPriority.MEDIUM, saved.getPriority());
            assertEquals(TaskStatus.TODO, saved.getStatus());
            assertNull(saved.getAssignedUser());
        }

        @Test
        @DisplayName("should update only description when only description provided")
        void shouldUpdateOnlyDescription() {
            seedTask(null);

            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    EXISTING_ID.value().toString(), Optional.empty(), Optional.of("new desc"),
                    Optional.empty(), Optional.empty(), Optional.empty()
            );
            UpdateTaskResult result = service.handle(cmd);
            assertInstanceOf(UpdateTaskResult.Ok.class, result);

            Task saved = taskStore.findById(EXISTING_ID).orElseThrow();
            assertEquals(TaskName.of("Task A"), saved.getName());
            assertEquals(TaskDescription.of("new desc"), saved.getDescription());
            assertEquals(TaskPriority.MEDIUM, saved.getPriority());
            assertEquals(TaskStatus.TODO, saved.getStatus());
            assertNull(saved.getAssignedUser());
        }
    }

    @Nested
    @DisplayName("Priority / Status")
    class PriorityStatus {

        @Test
        @DisplayName("should return INVALID_TASK_PRIORITY_FORMAT when enum name unknown")
        void shouldReturnInvalidTaskPriorityFormat() {
            seedTask(null);

            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    EXISTING_ID.value().toString(), Optional.empty(), Optional.empty(),
                    Optional.of("URGENT"), Optional.empty(), Optional.empty()
            );
            UpdateTaskResult result = service.handle(cmd);
            assertEquals(UpdateTaskResult.Error.INVALID_TASK_PRIORITY_FORMAT, result);
        }

        @Test
        @DisplayName("should return INVALID_TASK_STATUS_FORMAT when enum name unknown")
        void shouldReturnInvalidTaskStatusFormat() {
            seedTask(null);

            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    EXISTING_ID.value().toString(), Optional.empty(), Optional.empty(),
                    Optional.empty(), Optional.of("BLOCKED"), Optional.empty()
            );
            UpdateTaskResult result = service.handle(cmd);
            assertEquals(UpdateTaskResult.Error.INVALID_TASK_STATUS_FORMAT, result);
        }

        @Test
        @DisplayName("should update priority and status when both provided")
        void shouldUpdatePriorityAndStatus() {
            seedTask(null);

            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    EXISTING_ID.value().toString(), Optional.empty(), Optional.empty(),
                    Optional.of("HIGH"), Optional.of("IN_PROGRESS"), Optional.empty()
            );
            UpdateTaskResult result = service.handle(cmd);
            assertInstanceOf(UpdateTaskResult.Ok.class, result);

            Task saved = taskStore.findById(EXISTING_ID).orElseThrow();
            assertEquals(TaskPriority.HIGH, saved.getPriority());
            assertEquals(TaskStatus.IN_PROGRESS, saved.getStatus());
        }
    }

    @Nested
    @DisplayName("Username / Assignee")
    class UsernameAssignee {

        @Test
        @DisplayName("should keep NULL assignee when username not provided and current is null")
        void shouldKeepNullAssigneeWhenUsernameNotProvidedAndCurrentNull() {
            seedTask(null);

            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    EXISTING_ID.value().toString(), Optional.of("Renamed"), Optional.empty(),
                    Optional.empty(), Optional.empty(), Optional.empty()
            );
            UpdateTaskResult result = service.handle(cmd);
            assertInstanceOf(UpdateTaskResult.Ok.class, result);

            Task saved = taskStore.findById(EXISTING_ID).orElseThrow();
            assertNull(saved.getAssignedUser());
        }

        @Test
        @DisplayName("should keep EXISTING assignee when username not provided and current has user")
        void shouldKeepExistingAssigneeWhenUsernameNotProvidedAndCurrentHasUser() {
            User existing = new User(Username.of("assignee"), Email.of("assignee@org.com"));
            userStore.save(existing);
            seedTask(existing);

            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    EXISTING_ID.value().toString(), Optional.of("Renamed"), Optional.empty(),
                    Optional.empty(), Optional.empty(), Optional.empty()
            );
            UpdateTaskResult result = service.handle(cmd);
            assertInstanceOf(UpdateTaskResult.Ok.class, result);

            Task saved = taskStore.findById(EXISTING_ID).orElseThrow();
            assertNotNull(saved.getAssignedUser());
            assertEquals(existing.getId(), saved.getAssignedUser().getId());
        }

        @Test
        @DisplayName("should return INVALID_USERNAME_FORMAT when Username VO rejects")
        void shouldReturnInvalidUsernameFormat() {
            seedTask(null);

            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    EXISTING_ID.value().toString(), Optional.empty(), Optional.empty(),
                    Optional.empty(), Optional.empty(), Optional.of("ab") // za krótki
            );
            UpdateTaskResult result = service.handle(cmd);
            assertEquals(UpdateTaskResult.Error.INVALID_USERNAME_FORMAT, result);
        }

        @Test
        @DisplayName("should return NON_EXISTING_USER when provided username not found")
        void shouldReturnNonExistingUserWhenProvidedUsernameNotFound() {
            seedTask(null);

            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    EXISTING_ID.value().toString(), Optional.empty(), Optional.empty(),
                    Optional.empty(), Optional.empty(), Optional.of("unknownUser")
            );
            UpdateTaskResult result = service.handle(cmd);
            assertEquals(UpdateTaskResult.Error.NON_EXISTING_USER, result);
        }

        @Test
        @DisplayName("should update assignee when valid username provided and user exists")
        void shouldUpdateAssigneeWhenValidUserProvided() {
            User existing = new User(Username.of("assignee"), Email.of("assignee@org.com"));
            userStore.save(existing);
            seedTask(null);

            UpdateTaskCommand cmd = new UpdateTaskCommand(
                    EXISTING_ID.value().toString(), Optional.empty(), Optional.empty(),
                    Optional.empty(), Optional.empty(), Optional.of("assignee")
            );
            UpdateTaskResult result = service.handle(cmd);
            assertInstanceOf(UpdateTaskResult.Ok.class, result);

            Task saved = taskStore.findById(EXISTING_ID).orElseThrow();
            assertNotNull(saved.getAssignedUser());
            assertEquals(existing.getId(), saved.getAssignedUser().getId());
        }
    }
}