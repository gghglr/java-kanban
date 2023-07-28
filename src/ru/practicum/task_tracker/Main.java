package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.TaskTracker;
import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

public class Main {
    public static void main(String[] args) {
        TaskTracker taskTracker = new TaskTracker();

        Task task1 = new Task("Прочитать книгу", "Джордж Оруэлл, 1984", "New");
        long taskId1 = taskTracker.createTask(task1);

        Task task2 = new Task("Погулять с собакой", "Пойти прогуляться по парку с собакой в 6 вечера", "New");
        long taskId2 = taskTracker.createTask(task2);

        Epic epic1 = new Epic("Убраться дома", "Быстрая уборка");
        long epic1Id = taskTracker.createEpic(epic1);

        Subtask subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                "New", epic1Id);
        long subtaskId1 = taskTracker.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро, добавить средство и " +
                "сделать влажную уборку в каждой команте", "New", epic1Id);
        long subtaskId2 = taskTracker.addNewSubtask(subtask2);

        Epic epic2 = new Epic("Сходить в магазин", "Купить продукты для омлета");
        long epic2Id = taskTracker.createEpic(epic2);

        Subtask subtask3 = new Subtask("Купить молоко", "1 литр молока 3,2%", "New", epic2Id);
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
        //удаления одной таски и эпика
        System.out.println("Удвление task и epic: ");
        taskTracker.deleteTask(taskId1);
        taskTracker.deleteEpic(epic2Id);
        taskTracker.print();
        /*//обновление таски
        Task task3 = new Task("Отнести книгу в библиотеку", "-", "New");
        taskTracker.updateTaskInfo(task3, taskId1);
        taskTracker.print();
        //обновление эпика(замена описания 1 эпика) и сабтаски в этом эпике
        Epic epic3 = new Epic("Подготовиться к приходу гостей", "Хорошая уборка");
        taskTracker.updateEpicInfo(epic3, epic1Id);
        Subtask subtask4 = new Subtask("Протереть везде полы и пыль", "Со средством", "New", epic2Id);
        taskTracker.updateSubtaskInfo(subtask4, subtaskId2);
        taskTracker.print();*/
    }
}