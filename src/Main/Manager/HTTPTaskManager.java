package Main.Manager;

import Main.KVClient.KVTaskClient;
import Main.Models.Epic;
import Main.Models.Subtask;
import Main.Models.Task;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {

    private static KVTaskClient сlient;
    private URI uri;

    public HTTPTaskManager(URI uri) throws IOException {
        this.uri = uri;
        this.сlient = new KVTaskClient(uri);
    }

    public static HTTPTaskManager loadFromServer(String key, URI uri) {
        try {
            HTTPTaskManager manager = new HTTPTaskManager(uri);
            String managerInString = сlient.load(key);
            String[] lines = managerInString.split("/");
            List<Task> tasks = new ArrayList<>();
            for (int i = 1; i < (lines.length - 2); i++) {
                tasks.add(manager.fromString(lines[i]));
            }
            if (lines.length > 1) {
                List<Integer> historyIds = historyFromString(lines[lines.length- 1]);
                while (!historyIds.isEmpty()) {
                    Integer historyId = historyIds.get(0);
                    if (manager.getTaskById(historyId) != null) {
                        Task historyTask = manager.getTaskById(historyId);
                    } else if (manager.getSubtaskById(historyId) != null) {
                        Subtask historySubtask = manager.getSubtaskById(historyId);
                    } else if (manager.getEpicById(historyId) != null) {
                        Epic historyEpic = manager.getEpicById(historyId);
                    }
                    historyIds.remove(0);
                }
            }
            return manager;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void save() {
        String managerToString = "";
        String history = historyToString(InMemoryTaskManager.getHistoryManager());
        for (int id = 1; id <= InMemoryTaskManager.getIdentificator(); id++) {
            if (taskList.containsKey(id)) {
                managerToString = managerToString + toString(taskList.get(id)) + "\n";
            }
            if (subtaskList.containsKey(id)) {
                managerToString = managerToString + toString(subtaskList.get(id)) + "\n";
            }
            if (epicList.containsKey(id)) {
                managerToString = managerToString + toString(epicList.get(id)) + "\n";
            }
        }
        managerToString = managerToString + "\n";
        managerToString = managerToString + history;
        сlient.put("KEY", managerToString);
    }

    public static KVTaskClient getClient() {
        return сlient;
    }
}
