package ru.practicum.task_tracker.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.manager.Managers;
import ru.practicum.task_tracker.manager.TaskTracker;
import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Status;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {

    KVServer kvServer;
    @BeforeEach
    public void setUp() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @AfterEach
    public void shutDown(){
        kvServer.stop();
    }

    @Test
    public void getMethodTest(){
        TaskTracker httpTaskManager = Managers.getDefault();
        Task initTask = new Task("Почитать книгу", "Джордж Оруэлл 1984", Status.NEW,
                LocalDateTime.of(2023, 9, 29, 18, 40), 120);
        long taskId3 = httpTaskManager.createTask(initTask);
        Epic initEpic = new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        long epic1Id = httpTaskManager.createEpic(initEpic);
        Subtask initsubtask = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        Long subtaskId1 = httpTaskManager.addNewSubtask(initsubtask);
        TaskTracker restoredHttpTaskManager = Managers.getDefault();


        Epic restoredEpic = restoredHttpTaskManager.getEpic(1L);
        Subtask restoredSubtask = restoredHttpTaskManager.getSubtask(2L);
        Task restoredTask = restoredHttpTaskManager.getTask(0L);

        Assertions.assertEquals(initTask.getName(), restoredTask.getName());
        Assertions.assertEquals(initEpic.getName(), restoredEpic.getName());
        Assertions.assertEquals(initsubtask.getName(), restoredSubtask.getName());

    }

    @Test
    public void test2(){
        TaskTracker httpTaskManager = Managers.getDefault();
        Task initTask = new Task("Почитать книгу", "Джордж Оруэлл 1984", Status.NEW,
                LocalDateTime.of(2023, 9, 29, 18, 40), 120);
        long taskId3 = httpTaskManager.createTask(initTask);
        Epic initEpic = new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        long epic1Id = httpTaskManager.createEpic(initEpic);
        Subtask initsubtask = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        Long subtaskId1 = httpTaskManager.addNewSubtask(initsubtask);
        HttpTaskManager httpTaskManager1 = new HttpTaskManager(new KVTaskClient(), false);
        httpTaskManager1.createTask(initTask);

        HttpTaskManager httpTaskManager2 = new HttpTaskManager(new KVTaskClient(), true);
        Epic restoredEpic = httpTaskManager2.getEpic(1L);
        Subtask restoredSubtask = httpTaskManager2.getSubtask(2L);
        Task restoredTask = httpTaskManager2.getTask(0L);

        Assertions.assertEquals(initTask.getName(), restoredTask.getName());
        Assertions.assertEquals(initEpic.getName(), restoredEpic.getName());
        Assertions.assertEquals(initsubtask.getName(), restoredSubtask.getName());
    }
}