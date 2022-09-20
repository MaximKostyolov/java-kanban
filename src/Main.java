import Manager.Manager;
import Models.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        checkWork();
    }

    public static void checkWork() {
        Manager manager = new Manager();
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

        System.out.println(manager.getTaskList());
        System.out.println(manager.getSubtaskList());
        System.out.println(manager.getEpicList());

        Task newTask = manager.getTaskById(task1.getId());
        newTask.setStatus(Status.DONE);
        manager.updateTask(newTask, newTask.getId());
        Subtask newSubtask3 = manager.getSubtaskById(subtask3.getId());
        newSubtask3.setStatus(Status.DONE);
        manager.updateSubtask(newSubtask3, subtask3.getId());


        System.out.println(manager.getTaskList());
        System.out.println(manager.getSubtaskList());
        System.out.println(manager.getEpicList());
    }

}
