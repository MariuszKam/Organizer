package service.task.usecase.delete;

import com.organizer.model.task.*;
import com.organizer.service.task.adapter.InMemoryTaskStore;
import com.organizer.service.task.port.TaskStore;
import com.organizer.service.task.usecase.delete.DeleteTaskCommand;
import com.organizer.service.task.usecase.delete.DeleteTaskResult;
import com.organizer.service.task.usecase.delete.DeleteTaskService;
import com.organizer.service.task.usecase.delete.DeleteTaskUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Use Case: Delete Task")
class DeleteTaskServiceTest {

    private static final String ID1_STR = "00000000-0000-0000-0000-000000000001";
    private static final String ID2_STR = "00000000-0000-0000-0000-000000000002";

    private static final TaskId ID1 = TaskId.of(ID1_STR);
    private static final TaskId ID2 = TaskId.of(ID2_STR);

    private TaskStore taskStore;
    private DeleteTaskUseCase service;

    @BeforeEach
    void setUp() {
        taskStore = new InMemoryTaskStore();
        service = new DeleteTaskService(taskStore);
    }

    private void seedTask(TaskId id, String name, String desc,
                          TaskPriority p, TaskStatus s) {
        Task t = new Task(id, TaskName.of(name), TaskDescription.of(desc), p, s, null);
        taskStore.save(t);
    }

    @Nested
    @DisplayName("Validation (generic)")
    class ValidationGeneric {

        @Test
        @DisplayName("should return MISSING_COMMAND when command is null")
        void shouldReturnMissingCommandWhenCommandIsNull() {
            DeleteTaskResult result = service.handle(null);
            assertEquals(DeleteTaskResult.Error.MISSING_COMMAND, result);
            assertTrue(taskStore.findAll().isEmpty(), "Store must remain unchanged on error");
        }

        @Test
        @DisplayName("should return MISSING_TASK_ID when id is null")
        void shouldReturnMissingTaskIdWhenIdIsNull() {
            seedTask(ID1, "Task A", "desc", TaskPriority.MEDIUM, TaskStatus.TODO);

            DeleteTaskCommand cmd = new DeleteTaskCommand(null);
            DeleteTaskResult result = service.handle(cmd);

            assertEquals(DeleteTaskResult.Error.MISSING_TASK_ID, result);
            assertTrue(taskStore.findById(ID1).isPresent(), "Existing task must remain");
        }

        @Test
        @DisplayName("should return INVALID_TASK_ID_FORMAT when id is not a UUID")
        void shouldReturnInvalidTaskIdFormatWhenIdIsNotUuid() {
            seedTask(ID1, "Task A", "desc", TaskPriority.MEDIUM, TaskStatus.TODO);

            DeleteTaskCommand cmd = new DeleteTaskCommand("not-a-uuid");
            DeleteTaskResult result = service.handle(cmd);

            assertEquals(DeleteTaskResult.Error.INVALID_TASK_ID_FORMAT, result);
            assertTrue(taskStore.findById(ID1).isPresent(), "Existing task must remain");
        }

        @Test
        @DisplayName("should return NON_EXISTING_TASK when id not found")
        void shouldReturnNonExistingTaskWhenIdNotFound() {
            seedTask(ID1, "Task A", "desc", TaskPriority.MEDIUM, TaskStatus.TODO);

            DeleteTaskCommand cmd = new DeleteTaskCommand(ID2_STR);
            DeleteTaskResult result = service.handle(cmd);

            assertEquals(DeleteTaskResult.Error.NON_EXISTING_TASK, result);
            assertTrue(taskStore.findById(ID1).isPresent(), "Existing task must remain");
            assertTrue(taskStore.findById(ID2).isEmpty(), "Non-existing id should still be absent");
        }
    }

    @Nested
    @DisplayName("Happy path / Effects")
    class HappyPath {

        @Test
        @DisplayName("should delete task and return Ok with same id")
        void shouldDeleteTaskAndReturnOkWithSameId() {
            seedTask(ID1, "Task A", "desc", TaskPriority.MEDIUM, TaskStatus.TODO);
            assertTrue(taskStore.findById(ID1).isPresent(), "Precondition: task exists");

            DeleteTaskCommand cmd = new DeleteTaskCommand(ID1_STR);
            DeleteTaskResult result = service.handle(cmd);

            assertInstanceOf(DeleteTaskResult.Ok.class, result,
                    String.format("Expected Ok but got: %s", result));
            DeleteTaskResult.Ok ok = (DeleteTaskResult.Ok) result;
            assertEquals(ID1, ok.taskId(), "Returned id must match requested id");
            assertTrue(taskStore.findById(ID1).isEmpty(), "Task must be removed after delete");
        }

        @Test
        @DisplayName("should not affect other tasks when deleting one")
        void shouldNotAffectOtherTasksWhenDeletingOne() {
            seedTask(ID1, "Task A", "desc", TaskPriority.MEDIUM, TaskStatus.TODO);
            seedTask(ID2, "Task B", "desc2", TaskPriority.LOW, TaskStatus.IN_PROGRESS);

            DeleteTaskCommand cmd = new DeleteTaskCommand(ID1_STR);
            DeleteTaskResult result = service.handle(cmd);
            assertInstanceOf(DeleteTaskResult.Ok.class, result);

            assertTrue(taskStore.findById(ID1).isEmpty(), "Deleted task should be gone");
            assertTrue(taskStore.findById(ID2).isPresent(), "Other task must remain intact");
        }

        @Test
        @DisplayName("should return NON_EXISTING_TASK when deleting already deleted task")
        void shouldReturnNonExistingTaskWhenDeletingAlreadyDeletedTask() {
            seedTask(ID1, "Task A", "desc", TaskPriority.MEDIUM, TaskStatus.TODO);

            // first delete succeeds
            DeleteTaskResult first = service.handle(new DeleteTaskCommand(ID1_STR));
            assertInstanceOf(DeleteTaskResult.Ok.class, first);
            assertTrue(taskStore.findById(ID1).isEmpty(), "Task must be removed after first delete");

            // second delete = idempotency from UC perspective: now it's 'not found'
            DeleteTaskResult second = service.handle(new DeleteTaskCommand(ID1_STR));
            assertEquals(DeleteTaskResult.Error.NON_EXISTING_TASK, second,
                    "Second delete should report NON_EXISTING_TASK");
        }
    }
}
