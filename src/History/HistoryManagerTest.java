package History;

import Models.*;
import Manager.*;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private TaskManager manager;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
    }

    @AfterEach
    void deleteManager() {
        InMemoryTaskManager.setIdentificator(0);
        manager.removeEpicList();
        manager.removeTaskList();
    }

    @Test
    void add() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        manager.createTask(task);

        List<Task> history = manager.getHistory();

        assertNotNull(history, "Задачи не записываются в историю.");
        assertEquals(1, history.size(), "Неверное количество задач.");
        assertEquals(task, history.get(0), "Задачи не корректно записываются в историю.");
    }

    @Test
    void remove() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        manager.createTask(task);
        Epic epic = new Epic("Test epic", "Test epic description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        manager.createSubtask(subtask, epic.getId());

        manager.removeSubtaskById(subtask.getId());

        List<Task> history = manager.getHistory();

        assertNotNull(history, "Задачи не записываются в историю.");
        assertEquals(2, history.size(), "Неверное количество задач.");
        assertEquals(epic, history.get(0), "Задачи не корректно записываются в историю.");
    }

    @Test
    void getHistory() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        manager.createTask(task);
        Epic epic = new Epic("Test epic", "Test epic description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        manager.createSubtask(subtask, epic.getId());

        Task newTask = manager.getTaskById(task.getId());

        List<Task> history = manager.getHistory();

        assertNotNull(history, "Задачи не записываются в историю.");
        assertEquals(3, history.size(), "Неверное количество задач.");
        assertEquals(task, history.get(0), "Задачи не корректно записываются в историю.");
        assertEquals(subtask, history.get(1), "Задачи не корректно записываются в историю.");
        assertEquals(epic, history.get(2), "Задачи не корректно записываются в историю.");

    }
}