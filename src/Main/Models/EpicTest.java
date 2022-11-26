package Main.Models;

import Main.Manager.Managers;
import Main.Manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static Main.Models.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    private static TaskManager manager;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
    }

    @Test
    void addEmptyEpic() {
        Epic epic = new Epic("Name", "Description");
        manager.createEpic(epic);
        assertEquals(NEW, epic.getStatus(), "Cтатусы не совпадают.");
    }

    @Test
    void addEpicWithNewSubtasks() {
        Epic epic = new Epic("Name", "Description");
        manager.createEpic(epic);
        Subtask subtask1 = new Subtask("Name1", "Description1");
        manager.createSubtask(subtask1, epic.getId());
        Subtask subtask2 = new Subtask("Name2", "Description2");
        manager.createSubtask(subtask2, epic.getId());
        assertEquals(NEW, epic.getStatus(), "Cтатусы не совпадают.");
    }

    @Test
    void addEpicWithDoneSubtasks() {
        Epic epic = new Epic("Name", "Description");
        manager.createEpic(epic);
        Subtask subtask1 = new Subtask("Name1", "Description1", DONE, epic.getId());
        Subtask subtask2 = new Subtask("Name2", "Description2", DONE, epic.getId());
        manager.createSubtask(subtask1, epic.getId());
        manager.createSubtask(subtask2, epic.getId());
        assertEquals(DONE, epic.getStatus(), "Cтатусы не совпадают.");
    }

    @Test
    void addEpicWithNewAndDoneSubtasks() {
        Epic epic = new Epic("Name", "Description");
        manager.createEpic(epic);
        Subtask subtask1 = new Subtask("Name1", "Description1");
        Subtask subtask2 = new Subtask("Name2", "Description2", DONE, epic.getId());
        manager.createSubtask(subtask1, epic.getId());
        manager.createSubtask(subtask2, epic.getId());
        assertEquals(IN_PROGRESS, epic.getStatus(), "Cтатусы не совпадают.");
    }

    @Test
    void addEpicWithInProgressSubtasks() {
        Epic epic = new Epic("Name", "Description");
        manager.createEpic(epic);
        Subtask subtask1 = new Subtask("Name1", "Description1", IN_PROGRESS, epic.getId());
        Subtask subtask2 = new Subtask("Name2", "Description2", IN_PROGRESS, epic.getId());
        manager.createSubtask(subtask1, epic.getId());
        manager.createSubtask(subtask2, epic.getId());
        assertEquals(IN_PROGRESS, epic.getStatus(), "Cтатусы не совпадают.");
    }
}