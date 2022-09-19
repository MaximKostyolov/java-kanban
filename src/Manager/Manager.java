package Manager;

import java.util.ArrayList;
import java.util.HashMap;
import Models.*;

public class Manager {

    private static int identificator = 0;
    private HashMap<Integer, Task>  taskList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();

    public ArrayList<Task> getTaskList() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : taskList.values()) {
            tasks.add(task);
        }
        return tasks;
    }

    public ArrayList<Subtask> getSubtaskList() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Subtask subtask : subtaskList.values()) {
            subtasks.add(subtask);
        }
        return subtasks;
    }

    public ArrayList<Epic> getEpicList() {
        ArrayList<Epic> epics = new ArrayList<>();
        for (Epic epic : epicList.values()) {
            epics.add(epic);
        }
        return epics;
    }

    public void removeTaskList() {
        for (Integer key : taskList.keySet()) {
            taskList.remove(key);
        }
    }

    public void removeSubtaskList() {
        for (Integer key : subtaskList.keySet()) {
            subtaskList.remove(key);
        }
    }

    public void removeEpicList() {
        for (Integer key : epicList.keySet()) {
           Epic epic = epicList.get(key);
           for (Integer id : epic.getSubtaskId()) {
               removeSubtaskById(id);
           }
            epicList.remove(key);
        }
    }

    public Task getTaskById(int id) {
        Task task = null;
        boolean isValid = false;
        for (Integer key : taskList.keySet()) {
            if (key.equals(id)) {
                task = taskList.get(id);
                isValid = true;
            }
        }
        if (!isValid) {
            System.out.println("Введен неверный идентификатор!");
        }
        return task;
    }

    public Subtask getSubtaskById(int id) {
        Subtask subtask = null;
        boolean isValid = false;
        for (Integer key : subtaskList.keySet()) {
            if (key.equals(id)) {
                subtask = subtaskList.get(id);
                isValid = true;
            }
        }
        if (!isValid) {
            System.out.println("Введен неверный идентификатор!");
        }
        return subtask;
    }

    public Epic getEpicById(int id) {
        Epic epic = null;
        boolean isValid = false;
        for (Integer key : epicList.keySet()) {
            if (key.equals(id)) {
                epic = epicList.get(id);
                isValid = true;
            }
        }
        if (!isValid) {
            System.out.println("Введен неверный идентификатор!");
        }
        return epic;
    }

    public void createTask(Task task) {
        identificator = identificator + 1;
        task.setId(identificator);
        taskList.put(identificator, task);
    }

    public void createSubtask(Subtask subtask, int epicId) {
        identificator = identificator + 1;
        subtask.setId(identificator);
        subtask.setEpicId(epicId);
        Epic epic = epicList.get(epicId);
        ArrayList<Integer> subtaskId = epic.getSubtaskId();
        subtaskId.add(identificator);
        epic.setSubtaskId(subtaskId);
        subtaskList.put(identificator, subtask);
    }

    public void createEpic(Epic epic) {
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
    }

    public void updateTask(Task newTask, int id) {
        boolean isValid = false;
        for (Integer key : taskList.keySet()) {
            if (key.equals(id)) {
                taskList.put(id, newTask);
                isValid = true;
            }
        }
        if (!isValid) {
            System.out.println("Введен некоректный id");
        }
    }

    public void updateSubtask(Subtask newSubtask, int id) {
        boolean isValid = false;
        for (Integer key : subtaskList.keySet()) {
            if (key.equals(id)) {
                isValid = true;
                subtaskList.put(id, newSubtask);
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

    public void updateEpic(Epic newEpic, int id) {
        boolean isValid = false;
        for (Integer key : epicList.keySet()) {
            if (key.equals(id)) {
                isValid = true;
                epicList.put(id, newEpic);
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

    public void removeTaskById(int id) {
        boolean isValid = false;
        for (int key : taskList.keySet()) {
            if (key == id) {
                taskList.remove(id);
                isValid = true;
            }
        }
        if (!isValid) {
            System.out.println("Введен неверный id");
        }
    }

    public void removeSubtaskById(int id) {
        boolean isValid = false;
        for (int key : subtaskList.keySet()) {
            if (key == id) {
                subtaskList.remove(id);
                isValid = true;
            }
        }
        if (!isValid) {
            System.out.println("Введен неверный id");
        }
    }

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
            }
        }
        if (!isValid) {
            System.out.println("Введен неверный id");
        }
    }

    public ArrayList<Subtask> getSubtaskByEpic(int epicId) {
        Epic epic = getEpicById(epicId);
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskId()) {
            Subtask subtask = getSubtaskById(subtaskId);
            subtasksByEpic.add(subtask);
        }
        return subtasksByEpic;
    }

}
