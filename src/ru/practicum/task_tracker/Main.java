package ru.practicum.task_tracker;
import ru.practicum.task_tracker.manager.HistoryManager;
import ru.practicum.task_tracker.manager.Managers;
import ru.practicum.task_tracker.manager.TaskTracker;
import ru.practicum.task_tracker.task.*;

public class Main {
    public static void main(String[] args) {
        TaskTracker taskTracker = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task("Прочитать книгу", "Джордж Оруэлл, 1984", Status.NEW);
        long taskId1 = taskTracker.createTask(task1);

        Task task2 = new Task("Погулять с собакой", "Пойти прогуляться по парку с собакой в 6 вечера", Status.NEW);
        long taskId2 = taskTracker.createTask(task2);

        Epic epic1 = new Epic("Убраться дома", "Быстрая уборка");
        long epic1Id = taskTracker.createEpic(epic1);

        Subtask subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.NEW, epic1Id);
        long subtaskId1 = taskTracker.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро, добавить средство и " +
                "сделать влажную уборку в каждой команте", Status.NEW, epic1Id);
        long subtaskId2 = taskTracker.addNewSubtask(subtask2);

        Epic epic2 = new Epic("Сходить в магазин", "Купить продукты для омлета");
        long epic2Id = taskTracker.createEpic(epic2);

        Subtask subtask3 = new Subtask("Купить молоко", "1 литр молока 3,2%", Status.NEW, epic2Id);
        long subtaskId3 = taskTracker.addNewSubtask(subtask3);

        //Печать всех задач
        System.out.println("Проверка");
        System.out.println("Печать всех tasks, epics и subtasks: ");
        taskTracker.print();
        System.out.println("");
        //Провека изменения и сохранения статуса
        System.out.println("Проверка обновления статуса: ");
        taskTracker.updateSubtaskStatus(subtask1);
        taskTracker.updateSubtaskStatus(subtask1);
        taskTracker.updateSubtaskStatus(subtask2);
        taskTracker.updateSubtaskStatus(subtask2);
        taskTracker.updateSubtaskStatus(subtask3);
        taskTracker.updateTaskStatus(task1);
        taskTracker.print();
        /*удаления одной таски и эпика
        System.out.println("Удвление task и epic: ");
        taskTracker.deleteTask(taskId1);
        taskTracker.deleteEpic(epic2Id);
        taskTracker.print();
        обновление таски
        Task task3 = new Task("Отнести книгу в библиотеку", "-", "New");
        taskTracker.updateTaskInfo(task3, taskId1);
        taskTracker.print();
        //обновление эпика(замена описания 1 эпика) и сабтаски в этом эпике
        Epic epic3 = new Epic("Подготовиться к приходу гостей", "Хорошая уборка");
        taskTracker.updateEpicInfo(epic3, epic1Id);
        Subtask subtask4 = new Subtask("Протереть везде полы и пыль", "Со средством", "New", epic2Id);
        taskTracker.updateSubtaskInfo(subtask4, subtaskId2);
        taskTracker.print();*/
        System.out.println("История просмоторов задач: ");
        taskTracker.getTask(taskId2);
        taskTracker.getTask(taskId2);
        taskTracker.getEpic(epic1Id);
        taskTracker.getEpic(epic1Id);
        taskTracker.getSubtask(subtaskId3);
        taskTracker.getSubtask(subtaskId3);
        taskTracker.getSubtask(subtaskId3);
        historyManager.remove(taskId2);
        taskTracker.printHistory();

    }
}