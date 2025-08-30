package service.task.adapter;

import com.organizer.model.task.*;
import com.organizer.service.task.adapter.InMemoryTaskStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemoryTaskStore Tests")
class InMemoryTaskStoreTest {

    private static final TaskId ID1 =
            TaskId.of("00000000-0000-0000-0000-000000000001");
    private static final TaskId ID2 =
            TaskId.of("00000000-0000-0000-0000-000000000002");

    private static Task taskWithDefaults(TaskId id, String name, String desc) {
        return new Task(id, TaskName.of(name), TaskDescription.of(desc));
    }

    private static Task taskFull(TaskId id, String name, String desc, TaskPriority p, TaskStatus s) {
        return new Task(id, TaskName.of(name), TaskDescription.of(desc), p, s, null);
    }

    @Nested
    @DisplayName("Save Task Tests")
    class SaveTests {

        @Test
        @DisplayName("should save task successfully")
        void shouldSaveTaskSuccessfully() {
            InMemoryTaskStore store = new InMemoryTaskStore();
            Task t = taskWithDefaults(ID1, "Task A", "desc");
            store.save(t);
            assertTrue(store.findById(ID1).isPresent(), "Task should be saved and retrievable by ID");
        }

        @Test
        @DisplayName("should throw NPE when saving null task")
        void shouldThrowWhenSavingNull() {
            InMemoryTaskStore store = new InMemoryTaskStore();
            assertThrows(NullPointerException.class, () -> store.save(null),
                    "Saving null should throw NPE");
        }

        @Test
        @DisplayName("should overwrite task with same id (upsert semantics)")
        void shouldOverwriteTaskWithSameId() {
            InMemoryTaskStore store = new InMemoryTaskStore();
            Task t1 = taskWithDefaults(ID1, "Task A", "desc");
            Task t2 = taskFull(ID1, "Task B", "desc2", TaskPriority.HIGH, TaskStatus.IN_PROGRESS);

            store.save(t1);
            store.save(t2); // overwrite

            Task saved = store.findById(ID1).orElseThrow();
            assertEquals(TaskName.of("Task B"), saved.getName(), "Name should be updated");
            assertEquals(TaskDescription.of("desc2"), saved.getDescription(), "Description should be updated");
            assertEquals(TaskPriority.HIGH, saved.getPriority(), "Priority should be updated");
            assertEquals(TaskStatus.IN_PROGRESS, saved.getStatus(), "Status should be updated");
        }
    }

    @Nested
    @DisplayName("Find All / Immutability")
    class FindAllTests {

        @Test
        @DisplayName("should return immutable copy of tasks")
        void shouldReturnImmutableList() {
            InMemoryTaskStore store = new InMemoryTaskStore();
            store.save(taskWithDefaults(ID1, "Task A", "desc"));
            store.save(taskWithDefaults(ID2, "Task B", "desc"));

            List<Task> all = store.findAll();
            assertEquals(2, all.size());
            assertThrows(UnsupportedOperationException.class, () -> all.add(
                            taskWithDefaults(TaskId.of("00000000-0000-0000-0000-000000000003"), "Task C", "x")),
                    "Returned list should be immutable");
        }
    }

    @Nested
    @DisplayName("Remove Task Tests")
    class RemoveTests {

        @Test
        @DisplayName("should throw NPE when removing null task")
        void shouldThrowWhenRemovingNull() {
            InMemoryTaskStore store = new InMemoryTaskStore();
            assertThrows(NullPointerException.class, () -> store.remove(null),
                    "Removing null should throw NPE");
        }

        @Test
        @DisplayName("should remove existing task (idempotent for second call)")
        void shouldRemoveExistingTaskAndBeIdempotent() {
            InMemoryTaskStore store = new InMemoryTaskStore();
            Task t = taskWithDefaults(ID1, "Task A", "desc");
            store.save(t);

            assertDoesNotThrow(() -> store.remove(t), "First remove should not throw");
            assertTrue(store.findById(ID1).isEmpty(), "Task should be removed");

            assertDoesNotThrow(() -> store.remove(t), "Second remove should be idempotent");
            assertTrue(store.findById(ID1).isEmpty(), "Task remains removed");
        }

        @Test
        @DisplayName("should not change store size when removing non-existing task")
        void shouldNotChangeSizeWhenRemovingNonExistingTask() {
            InMemoryTaskStore store = new InMemoryTaskStore();
            Task t = taskWithDefaults(ID1, "Task A", "desc");
            store.save(t);
            int before = store.findAll().size();

            Task notSaved = taskWithDefaults(ID2, "Task B", "desc");
            assertDoesNotThrow(() -> store.remove(notSaved), "Removing non-existing should not throw");

            int after = store.findAll().size();
            assertEquals(before, after, "Size should remain unchanged");
            assertTrue(store.findById(ID1).isPresent(), "Existing task should remain");
        }
    }
}
