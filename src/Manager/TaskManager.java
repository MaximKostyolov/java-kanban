package Manager;

import java.util.ArrayList;
import java.util.List;

import Models.*;

public interface TaskManager {

    ArrayList<Task> getTaskList();

    ArrayList<Subtask> getSubtaskList();

    ArrayList<Epic> getEpicList();

    void removeTaskList();

    void removeSubtaskList();

    void removeEpicList();

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    void createTask(Task task);

    void createSubtask(Subtask subtask, int epicId);

    void createEpic(Epic epic);

    void updateTask(Task newTask, int id);

    void updateSubtask(Subtask newSubtask, int id);

    void updateEpic(Epic newEpic, int id);

    void removeTaskById(int id);

    void removeSubtaskById(int id);

    void removeEpicById(int id);

    ArrayList<Subtask> getSubtaskByEpic(int epicId);

    List<Task> getHistory();
}
