package service.task.usecase.read;

import com.organizer.model.task.*;
import com.organizer.service.task.adapter.InMemoryTaskStore;
import com.organizer.service.task.port.TaskStore;
import com.organizer.service.task.usecase.read.ReadTaskCommand;
import com.organizer.service.task.usecase.read.ReadTaskResult;
import com.organizer.service.task.usecase.read.ReadTaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Use Case: Read Task")
class ReadTaskServiceTest {

    private static final String EXISTING_ID_STR = "00000000-0000-0000-0000-000000000001";
    private static final String OTHER_ID_STR = "00000000-0000-0000-0000-000000000002";

    private TaskStore taskStore;
    private ReadTaskService service;
    private Task existingTask;

    @BeforeEach
    void setUp() {
        taskStore = new InMemoryTaskStore();

        TaskId existingId = TaskId.of(EXISTING_ID_STR);
        existingTask = new Task(
                existingId,
                TaskName.of("Task A"),
                TaskDescription.of("desc"),
                TaskPriority.MEDIUM,
                TaskStatus.TODO,
                null
        );
        taskStore.save(existingTask);

        service = new ReadTaskService(taskStore);
    }

    @Nested
    @DisplayName("Validation - generic")
    class GenericValidationTests {

        @Test
        @DisplayName("should return MISSING_COMMAND when command is null")
        void shouldReturnMissingCommandWhenCommandIsNull() {
            ReadTaskResult result = service.handle(null);
            assertEquals(ReadTaskResult.Error.MISSING_COMMAND, result,
                    String.format("Expected MISSING_COMMAND but got: %s", result));
        }
    }

    @Nested
    @DisplayName("Read by ID")
    class ReadByIdTests {

        @Test
        @DisplayName("should return MISSING_TASK_ID when id is null")
        void shouldReturnMissingTaskIdWhenIdIsNull() {
            ReadTaskCommand cmd = new ReadTaskCommand(null);
            ReadTaskResult result = service.handle(cmd);
            assertEquals(ReadTaskResult.Error.MISSING_TASK_ID, result,
                    String.format("Expected MISSING_TASK_ID but got: %s", result));
        }

        @Test
        @DisplayName("should return INVALID_TASK_ID_FORMAT when id format is invalid")
        void shouldReturnInvalidTaskIdFormatWhenIdFormatIsInvalid() {
            ReadTaskCommand cmd = new ReadTaskCommand("not-a-uuid");
            ReadTaskResult result = service.handle(cmd);
            assertEquals(ReadTaskResult.Error.INVALID_TASK_ID_FORMAT, result,
                    String.format("Expected INVALID_TASK_ID_FORMAT but got: %s", result));
        }

        @Test
        @DisplayName("should return NON_EXISTING_TASK when task is not found")
        void shouldReturnNonExistingTaskWhenTaskIsNotFound() {
            ReadTaskCommand cmd = new ReadTaskCommand(OTHER_ID_STR);
            ReadTaskResult result = service.handle(cmd);
            assertEquals(ReadTaskResult.Error.NON_EXISTING_TASK, result,
                    String.format("Expected NON_EXISTING_TASK but got: %s", result));
        }

        @Test
        @DisplayName("should return Ok with the task when id exists")
        void shouldReturnOkWithTaskWhenIdExists() {
            ReadTaskCommand cmd = new ReadTaskCommand(EXISTING_ID_STR);
            ReadTaskResult result = service.handle(cmd);
            assertInstanceOf(ReadTaskResult.Ok.class, result,
                    String.format("Expected Ok but got: %s", result));

            ReadTaskResult.Ok ok = (ReadTaskResult.Ok) result;
            Task returned = ok.task();

            assertEquals(TaskId.of(EXISTING_ID_STR), returned.getId(), "Returned task should have the requested id");
            assertEquals(TaskName.of("Task A"), returned.getName());
            assertEquals(TaskDescription.of("desc"), returned.getDescription());
            assertEquals(TaskPriority.MEDIUM, returned.getPriority());
            assertEquals(TaskStatus.TODO, returned.getStatus());
            assertNull(returned.getAssignedUser(), "Assignee should be null for seeded task");
        }
    }
}