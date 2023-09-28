package ru.practicum.task_tracker.Test;

import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.manager.FileBackedTasksManager;
import ru.practicum.task_tracker.manager.HistoryManager;
import ru.practicum.task_tracker.manager.Managers;
import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Status;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalDateTime;

public class FileBackedTasksManagerTest extends TaskManagerTest{
    File file = new File("text.txt");
    FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

    @Test
    public void loadFromFileTest(){
        fileBackedTasksManager.loadFromFile();
        //проверим размеры мап после загрузки из файла, если размер мап = количеству тасок в файле, то все ок
        //метод save() тоже проверяется
        assertEquals(1, fileBackedTasksManager.getTasks().size());
        assertEquals(2, fileBackedTasksManager.getEpics().size());
        assertEquals(4, fileBackedTasksManager.getSubtasks().size());
    }


    @Test
    public void emptyTasksEpicSubtasksTest(){
        assertEquals(null, fileBackedTasksManager.getTasks());

        assertEquals(null, fileBackedTasksManager.getEpics());

        assertEquals(null, fileBackedTasksManager.getSubtasks());
    }

    @Test
    public void epicWithoutSubtask(){
        Epic epic = new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        Long epic1Id = taskTracker.createEpic(epic);
        assertEquals(null, epic.getSubtaskIds());
    }

    @Test
    public void emptyHistoryTask(){
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertEquals(null, historyManager.getHistory());
    }

    @Test
    public void removeHistoryTaskTest(){
        FileBackedTasksManager saveLine = new FileBackedTasksManager(file);
        Task task3 = new Task("Почитать книгу", "Джордж Оруэлл 1984", Status.NEW,
                LocalDateTime.of(2023, 9, 29, 18, 40), 120);
        long taskId3 = saveLine.createTask(task3);

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

        saveLine.removeHistoryTask(taskId3);

        assertEquals(1, saveLine.getHistoryManager().getHistory().size());
    }

}
