import java.util.ArrayList;
import java.util.HashMap;

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
           for (Integer id : epic.subtaskId) {
               removeSubTaskById(id);
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
        subtask.epicId = epicId;
        Epic epic = epicList.get(epicId);
        epic.subtaskId.add(identificator);
        subtaskList.put(identificator, subtask);
    }

    public void createEpic(Epic epic) {
        identificator = identificator + 1;
        epic.setId(identificator);
        if (epic.subtaskId.isEmpty()) {
            epic.status = Status.NEW;
        } else {
            boolean isNew = true;
            boolean isDone = true;
            for (int id : epic.subtaskId) {
                Subtask subtask = getSubtaskById(id);
                if (!subtask.status.equals(Status.DONE)) {
                    isDone = false;
                } else if (!subtask.status.equals(Status.NEW)) {
                    isNew = false;
                }
            }
            if (isDone) {
                epic.status = Status.DONE;
            } else if (isNew) {
                epic.status = Status.NEW;
            } else {
                epic.status = Status.IN_PROGRESS;
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
                Epic epic = getEpicById(subtask1.epicId);
                boolean isNew = true;
                boolean isDone = true;
                for (int subtaskId : epic.subtaskId) {
                    Subtask subtask2 = getSubtaskById(subtaskId);
                    if (!subtask2.status.equals(Status.DONE)) {
                        isDone = false;
                    } else if (!subtask2.status.equals(Status.NEW)) {
                        isNew = false;
                    }
                }
                if (isDone) {
                    epic.status = Status.DONE;
                } else if (isNew) {
                    epic.status = Status.NEW;
                } else {
                    epic.status = Status.IN_PROGRESS;
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
                if (epic.status.equals(Status.DONE)) {
                    for (int subtaskId : epic.subtaskId) {
                        Subtask subtask = getSubtaskById(subtaskId);
                        subtask.status = Status.DONE;
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

    public void removeSubTaskById(int id) {
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
                for (Integer subtaskId : epic.subtaskId) {
                    removeSubTaskById(subtaskId);
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
        for (Integer subtaskId : epic.subtaskId) {
            Subtask subtask = getSubtaskById(subtaskId);
            subtasksByEpic.add(subtask);
        }
        return subtasksByEpic;
    }

}
