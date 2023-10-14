package ru.practicum.task_tracker.manager;

import com.google.gson.Gson;
import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Status;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

import java.io.File;

import java.time.LocalDateTime;


public class FileBackedTaskManagerTest {
    public static void main(String[] args){
        File file = new File("text.txt");
        FileBackedTasksManager saveLine = new FileBackedTasksManager(file);
        //ЗАПИСЬ В ФАЙЛ

        Task task3 = new Task("Почитать книгу", "Джордж Оруэлл 1984", Status.NEW,
                LocalDateTime.of(2023, 9, 29, 18, 40), 120);
        long taskId3 = saveLine.createTask(task3);
        System.out.println(new Gson().toJson(task3));
        Epic epic1 = new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        long epic1Id = saveLine.createEpic(epic1);
        Subtask subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        Long subtaskId1 = saveLine.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро добавить средство и " +
                "сделать влажную уборку в каждой команте", Status.DONE, epic1Id,
                null, 15);
        Long subtaskId2 = saveLine.addNewSubtask(subtask2);


        Epic epic2 = new Epic("Сходить в магазин", "Купить продукты для омлета", Status.NEW,
                LocalDateTime.of(2023, 9, 28, 9, 00),
                0);
        long epic2Id = saveLine.createEpic(epic2);
        Subtask subtask3 = new Subtask("Купить молоко", "1 литр молока", Status.NEW, epic2Id,
                LocalDateTime.of(2023, 9, 28, 9, 21),
                20);
        Long subtaskId3 = saveLine.addNewSubtask(subtask3);

        Subtask subtask4 = new Subtask("Купить яйца", "10 штук", Status.NEW, epic2Id,
                LocalDateTime.of(2023, 9, 28, 9, 00),
                20);
        Long subtaskId4 = saveLine.addNewSubtask(subtask4);

        saveLine.getTask(taskId3);
        saveLine.getSubtask(subtaskId1);
        saveLine.getSubtask(subtaskId2);
        System.out.println(new Gson().toJson(epic1));
        System.out.println(new Gson().toJson(subtask1));
        System.out.println(new Gson().toJson(subtask2));


        System.out.println("Проверка, что в новом FileBackedTasksManager все восстановилось:");
        FileBackedTasksManager newFileBackedTasksManager = new FileBackedTasksManager(file);
        newFileBackedTasksManager.loadFromFile();//загружаем из файла и закидываем в память
        newFileBackedTasksManager.print(); //печатаем из памяти
        System.out.println("\nИстория просмотров: \n");
        newFileBackedTasksManager.removeHistoryTask(taskId3);
        newFileBackedTasksManager.printHistory(); //печатаем историю из памяти

        System.out.println("\nсортировка задач про времени\n");
        for(Task task : saveLine.getPrioritizedTasks().values()){
            System.out.println(task.getName());
        }
        System.out.println(new Gson().toJson(saveLine.getHistoryManager().getHistory()));
    }
}
