package ru.practicum.task_tracker.Test;

import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.exception.ManagerSaveException;
import ru.practicum.task_tracker.manager.InMemoryTaskManager;
import ru.practicum.task_tracker.manager.TaskTracker;
import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Status;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class InMemoryTasksManager extends TaskManagerTest{
    TaskTracker taskTracker = new InMemoryTaskManager();
    @Test
    public void getEndTimeTest(){
        Epic epic =new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        Long epic1Id = taskTracker.createEpic(epic);
        Subtask subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        Long subtaskId1 = taskTracker.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро добавить средство и " +
                "сделать влажную уборку в каждой команте", Status.IN_PROGRESS, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 21), 15);
        Long subtaskId2 = taskTracker.addNewSubtask(subtask2);
        assertEquals(LocalDateTime.of(2023,9,27,19,35),
                taskTracker.getEndTime(epic));

    }
    @Test
    public void createPrioritizedTasks(){
        Task task3 = new Task("Почитать книгу", "Джордж Оруэлл 1984", Status.NEW,
                LocalDateTime.of(2023, 9, 29, 18, 40), 120);
        long taskId3 = taskTracker.createTask(task3);

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
        //самым первым должна быть subtask1, затем subtask2 а потом task
        assertEquals("Пропылесосить", taskTracker.getPrioritizedTasks()
                .get(LocalDateTime.of(2023, 9, 27, 19, 00)).getName());
        assertEquals("Влажная уборка", taskTracker.getPrioritizedTasks()
                .get(LocalDateTime.of(2023, 9, 27, 19, 21)).getName());
        assertEquals("Почитать книгу", taskTracker.getPrioritizedTasks()
                .get(LocalDateTime.of(2023, 9, 29, 18, 40)).getName());
    }

    @Test
    public void checkTimeStart(){
        Task task3 = new Task("Почитать книгу", "Джордж Оруэлл 1984", Status.NEW,
                LocalDateTime.of(2023, 9, 29, 18, 40), 120);
        long taskId3 = taskTracker.createTask(task3);

        Epic epic1 = new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        long epic1Id = taskTracker.createEpic(epic1);

        Subtask subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        Long subtaskId1 = taskTracker.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро добавить средство и " +
                "сделать влажную уборку в каждой команте", Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 01), 15);
        Long subtaskId2 = taskTracker.addNewSubtask(subtask2);
        assertEquals(false, taskTracker.checkTimeStart());
    }
}
