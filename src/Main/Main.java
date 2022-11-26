package Main;

import Main.Manager.*;
import Main.Models.*;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static java.nio.file.Files.deleteIfExists;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        TaskManager manager = addTask();
        updateTask(manager);
        fillHistory(manager);
        checkTimeInterseption(manager);
        saveInFile(manager);
        checkLoadFromFile(Managers.getDefault());
    }

    public static void checkTimeInterseption(TaskManager manager) {
        manager.createTask(new Task("Работа", "Сдать месячный отчет", Status.IN_PROGRESS, LocalDateTime.of(2022, 11, 20, 12, 0), 5000));
        System.out.println(manager.getHistory());
    }

    public static TaskManager addTask() {
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("Поход в магазин", "Покупка продуктов");
        manager.createTask(task1);
        Task task2 = new Task("Машина", "Заменить масло в двигателе");
        manager.createTask(task2);
        Epic epic1 = new Epic("Обучение", "Закрыть долги по учебе");
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Спринт 7", "Сдать финальный проект 7-го спринта");
        manager.createSubtask(subtask1, epic1.getId());
        Subtask subtask2 = new Subtask("Спринт 8", "Сдать финальный проект 8-го спринта");
        manager.createSubtask(subtask2, epic1.getId());
        Epic epic2 = new Epic("Поездка на горнолыжку", "Забронировать дом");
        manager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Подбор дома", "Поиск вариантов на Авито");
        manager.createSubtask(subtask3, epic2.getId());
        System.out.println("Менеджер задач создан, задачи успешно добавлены");
        return manager;
    }

    public static void updateTask(TaskManager manager) {
        Task newTask = manager.getTaskById(1);
        newTask.setStatus(Status.DONE);
        newTask.setStartTime(LocalDateTime.of(2022, 11, 22, 12, 0));
        newTask.setDuration(60);
        manager.updateTask(newTask, newTask.getId());
        Task newTask2 = manager.getTaskById(2);
        newTask2.setStartTime(LocalDateTime.of(2022, 11, 29, 12, 0));
        newTask2.setDuration(150);
        manager.updateTask(newTask2, newTask2.getId());
        Subtask newSubtask3 = manager.getSubtaskById(7);
        newSubtask3.setStatus(Status.DONE);
        manager.updateSubtask(newSubtask3, newSubtask3.getId());
        Subtask newSubtask4 = manager.getSubtaskById(4);
        newSubtask4.setStartTime(LocalDateTime.of(2022, 11, 23, 12, 0));
        newSubtask4.setDuration(1800);
        manager.updateSubtask(newSubtask4, newSubtask4.getId());
        Subtask newSubtask5 = manager.getSubtaskById(5);
        newSubtask5.setStartTime(LocalDateTime.of(2022, 11, 25, 12, 0));
        newSubtask5.setDuration(3600);
        manager.updateSubtask(newSubtask5, newSubtask5.getId());
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
        try {
            if (deleteIfExists(Paths.get("resourses","taskManager.csv"))) {
                System.out.println("Файл taskManager.csv будет перезаписан");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось записать менеджер задач в файл");
        } catch (ManagerSaveException e) {
            System.out.println(e.getDetailMessage());
        }
        manager = new FileBackedTasksManager(Paths.get("resourses","taskManager.csv"));
        ((FileBackedTasksManager) manager).save();
        System.out.println("Mенеджер задач успешно записан в файл taskManager.csv");
    }

    public static void checkLoadFromFile(TaskManager manager) {
        manager.removeEpicList();
        manager.removeTaskList();
        InMemoryTaskManager.setIdentificator(0);
        System.out.println("Менеджер будет загружен из файла");
        FileBackedTasksManager managerFromFile = FileBackedTasksManager.loadFromFile(Paths.get("resourses", "taskManager.csv").toFile());
        System.out.println("Менеджер задач успешно загружен из файла resourses/taskManager.csv");
        System.out.println("История просмотров задач из загруженного файла: " + managerFromFile.getHistory());
        System.out.println("Список отсортированных задач в порядке вренени cтарта");
        System.out.println(managerFromFile.getPrioritizedTasks());
    }

}


