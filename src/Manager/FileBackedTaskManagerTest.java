package Manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    void setUp() {
        Path dir = Paths.get("user.home", "taskManagerTest.csv");
        this.manager = new FileBackedTasksManager(dir);
    }

    @AfterEach
    void deleteManager() {
            InMemoryTaskManager.setIdentificator(0);
            manager.removeEpicList();
            manager.removeTaskList();
    }

    //@Test

}
