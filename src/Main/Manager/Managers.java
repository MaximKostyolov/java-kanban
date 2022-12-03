package Main.Manager;

import Main.History.HistoryManager;
import Main.History.InMemoryHistoryManager;
import java.io.IOException;
import java.net.URI;

public class Managers {

    public static TaskManager getDefault() {
        TaskManager manager = null;
        try {
            manager = new HTTPTaskManager(URI.create("http://localhost:8078"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return manager;
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}