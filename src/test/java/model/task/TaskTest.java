package model.task;

import com.organizer.model.task.Task;
import com.organizer.model.task.TaskPriority;
import com.organizer.model.task.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testTaskCreation() {
        Task task = new Task(1L, "Test Task", "This is a test task description.");

        assertEquals(1L, task.getId(), String.format("Expected task ID to be 1, but got %d", task.getId()));
        assertEquals("Test Task", task.getName(), String.format("Expected task name to be \"Test Task\", but got %s", task.getName()));
        assertEquals("This is a test task description.", task.getDescription(),
                     String.format("Expected task description to be \"This is a test task description.\", but got %s", task.getDescription()));
        assertEquals("Medium", task.getPriority().getDisplayName(),
                     String.format("Expected task priority to be \"Medium\", but got %s", task.getPriority().getDisplayName()));
        assertEquals("Todo", task.getStatus().getDisplayName(),
                     String.format("Expected task status to be \"Todo\", but got %s", task.getStatus().getDisplayName()));
    }

    @Test
    public void testTaskWithNullId() {
        assertThrows(IllegalArgumentException.class, () ->
            new Task(null, "Invalid Task", "This task has a null ID."),
            "Expected IllegalArgumentException for null task ID");
    }

    @Test
    public void testTaskWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Task(1L, "", "This task has an empty name."),
                     "Expected IllegalArgumentException for empty task name");
    }

    @Test
    public void testTaskWithNullName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Task(1L, null, "This task has a null name."),
                     "Expected IllegalArgumentException for null task name");
    }

    @Test
    public void testTaskWithEmptyDescription() {
        assertThrows(IllegalArgumentException.class, () ->
                new Task(1L, "Valid Task", ""),
                     "Expected IllegalArgumentException for empty task description");
    }

    @Test
    public void testTaskWithNullDescription() {
        assertThrows(IllegalArgumentException.class, () ->
                new Task(1L, "Valid Task", null),
                     "Expected IllegalArgumentException for null task description");
    }

    @Test
    public void testSetNewName() {
        Task task = new Task(1L, "Initial Name", "Initial Description");
        assertEquals("Initial Name", task.getName(),
                     String.format("Expected task name to be \"Initial Name\", but got %s", task.getName()));

        task.setName("Updated Name");
        assertEquals("Updated Name", task.getName(),
                     String.format("Expected task name to be \"Updated Name\", but got %s", task.getName()));
    }

    @Test
    public void testSetNewDescription() {
        Task task = new Task(1L, "Task Name", "Initial Description");
        assertEquals("Initial Description", task.getDescription(),
                     String.format("Expected task description to be \"Initial Description\", but got %s", task.getDescription()));

        task.setDescription("Updated Description");
        assertEquals("Updated Description", task.getDescription(),
                     String.format("Expected task description to be \"Updated Description\", but got %s", task.getDescription()));
    }

    @Test
    public void testSetPriorityHigh() {
        Task task = new Task(1L, "Task Name", "Task Description");
        assertEquals("Medium", task.getPriority().getDisplayName(),
                     String.format("Expected task priority to be \"Medium\", but got %s", task.getPriority().getDisplayName()));

        task.setPriority(TaskPriority.HIGH);
        assertEquals("High", task.getPriority().getDisplayName(),
                     String.format("Expected task priority to be \"High\", but got %s", task.getPriority().getDisplayName()));
    }

    @Test
    public void testSetPriorityLow() {
        Task task = new Task(1L, "Task Name", "Task Description");
        assertEquals("Medium", task.getPriority().getDisplayName(),
                     String.format("Expected task priority to be \"Medium\", but got %s", task.getPriority().getDisplayName()));

        task.setPriority(TaskPriority.LOW);
        assertEquals("Low", task.getPriority().getDisplayName(),
                     String.format("Expected task priority to be \"Low\", but got %s", task.getPriority().getDisplayName()));
    }

    @Test
    public void testSetStatusInProgress() {
        Task task = new Task(1L, "Task Name", "Task Description");
        assertEquals("Todo", task.getStatus().getDisplayName(),
                     String.format("Expected task status to be \"Todo\", but got %s", task.getStatus().getDisplayName()));

        task.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals("In Progress", task.getStatus().getDisplayName(),
                     String.format("Expected task status to be \"In Progress\", but got %s", task.getStatus().getDisplayName()));
    }

    @Test
    public void testSetStatusDone() {
        Task task = new Task(1L, "Task Name", "Task Description");
        assertEquals("Todo", task.getStatus().getDisplayName(),
                     String.format("Expected task status to be \"Todo\", but got %s", task.getStatus().getDisplayName()));

        task.setStatus(TaskStatus.DONE);
        assertEquals("Done", task.getStatus().getDisplayName(),
                     String.format("Expected task status to be \"Done\", but got %s", task.getStatus().getDisplayName()));
    }

    @Test
    public void testTaskIsEqual() {
        Task task1 = new Task(1L, "Task A", "Description A");
        Task task2 = new Task(1L, "Task B", "Description B");

        assertEquals(task1, task2, "Tasks with the same ID should be equal");
    }

    @Test
    public void testTaskIsNotEqual() {
        Task task1 = new Task(1L, "Task A", "Description A");
        Task task2 = new Task(2L, "Task B", "Description B");

        assertEquals(task1, task2, "Tasks with different IDs should not be equal");
    }

    @Test
    public void testTaskHashCode() {
        Task task1 = new Task(1L, "Task A", "Description A");
        Task task2 = new Task(1L, "Task B", "Description B");

        assertEquals(task1.hashCode(), task2.hashCode(), "Tasks with the same ID should have the same hash code");
    }

    @Test
    public void testTaskHashCodeNotEqual() {
        Task task1 = new Task(1L, "Task A", "Description A");
        Task task2 = new Task(2L, "Task B", "Description B");

        assertNotEquals(task1.hashCode(), task2.hashCode(), "Tasks with different IDs should have different hash codes");
    }

}
