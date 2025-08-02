package model.task;

import com.organizer.model.task.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
