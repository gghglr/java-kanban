package ru.practicum.task_tracker.Test;

import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.manager.InMemoryTaskManager;
import ru.practicum.task_tracker.manager.TaskTracker;
import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Status;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest {
    TaskTracker taskTracker = new InMemoryTaskManager();

    @Test
    public void haveEpicTest(){
        Subtask subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, 0L,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        Long subtaskId1 = taskTracker.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро добавить средство и " +
                "сделать влажную уборку в каждой команте", Status.DONE, 1L,
                LocalDateTime.of(2023, 9, 27, 19, 21), 15);
        Long subtaskId2 = taskTracker.addNewSubtask(subtask2);

        assertEquals(null, taskTracker.addNewSubtask(subtask1));

        assertEquals(null, taskTracker.addNewSubtask(subtask2));

    }

    @Test
    public void statusCheckTest(){
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

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void EmptyTasksTest(){
        InMemoryTasksManager inMemoryTasksManager = new InMemoryTasksManager();
        assertNull(taskTracker.getTasks());
        assertNull(taskTracker.getSubtasks());
        assertNull(taskTracker.getEpics());
    }

    @Test
    public void createTaskTest(){ //и заодно getTask
        Task task = new Task("Почитать книгу", "Джордж Оруэлл 1984", Status.NEW,
                LocalDateTime.of(2023, 9, 29, 18, 40), 120);
        final Long taskId = taskTracker.createTask(task);
        assertEquals(task, taskTracker.getTask(0L));
    }

    @Test
    public void createEpicTest(){ //getEpic заодно
        Epic epic = new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        final Long epicId = taskTracker.createEpic(epic);
        assertEquals(epicId, taskTracker.getEpic(0L).getId());
    }

    @Test
    public void createSubtaskTest(){ //getSubtask заодно
        Epic epic = new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        final Long epicId = taskTracker.createEpic(epic);
        Subtask subtask = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epicId,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        final Long subtaskId = taskTracker.addNewSubtask(subtask);
        assertEquals(subtask, taskTracker.getSubtask(1L));
    }

    @Test
    void updateTaskStatusTest() {
        Task task = new Task("Почитать книгу", "Джордж Оруэлл 1984", Status.NEW,
                LocalDateTime.of(2023, 9, 29, 18, 40), 120);
        final Long taskId = taskTracker.createTask(task);
        taskTracker.updateTaskStatus(task);
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    void updateSubtaskStatusTest(){ // и updateEpicStatus заодно
        Epic epic =new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        final Long epicId = taskTracker.createEpic(epic);
        Subtask subtask = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epicId,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        final Long subtaskId = taskTracker.addNewSubtask(subtask);
        taskTracker.updateSubtaskStatus(subtask);
        assertEquals(Status.IN_PROGRESS, taskTracker.getSubtask(1L).getStatus());
        assertEquals(Status.IN_PROGRESS, taskTracker.getEpic(0L).getStatus());
    }

    @Test
    void updateEpicInfoTest() {
        Epic epic =new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        final Long epicId = taskTracker.createEpic(epic);
        taskTracker.updateEpicInfo("123", epicId);
        assertEquals("123", taskTracker.getEpic(epicId).getDescription());
    }


    @Test
    void updateSubtaskInfoTest() {
        Epic epic =new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        final Long epicId = taskTracker.createEpic(epic);
        Subtask subtask = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epicId,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        final Long subId = taskTracker.addNewSubtask(subtask);
        taskTracker.updateSubtaskInfo("123", subId);
        assertEquals("123", taskTracker.getSubtask(subId).getDescription());
    }

    @Test
    void updateTaskInfoTest() {
        Task task = new Task("Почитать книгу", "Джордж Оруэлл 1984", Status.NEW,
                LocalDateTime.of(2023, 9, 29, 18, 40), 120);
        final Long taskId = taskTracker.createTask(task);
        taskTracker.updateTaskInfo("123", taskId);
        assertEquals("123", taskTracker.getTask(taskId).getDescription());
    }

    @Test
    void deleteSubtaskByIdTest() {
        Epic epic =new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        final Long epic1Id = taskTracker.createEpic(epic);
        Subtask subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        Long subtaskId1 = taskTracker.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро добавить средство и " +
                "сделать влажную уборку в каждой команте", Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 21), 15);
        Long subtaskId2 = taskTracker.addNewSubtask(subtask2);
        taskTracker.deleteSubtaskById(subtaskId1, epic);
        assertEquals(1, taskTracker.getSubtasks().size());
        assertEquals(1, epic.getSubtaskIds().size());
    }

    @Test
    void deleteTaskTest() {
        Task task = new Task("Почитать книгу", "Джордж Оруэлл 1984", Status.NEW,
                LocalDateTime.of(2023, 9, 29, 18, 40), 120);
        final Long taskId = taskTracker.createTask(task);
        taskTracker.deleteTask(taskId);
        assertEquals(null, taskTracker.getTasks());
    }

    @Test
    void getNamesOfEpicSubtasksTest() {
        Epic epic =new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        Long epic1Id = taskTracker.createEpic(epic);
        Subtask subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        Long subtaskId1 = taskTracker.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро добавить средство и " +
                "сделать влажную уборку в каждой команте", Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 21), 15);
        Long subtaskId2 = taskTracker.addNewSubtask(subtask2);
        List<String> name = new ArrayList<>(List.of("Пропылесосить", "Влажная уборка"));
        assertEquals(name, taskTracker.getNamesOfEpicSubtasks(epic));
    }

    @Test
    public void deleteEpicTest() {
        Epic epic =new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        Long epic1Id = taskTracker.createEpic(epic);
        Subtask subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        Long subtaskId1 = taskTracker.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро добавить средство и " +
                "сделать влажную уборку в каждой команте", Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 21), 15);
        Long subtaskId2 = taskTracker.addNewSubtask(subtask2);
        taskTracker.deleteEpic(epic1Id);
        assertEquals(null, taskTracker.getSubtasks());
        assertEquals(null, taskTracker.getEpics());
    }

    @Test
    public void generateIdTest(){
        Epic epic = new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        Long epicId = taskTracker.createEpic(epic);
        Subtask subtask = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epicId,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        long subtaskId1 = taskTracker.addNewSubtask(subtask);
        assertEquals(1L, subtaskId1);
    }

    @Test
    public void getTasksTest(){
        Task task1 = new Task("Почитать книгу", "Джордж Оруэлл 1984", Status.NEW,
                LocalDateTime.of(2023, 9, 29, 18, 40), 120);
        long taskId1 = taskTracker.createTask(task1);

        Task task2 = new Task("Погулять с собакой", "Пойти прогуляться по парку с собакой в 6 вечера", Status.NEW,
                LocalDateTime.of(2023, 9, 30, 18, 00), 120);
        long taskId2 = taskTracker.createTask(task2);
        assertEquals("Погулять с собакой", taskTracker.getTasks().get(1L).getName());
    }

    @Test
    public void getEpicsTest(){
        Epic epic = new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        final Long epicId = taskTracker.createEpic(epic);
        Subtask subtask = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epicId,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        final Long subId = taskTracker.addNewSubtask(subtask);
        assertEquals("Убраться дома", taskTracker.getEpics().get(0L).getName());
    }

    @Test
    public void getSubtasksTest(){
        Epic epic =new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19, 00), 0);
        final Long epicId = taskTracker.createEpic(epic);
        Subtask subtask = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epicId,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        final Long subId = taskTracker.addNewSubtask(subtask);
        assertEquals("Пропылесосить", taskTracker.getSubtasks().get(1L).getName());
    }

}
