package History;

import Models.*;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (history.size() < 10) {
            history.add(task);
        } else {
            history.remove(0);
            history.add(task);
        }
    }

    public void add(Subtask subtask) {
        if (history.size() < 10) {
            history.add(subtask);
        } else {
            history.remove(0);
            history.add(subtask);
        }
    }

    public void add(Epic epic) {
        if (history.size() < 10) {
            history.add(epic);
        } else {
            history.remove(0);
            history.add(epic);
        }
    }
    @Override
    public List<Task> getHistory() {
        return history;
    }
}
