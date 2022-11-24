package Manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        this.manager = new InMemoryTaskManager();
    }

    @AfterEach
    void deleteManager() {
        InMemoryTaskManager.setIdentificator(0);
        manager.removeEpicList();
        manager.removeTaskList();
    }

}
