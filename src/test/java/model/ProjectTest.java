package model;

import com.organizer.model.Project;
import com.organizer.model.task.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectTest {

    @Test
    public void testProjectCreation() {
        Project project = new Project(1L, "Test Project", List.of(new Task(1L, "Task 1", "Description 1"),
                new Task(2L, "Task 2", "Description 2")));

        assertEquals(1L, project.getId(), String.format("Expected project ID to be 1, but got %d", project.getId()));
        assertEquals("Test Project", project.getName(), String.format("Expected project name to be \"Test Project\", but got %s", project.getName()));
        assertEquals(2, project.getTaskList().size(), "Expected project to have 2 tasks, but got " + project.getTaskList().size());
    }

}
