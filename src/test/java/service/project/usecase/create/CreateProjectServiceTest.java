package service.project.usecase.create;

import com.organizer.model.project.Project;
import com.organizer.model.project.ProjectId;
import com.organizer.model.project.ProjectName;
import com.organizer.service.project.adapter.InMemoryProjectStore;
import com.organizer.service.project.port.GeneratorId;
import com.organizer.service.project.port.ProjectStore;
import com.organizer.service.project.usecase.create.CreateProjectCommand;
import com.organizer.service.project.usecase.create.CreateProjectResult;
import com.organizer.service.project.usecase.create.CreateProjectService;
import com.organizer.service.project.usecase.create.CreateProjectUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Use Case: Create Project")
class CreateProjectServiceTest {

    private static final ProjectId FIXED_ID =
            ProjectId.of("11111111-1111-1111-1111-111111111111");

    private ProjectStore store;
    private GeneratorId idGen;
    private CreateProjectUseCase service;

    @BeforeEach
    void setUp() {
        store = new InMemoryProjectStore();
        idGen = () -> FIXED_ID; // deterministyczny generator do asercji
        service = new CreateProjectService(store, idGen);
    }

    @Nested
    @DisplayName("Validation (generic)")
    class ValidationGeneric {

        @Test
        @DisplayName("should return MISSING_COMMAND when command is null")
        void shouldReturnMissingCommandWhenCommandIsNull() {
            CreateProjectResult result = service.handle(null);
            assertEquals(CreateProjectResult.Error.MISSING_COMMAND, result);
            assertTrue(store.findAll().isEmpty(), "Store should remain unchanged on error");
        }

        @Test
        @DisplayName("should return MISSING_PROJECT_NAME when name is null")
        void shouldReturnMissingProjectNameWhenNameIsNull() {
            CreateProjectCommand cmd = new CreateProjectCommand(null);
            CreateProjectResult result = service.handle(cmd);
            assertEquals(CreateProjectResult.Error.MISSING_PROJECT_NAME, result);
            assertTrue(store.findAll().isEmpty(), "Should not save on error");
        }

        @Test
        @DisplayName("should return INVALID_PROJECT_NAME_FORMAT when ProjectName VO rejects")
        void shouldReturnInvalidProjectNameFormatWhenVoRejects() {
            // załóżmy limit długości jak w innych VO; 51 znaków zwykle odrzuca
            CreateProjectCommand cmd = new CreateProjectCommand("x".repeat(51));
            CreateProjectResult result = service.handle(cmd);
            assertEquals(CreateProjectResult.Error.INVALID_PROJECT_NAME_FORMAT, result);
            assertTrue(store.findAll().isEmpty(), "Should not save on VO error");
        }
    }

    @Nested
    @DisplayName("Happy path / Persistence")
    class HappyPath {

        @Test
        @DisplayName("should persist project and return same id")
        void shouldPersistProjectAndReturnSameId() {
            CreateProjectCommand cmd = new CreateProjectCommand("Project A");

            CreateProjectResult result = service.handle(cmd);

            assertInstanceOf(CreateProjectResult.Ok.class, result,
                    String.format("Expected Ok but got: %s", result));
            CreateProjectResult.Ok ok = (CreateProjectResult.Ok) result;
            assertEquals(FIXED_ID, ok.projectId(), "Returned id must match GeneratorId");

            // zapisany rekord istnieje pod TYM id
            assertEquals(1, store.findAll().size(), "Exactly one project should be saved");
            Project saved = store.findById(FIXED_ID).orElseThrow(() -> new AssertionError("Project not saved"));
            assertEquals(ProjectName.of("Project A"), saved.getName(), "Saved name should match");
            assertTrue(saved.getTaskList().isEmpty(), "New project should start with empty task list");
        }

        @Test
        @DisplayName("should not persist when VO fails (no side effects)")
        void shouldNotPersistWhenVoFails() {
            CreateProjectCommand cmd = new CreateProjectCommand("x".repeat(51));
            CreateProjectResult result = service.handle(cmd);

            assertEquals(CreateProjectResult.Error.INVALID_PROJECT_NAME_FORMAT, result);
            assertTrue(store.findAll().isEmpty(), "Store must remain empty on error");
        }

        @Test
        @DisplayName("should use id from GeneratorId (deterministic)")
        void shouldUseIdFromGenerator() {
            CreateProjectCommand cmd = new CreateProjectCommand("Project B");

            CreateProjectResult result = service.handle(cmd);
            assertInstanceOf(CreateProjectResult.Ok.class, result);
            CreateProjectResult.Ok ok = (CreateProjectResult.Ok) result;
            assertEquals(FIXED_ID, ok.projectId(), "Service must return exactly the id provided by GeneratorId");

            assertTrue(store.findById(FIXED_ID).isPresent(), "Saved project should be under generated id");
        }
    }
}
