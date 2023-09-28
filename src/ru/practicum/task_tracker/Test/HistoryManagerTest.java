package ru.practicum.task_tracker.Test;

import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.manager.HistoryManager;
import ru.practicum.task_tracker.manager.InMemoryTaskManager;
import ru.practicum.task_tracker.manager.Managers;
import ru.practicum.task_tracker.manager.TaskTracker;
import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Status;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskTracker taskTracker = new InMemoryTaskManager();

    @Test
    public void emptyTest(){
        assertEquals(null, historyManager.getHistory());
    }

    @Test
    public void duplicateTest(){
        Task task1 = new Task("Прочитать книгу", "Джордж Оруэлл, 1984", Status.NEW,
                LocalDateTime.of(2023, 9, 29, 18, 40), 120);
        long taskId1 = taskTracker.createTask(task1);

        Task task2 = new Task("Погулять с собакой", "Пойти прогуляться по парку с собакой в 6 вечера", Status.NEW,
                LocalDateTime.of(2023, 9, 30, 18, 00), 120);
        long taskId2 = taskTracker.createTask(task2);

        taskTracker.getTask(taskId1);
        taskTracker.getTask(taskId1);
        taskTracker.getTask(taskId1);
        taskTracker.getTask(taskId1);

        taskTracker.getTask(taskId2);
        taskTracker.getTask(taskId2);
        taskTracker.getTask(taskId2);


        assertEquals(2, taskTracker.getHistoryManager().getHistory().size());
    }

    @Test
    public void deleteTaskTest(){
        Task task1 = new Task("Прочитать книгу", "Джордж Оруэлл, 1984", Status.NEW,
                LocalDateTime.of(2023, 9, 29, 18, 40), 120);
        long taskId1 = taskTracker.createTask(task1);

        Task task2 = new Task("Погулять с собакой", "Пойти прогуляться по парку с собакой в 6 вечера", Status.NEW,
                LocalDateTime.of(2023, 9, 30, 18, 00), 120);
        long taskId2 = taskTracker.createTask(task2);


        Epic epic1 = new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        long epic1Id = taskTracker.createEpic(epic1);

        Subtask subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        Long subtaskId1 = taskTracker.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро добавить средство и " +
                "сделать влажную уборку в каждой команте", Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 21), 15);
        Long subtaskId2 = taskTracker.addNewSubtask(subtask2);

        taskTracker.getTask(taskId1);
        taskTracker.getTask(taskId2);
        taskTracker.getEpic(epic1Id);
        taskTracker.getSubtask(subtaskId1);
        taskTracker.getSubtask(subtaskId2);

        taskTracker.getHistoryManager().remove(taskId1);

        //из начала
        assertEquals(4, taskTracker.getHistoryManager().getHistory().size());

        taskTracker.getHistoryManager().remove(epic1Id);
        //с середины
        assertEquals(3, taskTracker.getHistoryManager().getHistory().size());

        taskTracker.getHistoryManager().remove(subtaskId1);
        //с конца
        assertEquals(2, taskTracker.getHistoryManager().getHistory().size());
    }

}
