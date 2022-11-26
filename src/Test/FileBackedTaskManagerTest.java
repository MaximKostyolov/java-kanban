package Test;

import Main.Manager.FileBackedTasksManager;
import Main.Manager.ManagerSaveException;
import Main.Manager.TaskManager;
import Main.Models.Epic;
import Main.Models.Status;
import Main.Models.Subtask;
import Main.Models.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.Files.deleteIfExists;
import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    public static final String HOME = "resourses";
    public static final String TEST_CSV = "Test/taskManagerTest.csv";
    public static final Path PATH = Paths.get(HOME, TEST_CSV);

    @BeforeEach
    void setUp() throws IOException {
        deleteIfExists(PATH);
        Path dir = Files.createFile(PATH);
        this.manager = new FileBackedTasksManager(dir);
    }

    @AfterEach
    void deleteManager() {
            manager.setIdentificator(0);
            //manager.removeEpicList();
            manager.removeTaskList();
    }

    @Test
    void save() {
        manager.createTask(new Task("Test addNewTask", "Test addNewTask description"));

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("resourses/Test/taskManagerTest.csv"));
            List<String> lines = new ArrayList<>();
            while (br.ready()) {
                String line = br.readLine();
                lines.add(line);
            }

            assertNotNull(lines, "Файл пуст.");
            assertEquals(4, lines.size(), "Неверная запись в файл.");

        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Файл не найден");
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать с файла");
        } catch (ManagerSaveException e) {
            System.out.println(e.getDetailMessage());
        }
    }

    @Test
    void loadFromFile() {
        Task task = new Task(1,"Test addNewTask","Test addNewTask description", Status.NEW);
        Epic epic = new Epic(2, "Test epic", "Test epic description", Status.NEW, new ArrayList<Integer>(Arrays.asList(3)));
        Subtask subtask = new Subtask(3, "Test addNewSubtask", "Test addNewSubtask description", Status.NEW, 2);

        TaskManager managerFromFile = FileBackedTasksManager.loadFromFile(Paths.get("resourses/LoadTest", "taskManagerLoadTest.csv").toFile());

        List<Task> history = managerFromFile.getHistory();

        assertNotNull(history, "История не восстанавливается из файла.");
        assertEquals(3, history.size(), "История неверно восстанавливается из файла");
        assertEquals(task.getName(), history.get(0).getName(), "История неверно восстанавливается из файла");
        assertEquals(subtask.getId(), history.get(1).getId(), "История неверно восстанавливается из файла");
        assertEquals(epic.getDescription(), history.get(2).getDescription(), "История неверно восстанавливается из файла");

        Epic epicFromFile = managerFromFile.getEpicById(2);
        Subtask subtaskFromFile = managerFromFile.getSubtaskById(3);
        Task taskFromFile = managerFromFile.getTaskById(1);

        assertEquals(task, taskFromFile, "Задачи не совпадают");
        assertEquals(subtask, subtaskFromFile, "Задачи не совпадают");
        assertEquals(epic, epicFromFile, "Задачи не совпадают");
    }

}
