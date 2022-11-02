import History.HistoryManager;
import History.InMemoryHistoryManager;
import Manager.*;
import Models.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        TaskManager manager = addTask();
        updateTask(manager);
        fillHistory(manager);
        saveInFile(manager);
        checkLoadFromFile();
    }

    public static TaskManager addTask() {
        TaskManager manager = Managers.getDefault();
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
        System.out.println("Менеджер задач создан, задачи успешно добавлены");
        return manager;
    }

    public static void updateTask(TaskManager manager) {
        Task newTask = manager.getTaskById(1);
        newTask.setStatus(Status.DONE);
        manager.updateTask(newTask, newTask.getId());
        Subtask newSubtask3 = manager.getSubtaskById(7);
        newSubtask3.setStatus(Status.DONE);
        manager.updateSubtask(newSubtask3, newSubtask3.getId());
        System.out.println("Задачи успешно обновлены");
    }

    public static void fillHistory(TaskManager manager) {
        Task task = manager.getTaskById(2);
        Subtask subtask = manager.getSubtaskById(5);
        Epic epic = manager.getEpicById(6);
        task = manager.getTaskById(1);
        subtask = manager.getSubtaskById(4);
        epic = manager.getEpicById(3);
        task = manager.getTaskById(2);
        subtask = manager.getSubtaskById(7);
        epic = manager.getEpicById(6);
        epic = manager.getEpicById(3);
        task = manager.getTaskById(1);
        System.out.println("История просмотров задач: " + manager.getHistory());
    }

    public static void saveInFile(TaskManager manager) {
        Path taskManager = Paths.get("taskManager.csv");
        FileBackedTasksManager fileManager = new FileBackedTasksManager(taskManager);
        fileManager.save();
        System.out.println("Mенеджер задач успешно записан в файл taskManager.csv");
    }

    public static void checkLoadFromFile() {
        TaskManager managerFromFile = FileBackedTasksManager.loadFromFile(new File("taskManager.csv"));
        System.out.println("Менеджер задач успешно загружен из файла taskManager.csv");
        System.out.println("История просмотров задач из загруженного файла: " + managerFromFile.getHistory());
    }

}
