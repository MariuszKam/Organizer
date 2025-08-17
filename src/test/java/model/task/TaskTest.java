package model.task;

import com.organizer.model.task.*;
import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.Username;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task Aggregate Tests")
class TaskTest {

    private static final TaskName NAME = TaskName.of("Implement login");
    private static final TaskDescription DESC = TaskDescription.of("Implement authentication with JWT.");
    private static final Email EMAIL = Email.of("example@org.com");
    private static final Username USERNAME = Username.of("testUser");

    @Nested
    @DisplayName("Creation")
    class CreationTests {

        @Test
        @DisplayName("should create task with generated TaskId and default priority/status")
        void shouldCreateWithGeneratedIdAndDefaults() {
            Task task = new Task(NAME, DESC);
            assertNotNull(task.getId(), "TaskId should be generated");
            assertEquals(NAME, task.getName());
            assertEquals(DESC, task.getDescription());
            assertEquals(TaskPriority.MEDIUM, task.getPriority(), "Default priority should be MEDIUM");
            assertEquals(TaskStatus.TODO, task.getStatus(), "Default status should be TODO");
            assertNull(task.getAssignedUser(), "Assigned user should be null by default");
        }

        @Test
        @DisplayName("should create task with explicit TaskId")
        void shouldCreateWithExplicitId() {
            TaskId id = TaskId.of("11111111-1111-1111-1111-111111111111");
            Task task = new Task(id, NAME, DESC);
            assertEquals(id, task.getId(), "TaskId should match explicit value");
        }

        @Test
        @DisplayName("should throw when any required argument is null")
        void shouldThrowOnNulls() {
            assertThrows(NullPointerException.class, () -> new Task(null, DESC),
                    "Task creation should throw NullPointerException when name is null");
            assertThrows(NullPointerException.class, () -> new Task(NAME, null),
                    "Task creation should throw NullPointerException when description is null");
            TaskId id = TaskId.newId();
            assertThrows(NullPointerException.class, () -> new Task(null, NAME, DESC),
                    "Task creation should throw NullPointerException when TaskId is null");
            assertThrows(NullPointerException.class, () -> new Task(id, null, DESC),
                    "Task creation should throw NullPointerException when name is null");
            assertThrows(NullPointerException.class, () -> new Task(id, NAME, null),
                    "Task creation should throw NullPointerException when description is null");
        }

        @Test
        @DisplayName("should create with full constructor and validate non-nulls")
        void shouldCreateWithFullCtor() {
            User assignee = new User(USERNAME, EMAIL);
            Task task = new Task(TaskId.newId(), NAME, DESC, TaskPriority.HIGH, TaskStatus.IN_PROGRESS, assignee);
            assertEquals(TaskPriority.HIGH, task.getPriority(), "Priority should match HIGH");
            assertEquals(TaskStatus.IN_PROGRESS, task.getStatus(), "Status should match IN_PROGRESS");
            assertEquals(assignee, task.getAssignedUser(), "Assigned user should match provided user");
        }
    }

    @Nested
    @DisplayName("Change Operations")
    class ChangeOperationsTests {

        @Test
        @DisplayName("should change name/description/priority/status")
        void shouldChangeFields() {
            Task task = new Task(NAME, DESC);
            TaskName newName = TaskName.of("Implement logout");
            TaskDescription newDesc = TaskDescription.of("Add logout endpoint.");
            task.changeTaskName(newName);
            task.changeTaskDescription(newDesc);
            task.changePriority(TaskPriority.LOW);
            task.changeStatus(TaskStatus.DONE);

            assertEquals(newName, task.getName(), "Name should be updated");
            assertEquals(newDesc, task.getDescription(), "Description should be updated");
            assertEquals(TaskPriority.LOW, task.getPriority(), "Priority should be updated to LOW");
            assertEquals(TaskStatus.DONE, task.getStatus(), "Status should be updated to DONE");
        }

        @Test
        @DisplayName("should throw on null in change operations")
        void shouldThrowOnNullInChanges() {
            Task task = new Task(NAME, DESC);
            assertThrows(NullPointerException.class, () -> task.changeTaskName(null),
                    "ChangeTaskName should throw NullPointerException when name is null");
            assertThrows(NullPointerException.class, () -> task.changeTaskDescription(null),
                    "ChangeTaskDescription should throw NullPointerException when description is null");
            assertThrows(NullPointerException.class, () -> task.changePriority(null),
                    "ChangePriority should throw NullPointerException when priority is null");
            assertThrows(NullPointerException.class, () -> task.changeStatus(null),
                    "ChangeStatus should throw NullPointerException when status is null");
        }

        @Test
        @DisplayName("should assign user (non-null)")
        void shouldAssignUser() {
            Task task = new Task(NAME, DESC);
            User user = new User(Username.of("otheruser"), Email.of("example2@org.com"));
            task.assignUser(user);
            assertEquals(user, task.getAssignedUser(), "Assigned user should match the one set");
        }

        @Test
        @DisplayName("should throw when assigning null user")
        void shouldThrowWhenAssigningNull() {
            Task task = new Task(NAME, DESC);
            assertThrows(NullPointerException.class, () -> task.assignUser(null),
                    "AssignUser should throw NullPointerException when user is null");
        }
    }

    @Nested
    @DisplayName("Equality & HashCode")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("should be equal for the same TaskId")
        void shouldBeEqualForSameId() {
            TaskId same = TaskId.of("22222222-2222-2222-2222-222222222222");
            Task t1 = new Task(same, NAME, DESC);
            Task t2 = new Task(same, NAME, DESC);
            assertEquals(t1, t2, "Tasks with same TaskId should be equal");
            assertEquals(t1.hashCode(), t2.hashCode(), "Hash codes should match for equal Task objects");
        }

        @Test
        @DisplayName("should not be equal for different TaskIds")
        void shouldNotBeEqualForDifferentIds() {
            Task t1 = new Task(TaskId.of("33333333-3333-3333-3333-333333333333"), NAME, DESC);
            Task t2 = new Task(TaskId.of("44445444-4444-4444-4444-444444444444"), NAME, DESC);
            assertNotEquals(t1, t2, "Tasks with different TaskIds should not be equal");
            assertNotEquals(t1.hashCode(), t2.hashCode(), "Hash codes should differ for different TaskIds");
        }
    }
}
