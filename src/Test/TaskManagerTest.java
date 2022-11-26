package Test;

import Main.Manager.TaskManager;
import Main.Models.Epic;
import Main.Models.Subtask;
import Main.Models.Task;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import static Main.Models.Status.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    @Test
    void createTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        manager.createTask(task);
        final int taskId = task.getId();

        final Task savedTask = manager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = manager.getTaskList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void createSubtask() {
        Epic epic = new Epic("Test epic", "Test epic description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        manager.createSubtask(subtask, epic.getId());
        final int subtaskId = subtask.getId();

        final Subtask savedSubtask = manager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = manager.getSubtaskList();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void createEpic() {
        Epic epic = null;
        manager.createEpic(epic);

        final Epic savedEpic = manager.getEpicById(1);

        assertNull(savedEpic);

        final List<Epic> epics = manager.getEpicList();

        assertEquals(0, epics.size(), "Неверное количество задач.");
        assertTrue(epics.isEmpty());
    }

    @Test
    void removeTaskList() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        manager.createTask(task);
        final int taskId = task.getId();

        manager.removeTaskList();

        final List<Task> tasks = manager.getTaskList();

        assertEquals(0, tasks.size(), "Неверное количество задач.");
        assertTrue(tasks.isEmpty());
        final IndexOutOfBoundsException exception = assertThrows(

                // класс ошибки
                IndexOutOfBoundsException.class,

                // создание и переопределение экземпляра класса Executable
                new Executable() {
                    @Override
                    public void execute() {
                        tasks.get(1);
                    }
                });

        // можно проверить, находится ли в exception ожидаемый текст
        assertEquals("Index 1 out of bounds for length 0", exception.getMessage());
    }

    @Test
    void removeSubtaskList() {
        Epic epic = new Epic("Test epic", "Test epic description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        manager.createSubtask(subtask, epic.getId());
        manager.removeSubtaskList();

        final List<Subtask> subtasks = manager.getSubtaskList();

        assertEquals(0, subtasks.size(), "Неверное количество задач.");
        assertTrue(subtasks.isEmpty());
        final IndexOutOfBoundsException exception = assertThrows(

                // класс ошибки
                IndexOutOfBoundsException.class,

                // создание и переопределение экземпляра класса Executable
                new Executable() {
                    @Override
                    public void execute() {
                        subtasks.get(1);
                    }
                });

        // можно проверить, находится ли в exception ожидаемый текст
        assertEquals("Index 1 out of bounds for length 0", exception.getMessage());
    }

    @Test
    void removeEpicList() {
        Epic epic = new Epic("Test epic", "Test epic description");
        manager.createEpic(epic);
        manager.removeEpicList();

        final List<Epic> epics = manager.getEpicList();

        assertEquals(0, epics.size(), "Неверное количество задач.");
        assertTrue(epics.isEmpty());
        final IndexOutOfBoundsException exception = assertThrows(

                // класс ошибки
                IndexOutOfBoundsException.class,

                // создание и переопределение экземпляра класса Executable
                new Executable() {
                    @Override
                    public void execute() {
                        epics.get(1);
                    }
                });

        // можно проверить, находится ли в exception ожидаемый текст
        assertEquals("Index 1 out of bounds for length 0", exception.getMessage());

    }

    @Test
    void getTaskById() {
        final int taskId = 0;

        final Task savedTask = manager.getTaskById(taskId);

        assertNull(savedTask);
    }

    @Test
    void getSubtaskById() {
        Epic epic = new Epic("Test epic", "Test epic description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        manager.createSubtask(subtask, epic.getId());
        final int subtaskId = subtask.getId();

        final Subtask savedSubtask = manager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
    }

    @Test
    void getEpicById() {
        Epic epic = new Epic("Test epic", "Test epic description");
        manager.createEpic(epic);
        final int epicId = 10;
        manager.updateEpic(epic, epicId);

        final Epic savedEpic = manager.getEpicById(epicId);

        assertNull(savedEpic);
        assertNotEquals(epic, savedEpic, "Задачи совпадают.");
    }

    @Test
    void updateTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        manager.createTask(task);
        final int taskId = task.getId();
        task.setStatus(DONE);
        manager.updateTask(task, taskId);

        final Task savedTask = manager.getTaskById(taskId);


        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = manager.getTaskList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic("Test epic", "Test epic description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        manager.createSubtask(subtask, epic.getId());
        final int subtaskId = subtask.getId();
        subtask.setStatus(IN_PROGRESS);
        manager.updateSubtask(subtask, subtaskId);

        final Subtask savedSubtask = manager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = manager.getSubtaskList();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("Test epic", "Test epic description");
        manager.createEpic(epic);
        final int epicId = epic.getId();
        epic.setStartTime(LocalDateTime.of(2022, 11, 23, 12, 00));
        epic.setDuration(180);
        manager.updateEpic(epic, epicId);

        final Epic savedEpic = manager.getEpicById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = manager.getEpicList();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }


    @Test
    void removeTaskById() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        manager.createTask(task);
        Task task2 = new Task("Test addNewTask2", "Test addNewTask description2");
        manager.createTask(task2);
        final int taskId = task.getId();

        manager.removeTaskById(taskId);

        final List<Task> tasks = manager.getTaskList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task2, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void removeSubtaskById() {
        Epic epic = new Epic("Test epic", "Test epic description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        manager.createSubtask(subtask, epic.getId());
        final int subtaskId = subtask.getId();

        manager.removeSubtaskById(subtaskId);

        final List<Subtask> subtasks = manager.getSubtaskList();

        assertEquals(0, subtasks.size(), "Неверное количество задач.");
        assertTrue(subtasks.isEmpty());
    }

    @Test
    void removeEpicById() {
        manager.removeEpicById(150);

        final List<Epic> epics = manager.getEpicList();

        assertEquals(0, epics.size(), "Неверное количество задач.");
        assertTrue(epics.isEmpty());
    }

    @Test
    void getSubtaskByEpic() {
        Epic epic = new Epic("Test epic", "Test epic description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        manager.createSubtask(subtask, epic.getId());

        ArrayList<Subtask> subtasksByEpic = manager.getSubtaskByEpic(epic.getId());

        assertNotNull(subtasksByEpic, "Сабтаски не возвращаются.");
        assertEquals(1, subtasksByEpic.size(), "Неверное количество сабтасок.");
        assertEquals(2, subtasksByEpic.get(0).getId(), "Id сабтасок не совпадают.");


    }

}