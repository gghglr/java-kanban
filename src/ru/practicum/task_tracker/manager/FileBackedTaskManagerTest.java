package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Status;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

import java.io.File;

public class FileBackedTaskManagerTest {
    public static void main(String[] args){
        File file = new File("text.txt");
        FileBackedTasksManager saveLine = new FileBackedTasksManager(file);

        //ЗАПИСЬ В ФАЙЛ
        Task task3 = new Task("Прочитать книгу", "Джордж Оруэлл 1984", Status.NEW); //создаем таску
        long taskId3 = saveLine.createTask(task3); //создаем ее в памяти и сохраняем в файл

        Epic epic1 = new Epic("Убраться дома", "Быстрая уборка", Status.NEW);
        long epic1Id = saveLine.createEpic(epic1);

        Subtask subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epic1Id);
        long subtaskId1 = saveLine.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро добавить средство и " +
                "сделать влажную уборку в каждой команте", Status.DONE, epic1Id);
        long subtaskId2 = saveLine.addNewSubtask(subtask2);


        Epic epic2 = new Epic("Сходить в магазин", "Купить продукты для омлета", Status.NEW);
        long epic2Id = saveLine.createEpic(epic2);
        Subtask subtask3 = new Subtask("Купить молоко", "1 литр молока", Status.NEW, epic2Id);
        long subtaskId3 = saveLine.addNewSubtask(subtask3);

        saveLine.getTask(taskId3);
        saveLine.getSubtask(subtaskId1);
        saveLine.getSubtask(subtaskId2);
        saveLine.save();
        saveLine.saveHistory(file);


        System.out.println("Проверка, что в новом FileBackedTasksManager все восстановилось:");
        FileBackedTasksManager newFileBackedTasksManager = new FileBackedTasksManager(file);
        newFileBackedTasksManager.loadFromFile();//загружаем из файла и закидываем в память
        newFileBackedTasksManager.print(); //печатаем из памяти
        System.out.println("\nИстория просмотров: \n");
        newFileBackedTasksManager.printHistory(); //печатаем историю из памяти
        //разобраться с исклбчениями и кинуть на праверку
    }
}
