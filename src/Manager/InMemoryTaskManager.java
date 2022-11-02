package Manager;

import History.HistoryManager;
import Models.Epic;
import Models.Status;
import Models.Subtask;
import Models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private static int identificator = 0;
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private static HistoryManager historyManager = Managers.getDefaultHistory();

    public static int getIdentificator() {
        return identificator;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public ArrayList<Task> getTaskList() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : taskList.values()) {
            tasks.add(task);
        }
        return tasks;
    }

    @Override
    public ArrayList<Subtask> getSubtaskList() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Subtask subtask : subtaskList.values()) {
            subtasks.add(subtask);
        }
        return subtasks;
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        ArrayList<Epic> epics = new ArrayList<>();
        for (Epic epic : epicList.values()) {
            epics.add(epic);
        }
        return epics;
    }

    @Override
    public void removeTaskList() {
        for (Integer key : taskList.keySet()) {
            removeTaskById(key);
        }
    }

    @Override
    public void removeSubtaskList() {
        for (Integer key : subtaskList.keySet()) {
            removeSubtaskById(key);
        }
    }

    @Override
    public void removeEpicList() {
        for (Integer key : epicList.keySet()) {
            Epic epic = epicList.get(key);
            for (Integer id : epic.getSubtaskId()) {
                removeSubtaskById(id);
            }
            removeEpicById(key);
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = null;
        for (Integer key : taskList.keySet()) {
            if (key.equals(id)) {
                task = taskList.get(id);
                historyManager.add(task);
            }
        }
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = null;
        for (Integer key : subtaskList.keySet()) {
            if (key.equals(id)) {
                subtask = subtaskList.get(id);
                historyManager.add(subtask);
            }
        }
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = null;
        for (Integer key : epicList.keySet()) {
            if (key.equals(id)) {
                epic = epicList.get(id);
                historyManager.add(epic);
            }
        }
        return epic;
    }

    @Override
    public void createTask(Task task) {
        if (task != null) {
            identificator = identificator + 1;
            task.setId(identificator);
            taskList.put(identificator, task);
            historyManager.add(task);
        }
    }

    @Override
    public void createSubtask(Subtask subtask, int epicId) {
        if (subtask != null) {
            identificator = identificator + 1;
            subtask.setId(identificator);
            subtask.setEpicId(epicId);
            Epic epic = epicList.get(epicId);
            ArrayList<Integer> subtaskId = epic.getSubtaskId();
            subtaskId.add(identificator);
            epic.setSubtaskId(subtaskId);
            subtaskList.put(identificator, subtask);
            historyManager.add(subtask);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        if (epic != null) {
            identificator = identificator + 1;
            epic.setId(identificator);
            if (epic.getSubtaskId().isEmpty()) {
                epic.setStatus(Status.NEW);
            } else {
                boolean isNew = true;
                boolean isDone = true;
                for (int id : epic.getSubtaskId()) {
                    Subtask subtask = getSubtaskById(id);
                    if (!subtask.getStatus().equals(Status.DONE)) {
                        isDone = false;
                    } else if (!subtask.getStatus().equals(Status.NEW)) {
                        isNew = false;
                    }
                }
                if (isDone) {
                    epic.setStatus(Status.DONE);
                } else if (isNew) {
                    epic.setStatus(Status.NEW);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            }
            epicList.put(identificator, epic);
            historyManager.add(epic);
        }
    }

    @Override
    public void updateTask(Task newTask, int id) {
        if (newTask != null) {
            boolean isValid = false;
            for (Integer key : taskList.keySet()) {
                if (key.equals(id)) {
                    taskList.put(id, newTask);
                    isValid = true;
                    historyManager.add(newTask);
                }
            }
            if (!isValid) {
                System.out.println("Введен некоректный id");
            }
        }
    }

    @Override
    public void updateSubtask(Subtask newSubtask, int id) {
        if (newSubtask != null) {
            boolean isValid = false;
            for (Integer key : subtaskList.keySet()) {
                if (key.equals(id)) {
                    isValid = true;
                    subtaskList.put(id, newSubtask);
                    historyManager.add(newSubtask);
                    Subtask subtask1 = subtaskList.get(key);
                    Epic epic = getEpicById(subtask1.getEpicId());
                    boolean isNew = true;
                    boolean isDone = true;
                    for (int subtaskId : epic.getSubtaskId()) {
                        Subtask subtask2 = getSubtaskById(subtaskId);
                        if (!subtask2.getStatus().equals(Status.DONE)) {
                            isDone = false;
                        } else if (!subtask2.getStatus().equals(Status.NEW)) {
                            isNew = false;
                        }
                    }
                    if (isDone) {
                        epic.setStatus(Status.DONE);
                    } else if (isNew) {
                        epic.setStatus(Status.NEW);
                    } else {
                        epic.setStatus(Status.IN_PROGRESS);
                    }
                }
            }
            if (!isValid) {
                System.out.println("Введен некоректный id");
            }
        }
    }

    @Override
    public void updateEpic(Epic newEpic, int id) {
        if (newEpic != null) {
            boolean isValid = false;
            for (Integer key : epicList.keySet()) {
                if (key.equals(id)) {
                    isValid = true;
                    epicList.put(id, newEpic);
                    historyManager.add(newEpic);
                    Epic epic = getEpicById(id);
                    if (epic.getStatus().equals(Status.DONE)) {
                        for (int subtaskId : epic.getSubtaskId()) {
                            Subtask subtask = getSubtaskById(subtaskId);
                            subtask.setStatus(Status.DONE);
                        }
                    }
                }
            }
            if (!isValid) {
                System.out.println("Введен некоректный id");
            }
        }
    }

    @Override
    public void removeTaskById(int id) {
        boolean isValid = false;
        for (int key : taskList.keySet()) {
            if (key == id) {
                taskList.remove(id);
                isValid = true;
                historyManager.remove(id);
                return;
            }
        }
        if (!isValid) {
            System.out.println("Введен неверный id");
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        boolean isValid = false;
        for (int key : subtaskList.keySet()) {
            if (key == id) {
                subtaskList.remove(id);
                isValid = true;
                historyManager.remove(id);
                return;
            }
        }
        if (!isValid) {
            System.out.println("Введен неверный id");
        }
    }

    @Override
    public void removeEpicById(int id) {
        boolean isValid = false;
        for (int key : epicList.keySet()) {
            if (key == id) {
                Epic epic = getEpicById(id);
                for (Integer subtaskId : epic.getSubtaskId()) {
                    removeSubtaskById(subtaskId);
                }
                epicList.remove(id);
                isValid = true;
                historyManager.remove(id);
                return;
            }
        }
        if (!isValid) {
            System.out.println("Введен неверный id");
        }
    }

    @Override
    public ArrayList<Subtask> getSubtaskByEpic(int epicId) {
        Epic epic = getEpicById(epicId);
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskId()) {
            Subtask subtask = getSubtaskById(subtaskId);
            subtasksByEpic.add(subtask);
        }
        return subtasksByEpic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}