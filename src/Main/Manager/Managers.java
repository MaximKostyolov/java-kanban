package Main.Manager;

import Main.History.HistoryManager;
import Main.History.InMemoryHistoryManager;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HTTPTaskManager getHTTPTaskManager() throws IOException {
        return new HTTPTaskManager(URI.create("http://localhost:8078"));
    }

    public static FileBackedTasksManager getFileBackedTasksManager() {
        return new FileBackedTasksManager(Paths.get("resourses/", "FileBackedTaskManager"));
    }


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
