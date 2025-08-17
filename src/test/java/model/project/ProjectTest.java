package model.project;

import com.organizer.model.project.Project;
import com.organizer.model.project.ProjectId;
import com.organizer.model.project.ProjectName;
import com.organizer.model.task.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Project Aggregate Tests")
class ProjectTest {

    private static final ProjectName PROJECT_NAME = ProjectName.of("Project with Tasks");
    private static final TaskName T1_NAME = TaskName.of("Task 1");
    private static final TaskName T2_NAME = TaskName.of("Task 2");
    private static final TaskDescription T1_DESC = TaskDescription.of("Description for Task 1");
    private static final TaskDescription T2_DESC = TaskDescription.of("Description for Task 2");

    @Nested
    @DisplayName("Creation")
    class CreationTests {

        @Test
        @DisplayName("should create project with generated ProjectId")
        void shouldCreateProjectWithGeneratedId() {
            Project project = new Project(PROJECT_NAME);
            assertNotNull(project.getId(), "Project ID should be generated");
            assertEquals("Project with Tasks", project.getName().name(),
                    "Project name should match the provided name");
            assertNotNull(project.getTaskList(), "Task list should be initialized");
            assertTrue(project.getTaskList().isEmpty(), "Task list should be empty on creation");
        }

        @Test
        @DisplayName("should create project with explicit ProjectId")
        void shouldCreateProjectWithExplicitId() {
            ProjectId id = ProjectId.of("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
            Project project = new Project(id, PROJECT_NAME);
            assertEquals(id, project.getId(), "Project ID should match explicit value");
        }

        @Test
        @DisplayName("should throw when id or name is null")
        void shouldThrowOnNulls() {
            assertThrows(NullPointerException.class, () -> new Project(null, PROJECT_NAME),
                    "Project creation should throw NullPointerException when ProjectId is null");
            assertThrows(NullPointerException.class, () -> new Project(ProjectId.newId(), null),
                    "Project creation should throw NullPointerException when ProjectName is null");
        }
    }

    @Nested
    @DisplayName("Tasks Management")
    class TasksManagementTests {

        @Test
        @DisplayName("should add tasks and expose unmodifiable task list")
        void shouldAddTasksAndExposeUnmodifiableList() {
            Project project = new Project(PROJECT_NAME);
            Task t1 = new Task(T1_NAME, T1_DESC);
            Task t2 = new Task(T2_NAME, T2_DESC);

            project.addTask(t1);
            project.addTask(t2);

            List<Task> tasks = project.getTaskList();
            assertEquals(2, tasks.size(), "Task list should contain 2 tasks after adding");
            assertEquals(t1.getId(), tasks.get(0).getId(), "First task ID should match Task 1 ID");
            assertEquals(t2.getId(), tasks.get(1).getId(), "Second task ID should match Task 2 ID");

            assertThrows(UnsupportedOperationException.class, () ->
                            tasks.add(new Task(TaskName.of("X"), TaskDescription.of("Y"))),
                    "Task list should be unmodifiable");
        }

        @Test
        @DisplayName("should throw when adding duplicate TaskId to project")
        void shouldThrowOnDuplicateTask() {
            Project project = new Project(PROJECT_NAME);
            TaskId same = TaskId.of("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
            Task t1 = new Task(same, T1_NAME, T1_DESC);
            Task t2 = new Task(same, T2_NAME, T2_DESC);

            project.addTask(t1);
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> project.addTask(t2));
            assertTrue(ex.getMessage().contains(same.toString()), "Exception message should contain duplicate TaskId");
        }

        @Test
        @DisplayName("should throw when adding null task")
        void shouldThrowWhenAddingNullTask() {
            Project project = new Project(PROJECT_NAME);
            assertThrows(NullPointerException.class, () -> project.addTask(null),
                    "Adding null task should throw NullPointerException");
        }
    }

    @Nested
    @DisplayName("Rename")
    class RenameTests {

        @Test
        @DisplayName("should change project name")
        void shouldChangeProjectName() {
            Project project = new Project(PROJECT_NAME);
            project.changeProjectName(ProjectName.of("Updated Name"));
            assertEquals("Updated Name", project.getName().name(), "Project name should be updated");
        }

        @Test
        @DisplayName("should throw when new name is null")
        void shouldThrowWhenNameNull() {
            Project project = new Project(PROJECT_NAME);
            assertThrows(NullPointerException.class, () -> project.changeProjectName(null),
                    "ChangeProjectName should throw NullPointerException when new name is null");
        }
    }

    @Nested
    @DisplayName("Equality & HashCode")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("should be equal for the same ProjectId")
        void shouldBeEqualForSameId() {
            ProjectId same = ProjectId.of("cccccccc-cccc-cccc-cccc-cccccccccccc");
            Project p1 = new Project(same, PROJECT_NAME);
            Project p2 = new Project(same, ProjectName.of("Another Name"));
            assertEquals(p1, p2, "Projects with same ProjectId should be equal");
            assertEquals(p1.hashCode(), p2.hashCode(), "Hash codes should match for equal Project objects");
        }

        @Test
        @DisplayName("should not be equal for different ProjectIds")
        void shouldNotBeEqualForDifferentIds() {
            Project p1 = new Project(ProjectId.of("dddddddd-dddd-dddd-dddd-dddddddddddd"), PROJECT_NAME);
            Project p2 = new Project(ProjectId.of("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee"), PROJECT_NAME);
            assertNotEquals(p1, p2, "Projects with different ProjectIds should not be equal");
        }
    }
}
