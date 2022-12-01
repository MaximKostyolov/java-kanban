package Main.Manager;

import Main.History.HistoryManager;
import Main.Models.Epic;
import Main.Models.Status;
import Main.Models.Subtask;
import Main.Models.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private static int identificator = 0;
    protected static final HashMap<Integer, Task> taskList = new HashMap<>();
    protected static final HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    protected static final HashMap<Integer, Epic> epicList = new HashMap<>();
    private static final HistoryManager historyManager = Managers.getDefaultHistory();

    public static int getIdentificator() {
        return identificator;
    }

    public void setIdentificator(int identificator) {
        this.identificator = identificator;
    }

    public static HistoryManager getHistoryManager() {
        return historyManager;
    }

    public void setStartTimeToEpic(Epic epic) {
        LocalDateTime startTime = LocalDateTime.MAX;
        for (int i : epic.getSubtaskId()) {
            Subtask subtask = getSubtaskById(i);
            if (subtask.getStartTime() != null) {
                if  (subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }
            }
        }
        if (startTime == LocalDateTime.MAX) {
            startTime = null;
        }
        epic.setStartTime(startTime);
    }

    public void setEndTimeToEpic(Epic epic) {
        LocalDateTime endTime = LocalDateTime.MIN;
        for (int i : epic.getSubtaskId()) {
            Subtask subtask = getSubtaskById(i);
            if (subtask.getEndTime() != null) {
                if (subtask.getEndTime().isAfter(endTime)) {
                    endTime = subtask.getEndTime();
                }
            }
        }
        if (endTime == LocalDateTime.MIN) {
            endTime = null;
        }
        epic.setEndTime(endTime);
    }

    public void setDurationToEpic(Epic epic) {
        Duration duration = null;
        if ((epic.getStartTime() != null) && (epic.getEndTime() != null)) {
            duration = Duration.between(epic.getStartTime(), epic.getEndTime());
        }
        long durationInMinutes = 0;
        if (duration != null) {
            durationInMinutes = duration.toMinutes();
        }
        epic.setDuration(durationInMinutes);
    }

    public void setEpicStatus(Epic epic) {
        if (epic.getSubtaskId().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            boolean isNew = true;
            boolean isDone = true;
            for (int id : epic.getSubtaskId()) {
                Subtask subtask = getSubtaskById(id);
                if (!subtask.getStatus().equals(Status.DONE)) {
                    isDone = false;
                }
                if (!subtask.getStatus().equals(Status.NEW)) {
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

    public static TreeSet<Task> getPrioritizedTasks() {
        Comparator<Task> comparator = (task1, task2) -> {
            LocalDateTime date1 = task1.getStartTime();
            LocalDateTime date2 = task2.getStartTime();
            if (date1 == null) {
                return 1;
            }
            if (date2 == null) {
                return -1;
            }
            if ((date1 == null) && (date2 == null)) {
                return task1.getId() - task2.getId();
            }
            int compareByDate = date1.compareTo(date2);
            if (compareByDate != 0) {
                return compareByDate;
            } else {
                return task1.getId() - task2.getId();
            }
        };
        List<Task> tasks = getHistoryManager().getHistory();
        TreeSet<Task> prioritizedTasks = new TreeSet<>(comparator);
        if (!tasks.isEmpty()) {
            prioritizedTasks.addAll(tasks);
        }
        return prioritizedTasks;
    }

    public Task checkTimeIntersection(Task task) {
        TreeSet<Task> sortedTask = getPrioritizedTasks();
        if (!sortedTask.isEmpty()) {
            for (Task taskFromsortedTask : sortedTask) {
                if ((task.getStartTime() != null) && (task.getEndTime() != null) && (taskFromsortedTask.getStartTime() != null) &&
                        (taskFromsortedTask != null) && (!task.equals(taskFromsortedTask))) {
                    if ((task.getStartTime().isBefore(taskFromsortedTask.getEndTime())) && (task.getEndTime().
                            isAfter(taskFromsortedTask.getStartTime()))) {
                        System.out.println("Задача " + task.getName() + " пересекается по времени выполнения с задачей " +
                                taskFromsortedTask.getName() + ":");
                        System.out.println(taskFromsortedTask.getStartTime() + " - " + taskFromsortedTask.getEndTime());
                        System.out.println("Задача " + task.getName() + " будет добавлена без времени начала и окончания");
                        task.setStartTime(null);
                        task.setEndTime(null);
                    }
                }
            }
        }
        return task;
    }

    public Subtask checkTimeIntersection(Subtask subtask) {
        TreeSet<Task> sortedTask = getPrioritizedTasks();
        if (!sortedTask.isEmpty()) {
            for (Task taskFromsortedTask : sortedTask) {
                if ((subtask.getStartTime() != null) && (subtask.getEndTime() != null) && (taskFromsortedTask.getStartTime() != null)
                        && (taskFromsortedTask != null) && (!subtask.equals(taskFromsortedTask))) {
                    if ((subtask.getStartTime().isBefore(taskFromsortedTask.getEndTime())) && (subtask.getEndTime().
                            isAfter(taskFromsortedTask.getStartTime()))) {
                        System.out.println("Подзадача " + subtask.getName() + " пересекается по времени выполнения с задачей " +
                                taskFromsortedTask.getName() + ":");
                        System.out.println(taskFromsortedTask.getStartTime() + " - " + taskFromsortedTask.getEndTime());
                        System.out.println("Подзадача " + subtask.getName() + " будет добавлена без времени начала и окончания");
                        subtask.setStartTime(null);
                        subtask.setEndTime(null);
                    }
                }
            }
        }
        return subtask;
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
        while (!taskList.isEmpty()) {
            List<Task> tasks = getTaskList();
            removeTaskById(tasks.get(0).getId());
        }
    }

    @Override
    public void removeSubtaskList() {
        while (!subtaskList.isEmpty()) {
            List<Subtask> subtasks = getSubtaskList();
            removeSubtaskById(subtasks.get(0).getId());
        }
    }

    @Override
    public void removeEpicList() {
        while (!epicList.isEmpty()) {
            List<Epic> epics = getEpicList();
            removeEpicById(epics.get(0).getId());
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = null;
        for (Integer key : taskList.keySet()) {
            if (key.equals(id)) {
                task = taskList.get(id);
                historyManager.add(task);
                //break;
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
                //break;
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
                //break;
            }
        }
        return epic;
    }

    @Override
    public void createTask(Task task) {
        if (task != null) {
            identificator = identificator + 1;
            task.setId(identificator);
            task = checkTimeIntersection(task);
            taskList.put(identificator, task);
            System.out.println("Задача " + task.getName() + " успешно добавлена");
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
            ArrayList<Integer> subtasksId = epic.getSubtaskId();
            subtasksId.add(identificator);
            epic.setSubtaskId(subtasksId);
            subtask = checkTimeIntersection(subtask);
            subtaskList.put(identificator, subtask);
            System.out.println("Подзадача " + subtask.getName() + " успешно добавлена");
            historyManager.add(subtask);
            setEpicStatus(epic);
            setStartTimeToEpic(epic);
            setEndTimeToEpic(epic);
            setDurationToEpic(epic);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        if (epic != null) {
            identificator = identificator + 1;
            epic.setId(identificator);
            setEpicStatus(epic);
            setStartTimeToEpic(epic);
            setEndTimeToEpic(epic);
            setDurationToEpic(epic);
            epicList.put(identificator, epic);
            System.out.println("Эпик " + epic.getName() + " успешно создан");
            historyManager.add(epic);
        }
    }

    @Override
    public void updateTask(Task newTask, int id) {
        if (newTask != null) {
            boolean isValid = false;
            for (Integer key : taskList.keySet()) {
                if (key.equals(id)) {
                    isValid = true;
                    newTask = checkTimeIntersection(newTask);
                    taskList.put(id, newTask);
                    System.out.println("Задача " + newTask.getName() + " успешно обновлена");
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
                    newSubtask = checkTimeIntersection(newSubtask);
                    subtaskList.put(id, newSubtask);
                    System.out.println("Подзадача " + newSubtask.getName() + " успешно обновлена");
                    historyManager.add(newSubtask);
                    Epic epic = getEpicById(newSubtask.getEpicId());
                    setEpicStatus(epic);
                    setStartTimeToEpic(epic);
                    setEndTimeToEpic(epic);
                    setDurationToEpic(epic);
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
                    System.out.println("Эпик " + newEpic.getName() + " успешно обновлен");
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
                isValid = true;
                historyManager.remove(id);
                taskList.remove(id);
                break;
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
                Epic epic = getEpicById(subtaskList.get(id).getEpicId());
                ArrayList<Integer> subtaskId = epic.getSubtaskId();
                subtaskId.remove((Integer) id);
                epic.setSubtaskId(subtaskId);
                subtaskList.remove(id);
                isValid = true;

                historyManager.remove(id);
                break;
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
                while (!epic.getSubtaskId().isEmpty()) {
                    removeSubtaskById(epic.getSubtaskId().get(0));
                }
                epicList.remove(id);
                isValid = true;
                historyManager.remove(id);
                break;
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