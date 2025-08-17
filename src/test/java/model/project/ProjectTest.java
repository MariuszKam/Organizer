package model.project;

import com.organizer.model.project.Project;
import com.organizer.model.project.ProjectName;
import com.organizer.model.task.Task;
import com.organizer.model.task.TaskDescription;
import com.organizer.model.task.TaskName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectTest {

    @Test
    public void testProjectCreation() {
        Project project = createProjectWithTasks();

        assertEquals(1L, project.getId(), String.format("Expected project ID to be 1, but got %d", project.getId()));

        assertEquals("Project with Tasks", project.getName().name(),
                String.format("Expected project name to be \"Project with Tasks\", but got %s", project.getName()));
        assertNotNull(project.getTaskList(), "Expected task list to be initialized, but it was null");
    }

    @Test
    public void testProjectAddTask() {
        Project project = new Project(1L, new ProjectName("Test Project"));
        Task task1 = new Task(1L, new TaskName("Task 1"),new TaskDescription("Description for Task 1"));
        Task task2 = new Task(2L, new TaskName("Task 2"),new TaskDescription("Description for Task 2"));

        project.addTask(task1);
        project.addTask(task2);

        List<Task> tasks = project.getTaskList();
        assertEquals(2, tasks.size(), String.format("Expected task list size to be 2, but got %d", tasks.size()));
        assertEquals(1L, tasks.get(0).getId(), String.format("Expected first task ID to be 1, but got %d", tasks.get(0).getId()));
        assertEquals(2L, tasks.get(1).getId(), String.format("Expected second task ID to be 2, but got %d", tasks.get(1).getId()));
    }

    @Test
    public void testProjectWithNullId() {
        assertThrows(NullPointerException.class, () ->
            new Project(null, new ProjectName("Invalid Project")), "Expected IllegalArgumentException for null project ID");
    }

    @Test
    public void testProjectWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () ->
            new Project(1L,new ProjectName("")), "Expected IllegalArgumentException for empty project name");
    }

    @Test
    public void testProjectWithNullName() {
        assertThrows(NullPointerException.class, () ->
                new Project(1L, null), "Expected IllegalArgumentException for null project name");
    }

    @Test
    public void testSetNewName() {
        Project project = createProjectWithTasks();
        assertEquals("Project with Tasks", project.getName().name(), String.format("Expected project name to be \"Project with Tasks\", but got %s", project.getName()));

        project.changeProjectName(new ProjectName("Updated Name"));
        assertEquals("Updated Name", project.getName().name(), String.format("Expected project name to be \"Updated Name\", but got %s", project.getName()));
    }

    @Test
    public void testProjectIsEqual() {
        Project project1 = new Project(1L, new ProjectName("Project A"));
        Project project2 = new Project(1L, new ProjectName("Project B"));

        assertEquals(project1, project2, "Projects with the same ID should be equal");
    }

    @Test
    public void testProjectIsNotEqual() {
        Project project1 = new Project(1L, new ProjectName("Project A"));
        Project project2 = new Project(2L, new ProjectName("Project B"));

        assertNotEquals(project1, project2, "Projects with different IDs should not be equal");
    }

    @Test
    public void testHashCodeEqual() {
        Project project1 = new Project(1L, new ProjectName("Project A"));
        Project project2 = new Project(1L, new ProjectName("Project B"));

        assertEquals(project1.hashCode(), project2.hashCode(), "Projects with the same ID should have the same hash code");
    }

    @Test
    public void testHashCodeNotEqual() {
        Project project1 = new Project(1L, new ProjectName("Project A"));
        Project project2 = new Project(2L, new ProjectName("Project B"));

        assertNotEquals(project1.hashCode(), project2.hashCode(), "Projects with different IDs should have different hash codes");
    }

    private Project createProjectWithTasks() {
        Project project = new Project(1L, new ProjectName("Project with Tasks"));
        project.addTask(new Task(1L, new TaskName("Task 1"), new TaskDescription("Description for Task 1")));
        project.addTask(new Task(2L, new TaskName("Task 2"), new TaskDescription("Description for Task 2")));
        return project;
    }

}
