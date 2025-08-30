package service.project.adapter;

import com.organizer.model.project.Project;
import com.organizer.model.project.ProjectId;
import com.organizer.model.project.ProjectName;
import com.organizer.service.project.adapter.InMemoryProjectStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemoryProjectStore Tests")
class InMemoryProjectStoreTest {

    private static final ProjectId ID1 =
            ProjectId.of("00000000-0000-0000-0000-000000000001");
    private static final ProjectId ID2 =
            ProjectId.of("00000000-0000-0000-0000-000000000002");

    private static Project project(ProjectId id, String name) {
        return new Project(id, ProjectName.of(name));
    }

    @Nested
    @DisplayName("Save Project Tests")
    class SaveTests {

        @Test
        @DisplayName("should save project successfully")
        void shouldSaveProjectSuccessfully() {
            InMemoryProjectStore store = new InMemoryProjectStore();
            Project p = project(ID1, "Project A");
            store.save(p);
            assertTrue(store.findById(ID1).isPresent(), "Project should be saved and retrievable by ID");
        }

        @Test
        @DisplayName("should throw NPE when saving null project")
        void shouldThrowWhenSavingNull() {
            InMemoryProjectStore store = new InMemoryProjectStore();
            assertThrows(NullPointerException.class, () -> store.save(null),
                    "Saving null should throw NPE");
        }

        @Test
        @DisplayName("should overwrite project with same id (upsert semantics)")
        void shouldOverwriteProjectWithSameId() {
            InMemoryProjectStore store = new InMemoryProjectStore();
            Project p1 = project(ID1, "Project A");
            Project p2 = project(ID1, "Project B"); // overwrite same ID

            store.save(p1);
            store.save(p2);

            Project saved = store.findById(ID1).orElseThrow();
            assertEquals(ProjectName.of("Project B"), saved.getName(), "Name should be updated");
        }
    }

    @Nested
    @DisplayName("Find All / Immutability")
    class FindAllTests {

        @Test
        @DisplayName("should return immutable copy of projects")
        void shouldReturnImmutableList() {
            InMemoryProjectStore store = new InMemoryProjectStore();
            store.save(project(ID1, "Project A"));
            store.save(project(ID2, "Project B"));

            List<Project> all = store.findAll();
            assertEquals(2, all.size(), "Should return all saved projects");
            assertThrows(UnsupportedOperationException.class, () -> all.add(
                            project(ProjectId.of("00000000-0000-0000-0000-000000000003"), "Project C")),
                    "Returned list should be immutable");
        }
    }

    @Nested
    @DisplayName("Remove Project Tests")
    class RemoveTests {

        @Test
        @DisplayName("should throw NPE when removing null project")
        void shouldThrowWhenRemovingNull() {
            InMemoryProjectStore store = new InMemoryProjectStore();
            assertThrows(NullPointerException.class, () -> store.remove(null),
                    "Removing null should throw NPE");
        }

        @Test
        @DisplayName("should remove existing project (idempotent for second call)")
        void shouldRemoveExistingProjectAndBeIdempotent() {
            InMemoryProjectStore store = new InMemoryProjectStore();
            Project p = project(ID1, "Project A");
            store.save(p);

            assertDoesNotThrow(() -> store.remove(p), "First remove should not throw");
            assertTrue(store.findById(ID1).isEmpty(), "Project should be removed");

            assertDoesNotThrow(() -> store.remove(p), "Second remove should be idempotent");
            assertTrue(store.findById(ID1).isEmpty(), "Project remains removed");
        }

        @Test
        @DisplayName("should not change store size when removing non-existing project")
        void shouldNotChangeSizeWhenRemovingNonExistingProject() {
            InMemoryProjectStore store = new InMemoryProjectStore();
            Project p = project(ID1, "Project A");
            store.save(p);
            int before = store.findAll().size();

            Project notSaved = project(ID2, "Project B");
            assertDoesNotThrow(() -> store.remove(notSaved), "Removing non-existing should not throw");

            int after = store.findAll().size();
            assertEquals(before, after, "Size should remain unchanged");
            assertTrue(store.findById(ID1).isPresent(), "Existing project should remain");
        }
    }
}
