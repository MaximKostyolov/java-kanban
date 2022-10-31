package Manager;

import History.HistoryManager;
import Models.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.deleteIfExists;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final Path file;

    public FileBackedTasksManager(Path file) {
        this.file = file;
    }

    public static void main(String[] args) {

        //checkWritingToFile();
        checkReadingFromFile();

    }

    public static void checkReadingFromFile() {
        String HOME = System.getProperty("user.home");
        Path file = Paths.get(HOME, "taskManager.csv");
        FileBackedTasksManager manager = loadFromFile(file);
        manager.createTask(new Task("Работа", "Сделать месячный отчет", Status.IN_PROGRESS));
        System.out.println("Задачи успешно считаны с файла и добавлена новая задача");
        System.out.println(manager.getHistory());
    }

    public static void checkWritingToFile() {
        String HOME = System.getProperty("user.home");
        try {
            if (deleteIfExists(Paths.get(HOME, "taskManager.csv"))) {
                Path taskManager = Files.createFile(Paths.get(HOME, "taskManager.csv"));
                TaskManager manager = new FileBackedTasksManager(taskManager);
                Task task1 = new Task("Поход в магазин", "Покупка продуктов");
                manager.createTask(task1);
                Task task2 = new Task("Снять квартиру", "Найти и забронировать квартиру для встречи с друзьями");
                manager.createTask(task2);
                Epic epic1 = new Epic("Обучение", "Закрыть долги по учебе");
                manager.createEpic(epic1);
                Subtask subtask1 = new Subtask("Спринт 3", "Сдать финальный проект 3-го спринта");
                manager.createSubtask(subtask1, epic1.getId());
                Subtask subtask2 = new Subtask("Спринт 4", "Сдать финальный проект 4-го спринта");
                manager.createSubtask(subtask2, epic1.getId());
                Epic epic2 = new Epic("Покупка машины", "Поиск и выбор оптимального варианта");
                manager.createEpic(epic2);
                Subtask subtask3 = new Subtask("Подбор автомобиля", "Поиск вариантов на Авито");
                manager.createSubtask(subtask3, epic2.getId());
                System.out.println("Задачи успешно записаны в файл taskManager.csv");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file.toFile())) {
            fileWriter.write("id,type,name,status,description,epic\n");
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл");
        }
        try (Writer fileWriter = new FileWriter(file.toFile(), true)) {
            for (int id = 1; id <= getIdentificator(); id++) {
                if (getTaskById(id) != null) {
                    fileWriter.write(toString(getTaskById(id)) + "\n");
                }
                if (getSubtaskById(id) != null) {
                    fileWriter.write(toString(getSubtaskById(id)) + "\n");
                }
                if (getEpicById(id) != null) {
                    fileWriter.write(toString(getEpicById(id)) + "\n");
                }
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл");
        } catch (ManagerSaveException e) {
            System.out.println(e.getDetailMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(Path file) {
        FileBackedTasksManager manager = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file.toFile()))) {
            manager = new FileBackedTasksManager(file);
            List<String> lines = new ArrayList<>();
            while (br.ready()) {
                String line = br.readLine();
                lines.add(line);
            }
            List<Task> tasks = new ArrayList<>();
            for (int i = 1; i < (lines.size() - 2); i++) {
                tasks.add(manager.fromString(lines.get(i)));
            }
            List<Integer> historyId = historyFromString(lines.get(lines.size() - 1));
            for (Integer id : historyId) {
                if (manager.getTaskById(id) != null) {
                    Task historyTask = manager.getTaskById(id);
                } else if (manager.getSubtaskById(id) != null) {
                    Subtask historyTask = manager.getSubtaskById(id);
                } else if (manager.getEpicById(id) != null) {
                    Epic historyEpic = manager.getEpicById(id);
                }
            }
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Файл не найден");
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать с файла");
        } catch (ManagerSaveException e) {
            System.out.println(e.getDetailMessage());
        }
        return manager;
    }

    public String toString(Task task) {
        String taskToString = task.getId() + "," + "TASK" + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + ",";
        return taskToString;
    }

    public String toString(Subtask subtask) {
        String subtaskToString = subtask.getId() + "," + "SUBTASK" + ","  + subtask.getName() + "," + subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getEpicId();
        return subtaskToString;
    }

    public String toString(Epic epic) {
        String epicToString = epic.getId() + "," + "EPIC" + ","  + epic.getName() + "," + epic.getStatus() + "," + epic.getDescription() + ",";
        return epicToString;
    }

    public Task fromString(String value) {
        String[] lineContents = value.split(",", -1);
        int id = Integer.parseInt(lineContents[0]);
        Type type = Type.valueOf(lineContents[1]);
        String name = lineContents[2];
        Status status = Status.valueOf(lineContents[3]);
        String description = lineContents[4];
        int epicId = 0;
        if (!lineContents[5].isEmpty()) {
            epicId = Integer.parseInt(lineContents[5]);
        }
        switch (type) {
            case TASK:
                Task task = new Task(name, description, status);
                createTask(task);
                return task;
            case SUBTASK:
                Subtask subtask = new Subtask(name, description, status, epicId);
                createSubtask(subtask, epicId);
                return subtask;
            case EPIC:
                Epic epic = new Epic(name, description, status);
                createEpic(epic);
                return epic;
            default:
                return null;
        }
    }

    public static String historyToString(HistoryManager manager) {
        String historyString = "";
        List<Task> history = manager.getHistory();
        for (Task task : history) {
            if (!historyString.isEmpty()) {
                historyString = historyString + "," + String.valueOf(task.getId());
            } else {
                historyString = String.valueOf(task.getId());
            }
        }
        return historyString;
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> historyId = new ArrayList<>();
        String[] history = value.split(",");
        for (int i = history.length - 1; i <= 0; i--) {
            historyId.add(Integer.parseInt(history[i]));
        }
        return historyId;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask, int epicId) {
        super.createSubtask(subtask, epicId);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task newTask, int id) {
        super.updateTask(newTask, id);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask, int id) {
        super.updateSubtask(newSubtask, id);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic, int id) {
        super.updateEpic(newEpic, id);
        save();
    }

}
