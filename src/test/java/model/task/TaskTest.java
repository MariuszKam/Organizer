package model.task;

import com.organizer.model.task.*;
import com.organizer.model.user.Email;
import com.organizer.model.user.User;
import com.organizer.model.user.Username;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testTaskCreation() {
        Task task = createTaskWithValidData();

        assertEquals(1L, task.getId(), String.format("Expected task ID to be 1, but got %d", task.getId()));
        assertEquals("Test Task", task.getName().name(), String.format("Expected task name to be \"Test Task\", but got %s", task.getName()));
        assertEquals("This is a test task description.", task.getDescription().description(),
                     String.format("Expected task description to be \"This is a test task description.\", but got %s", task.getDescription()));
        assertEquals("Medium", task.getPriority().getDisplayName(),
                     String.format("Expected task priority to be \"Medium\", but got %s", task.getPriority().getDisplayName()));
        assertEquals("To Do", task.getStatus().getDisplayName(),
                     String.format("Expected task status to be \"Todo\", but got %s", task.getStatus().getDisplayName()));
    }

    @Test
    public void testTaskWithNullId() {
        assertThrows(NullPointerException.class, () ->
            new Task(null, new TaskName("Invalid Task"), new TaskDescription("This task has a null ID.")),
            "Expected IllegalArgumentException for null task ID");
    }

    @Test
    public void testTaskWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Task(1L, new TaskName(""),new TaskDescription("This task has an empty name.")),
                     "Expected IllegalArgumentException for empty task name");
    }

    @Test
    public void testTaskWithNullName() {
        assertThrows(NullPointerException.class, () ->
                new Task(1L, null, new TaskDescription("This task has a null name.")),
                     "Expected IllegalArgumentException for null task name");
    }

    @Test
    public void testTaskWithEmptyDescription() {
        assertThrows(IllegalArgumentException.class, () ->
                new Task(1L, new TaskName("Valid Task"), new TaskDescription("")),
                     "Expected IllegalArgumentException for empty task description");
    }

    @Test
    public void testTaskWithNullDescription() {
        assertThrows(NullPointerException.class, () ->
                new Task(1L, new TaskName("Valid Task"), null),
                     "Expected IllegalArgumentException for null task description");
    }

    @Test
    public void testSetNewName() {
        Task task = createTaskWithValidData();
        assertEquals("Test Task", task.getName().name(),
                     String.format("Expected task name to be \"Test Task\", but got %s", task.getName()));

        task.changeTaskName(new TaskName("Updated Name"));
        assertEquals("Updated Name", task.getName().name(),
                     String.format("Expected task name to be \"Updated Name\", but got %s", task.getName()));
    }

    @Test
    public void testSetNewNameWithNull() {
        Task task = new Task(1L, new TaskName("Task Name"), new TaskDescription("Task Description"));
        assertThrows(NullPointerException.class, () -> task.changeTaskName(null),
                     "Expected IllegalArgumentException when setting task name to null");
    }

    @Test
    public void testSetNewDescription() {
        Task task = createTaskWithValidData();
        assertEquals("This is a test task description.", task.getDescription().description(),
                     String.format("Expected task description to be \"This is a test task description.\", but got %s", task.getDescription()));

        task.changeTaskDescription(new TaskDescription("Updated Description"));
        assertEquals("Updated Description", task.getDescription().description(),
                     String.format("Expected task description to be \"Updated Description\", but got %s", task.getDescription()));
    }

    @Test
    public void testSetNewDescriptionWithNull() {
        Task task = new Task(1L, new TaskName("Task Name"), new TaskDescription("Task Description"));
        assertThrows(NullPointerException.class, () -> task.changeTaskDescription(null),
                     "Expected IllegalArgumentException when setting task description to null");
    }

    @Test
    public void testSetPriorityHigh() {
        Task task = createTaskWithValidData();
        assertEquals("Medium", task.getPriority().getDisplayName(),
                     String.format("Expected task priority to be \"Medium\", but got %s", task.getPriority().getDisplayName()));

        task.changePriority(TaskPriority.HIGH);
        assertEquals("High", task.getPriority().getDisplayName(),
                     String.format("Expected task priority to be \"High\", but got %s", task.getPriority().getDisplayName()));
    }

    @Test
    public void testSetPriorityLow() {
        Task task = createTaskWithValidData();
        assertEquals("Medium", task.getPriority().getDisplayName(),
                     String.format("Expected task priority to be \"Medium\", but got %s", task.getPriority().getDisplayName()));

        task.changePriority(TaskPriority.LOW);
        assertEquals("Low", task.getPriority().getDisplayName(),
                     String.format("Expected task priority to be \"Low\", but got %s", task.getPriority().getDisplayName()));
    }

    @Test
    public void testSetStatusInProgress() {
        Task task = createTaskWithValidData();
        assertEquals("To Do", task.getStatus().getDisplayName(),
                     String.format("Expected task status to be \"Todo\", but got %s", task.getStatus().getDisplayName()));

        task.changeStatus(TaskStatus.IN_PROGRESS);
        assertEquals("In Progress", task.getStatus().getDisplayName(),
                     String.format("Expected task status to be \"In Progress\", but got %s", task.getStatus().getDisplayName()));
    }

    @Test
    public void testSetStatusDone() {
        Task task = createTaskWithValidData();
        assertEquals("To Do", task.getStatus().getDisplayName(),
                     String.format("Expected task status to be \"Todo\", but got %s", task.getStatus().getDisplayName()));

        task.changeStatus(TaskStatus.DONE);
        assertEquals("Done", task.getStatus().getDisplayName(),
                     String.format("Expected task status to be \"Done\", but got %s", task.getStatus().getDisplayName()));
    }

    @Test
    public void testTaskIsEqual() {
        Task task1 = new Task(1L, new TaskName("Task A"), new TaskDescription("Description A"));
        Task task2 = new Task(1L, new TaskName("Task B"), new TaskDescription("Description B"));

        assertEquals(task1, task2, "Tasks with the same ID should be equal");
    }

    @Test
    public void testTaskIsNotEqual() {
        Task task1 = new Task(1L, new TaskName("Task A"), new TaskDescription("Description A"));
        Task task2 = new Task(2L, new TaskName("Task B"), new TaskDescription("Description B"));

        assertNotEquals(task1, task2, "Tasks with different IDs should not be equal");
    }

    @Test
    public void testTaskHashCode() {
        Task task1 = new Task(1L, new TaskName("Task A"), new TaskDescription("Description A"));
        Task task2 = new Task(1L, new TaskName("Task B"), new TaskDescription("Description B"));

        assertEquals(task1.hashCode(), task2.hashCode(), "Tasks with the same ID should have the same hash code");
    }

    @Test
    public void testTaskHashCodeNotEqual() {
        Task task1 = new Task(1L, new TaskName("Task A"), new TaskDescription("Description A"));
        Task task2 = new Task(2L, new TaskName("Task B"), new TaskDescription("Description B"));

        assertNotEquals(task1.hashCode(), task2.hashCode(), "Tasks with different IDs should have different hash codes");
    }

    @Test
    public void testAssignUser() {
        Task task = createTaskWithValidData();
        User user = new User(1L, new Username("testuser"), new Email("example@org.com"));

        task.assignUser(user);
        assertEquals(user, task.getAssignedUser(), "Assigned user should match the user assigned to the task");
    }

    @Test
    public void testAssignUserWithNull() {
        Task task = createTaskWithValidData();
        assertThrows(NullPointerException.class, () -> task.assignUser(null),
                     "Expected IllegalArgumentException when assigning null user to task");
    }

    private Task createTaskWithValidData() {
        return new Task(1L, new TaskName("Test Task"), new TaskDescription("This is a test task description."));
    }

}
