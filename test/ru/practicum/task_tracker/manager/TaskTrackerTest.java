package ru.practicum.task_tracker.manager;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Status;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TaskTrackerTest<T extends TaskTracker> {
    protected T taskTracker;
    protected Epic epic1;
    protected  Long epic1Id;
    protected Subtask subtask1;
    protected Long subtaskId1;
    protected Subtask subtask2;
    protected Long subtaskId2;
    protected Task task;
    protected Long taskId;

    @BeforeEach
    public void setUp(){
        taskTracker = (T) new InMemoryTaskManager();
        epic1 =new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
                LocalDateTime.of(2023, 9, 27, 19,0), 0);
        epic1Id = taskTracker.createEpic(epic1);
        subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все комнаты",
                Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19,0), 20);
        subtaskId1 = taskTracker.addNewSubtask(subtask1);
        subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро добавить средство и " +
                "сделать влажную уборку в каждой комнате", Status.IN_PROGRESS, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19,21), 15);
        subtaskId2 = taskTracker.addNewSubtask(subtask2);
        task = new Task("Почитать книгу", "Джордж Оруэлл 1984", Status.NEW,
                LocalDateTime.of(2023, 9, 29, 18,40), 120);
        taskId = taskTracker.createTask(task);
    }

    @Test
    public void taskMethodsTest(){

        assertEquals(taskTracker.getTask(taskId), task);//проверка и createTask и getTask

        taskTracker.updateTaskStatus(task);

        assertEquals(Status.IN_PROGRESS, taskTracker.getTasks().get(0).getStatus());//проверка смены статуса

        taskTracker.updateTaskInfo("1984", taskId);

        assertEquals("1984", taskTracker.getTask(taskId).getDescription());//проверка смены описания
        //и метода getTask

        taskTracker.deleteTask(taskId);

        assertEquals(0, taskTracker.getTasks().size());//проверка удаления
    }

    @Test
    public void epicMethodsTest(){

        assertEquals(taskTracker.getEpics().get(0), epic1);//сразу проверка так как epic создался в setUp

        taskTracker.updateEpicInfo("убраться", epic1Id);

        assertEquals("убраться", taskTracker.getEpic(epic1Id).getDescription());


        //все подзадачи со статусом New
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.NEW);
        taskTracker.updateEpicStatus(epic1Id);

        assertEquals(Status.NEW, taskTracker.getEpic(epic1Id).getStatus());

        //все подзадачи со статусом Done
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskTracker.updateEpicStatus(epic1Id);

        assertEquals(Status.DONE, taskTracker.getEpic(epic1Id).getStatus());

        //подзадачи со статусом IN_PROGRESS
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask1.setStatus(Status.IN_PROGRESS);
        taskTracker.updateEpicStatus(epic1Id);

        assertEquals(Status.IN_PROGRESS, taskTracker.getEpic(epic1Id).getStatus());

        //удаление 1 подзадачи
        taskTracker.deleteSubtaskById(subtaskId1);

        assertEquals(1, epic1.getSubtaskIds().size());

        //удаление всех подзадач:
        taskTracker.deleteAllSubtasks(epic1Id);

        assertEquals(new ArrayList<>(), epic1.getSubtaskIds());

        //удаление эпика
        taskTracker.deleteEpic(epic1Id);

        assertEquals(0, taskTracker.getEpics().size());
    }

    @Test
    public void subtaskMethodsTest(){

        assertEquals(taskTracker.getSubtasks().get(0), subtask1);

        taskTracker.updateSubtaskInfo("пропылесосить", subtaskId1);

        assertEquals("пропылесосить", taskTracker.getSubtask(subtaskId1).getDescription());//проверка смены
        //описания и метода getSubtask

        taskTracker.updateSubtaskStatus(subtask2);

        assertEquals(Status.DONE, taskTracker.getSubtask(subtaskId2).getStatus());//проверка смены статуса

        assertEquals(Status.DONE, taskTracker.getEpic(epic1Id).getStatus());//статус у эпика теперь должен стать Done

        taskTracker.deleteSubtaskById(subtaskId2);

        assertEquals(1, taskTracker.getSubtasks().size());

        taskTracker.deleteAllSubtasks(epic1Id);

        assertEquals(new ArrayList<>(), taskTracker.getEpic(epic1Id).getSubtaskIds());
    }

    @Test
    public void testTime(){

        assertEquals(LocalDateTime.of(2023,9,27,19,35),
                taskTracker.getEpic(epic1Id).getEndTime());

        assertEquals("Пропылесосить", taskTracker.getPrioritizedTasks()
                .get(LocalDateTime.of(2023, 9, 27, 19,0)).getName());
        assertEquals("Влажная уборка", taskTracker.getPrioritizedTasks()
                .get(LocalDateTime.of(2023, 9, 27, 19,21)).getName());
        assertEquals("Почитать книгу", taskTracker.getPrioritizedTasks()
                .get(LocalDateTime.of(2023, 9, 29, 18,40)).getName());

        assertTrue(taskTracker.checkTimeStart());
    }

    @Test
    public void testWithEmptyListOfTasks(){
        taskTracker.deleteTask(taskId);
        taskTracker.deleteEpic(epic1Id);
        taskTracker.deleteAllSubtasks(epic1Id);

        assertEquals("Нет такой задачи", taskTracker.updateTaskStatus(task));
        assertEquals("Нет такой подзадачи", taskTracker.updateSubtaskStatus(subtask1));
        assertEquals("Нет такого эпика", taskTracker.updateEpicStatus(epic1Id));
        assertEquals("Нет такой задачи", taskTracker.updateTaskInfo("asd", taskId));
        assertEquals("Нет такой подзадачи", taskTracker.updateSubtaskInfo("asd", subtaskId1));
        assertEquals("Нет такого эпика", taskTracker.updateEpicInfo("asd", epic1Id));
        assertEquals("Нет такой задачи", taskTracker.deleteTask(taskId));
        assertEquals("Нет такой подзадачи", taskTracker.deleteSubtaskById(subtaskId1));
        assertEquals("Нечего удалять", taskTracker.deleteAllSubtasks(epic1Id));
        assertEquals(new ArrayList<>(), taskTracker.getNamesOfEpicSubtasks(epic1));
        assertEquals(new ArrayList<>(), taskTracker.getTasks());
        assertEquals(new ArrayList<>(), taskTracker.getSubtasks());
        assertEquals(new ArrayList<>(), taskTracker.getEpics());
    }

    @Test
    public void testWithWrongId(){
        task.setId(10L);
        epic1.setId(11L);
        subtask1.setId(12L);
        subtask2.setId(13L);
        assertEquals("Нет такой задачи", taskTracker.updateTaskStatus(task));
        assertEquals("Нет такой подзадачи", taskTracker.updateSubtaskStatus(subtask1));
        assertEquals("Нет такого эпика", taskTracker.updateEpicStatus(11L));
        assertEquals("Нет такой задачи", taskTracker.updateTaskInfo("asd", 10L));
        assertEquals("Нет такой подзадачи", taskTracker.updateSubtaskInfo("asd", 12L));
        assertEquals("Нет такого эпика", taskTracker.updateEpicInfo("asd", 11L));
        assertEquals("Нет такой задачи", taskTracker.deleteTask(10L));
        assertEquals("Нет такой подзадачи", taskTracker.deleteSubtaskById(12L));
        assertEquals("Нечего удалять", taskTracker.deleteAllSubtasks(10L));
        assertEquals(new ArrayList<>(), taskTracker.getNamesOfEpicSubtasks(epic1));
    }

    @Test
    public void historyManagerTest(){
        HistoryManager historyManager = taskTracker.getHistoryManager();
        taskTracker.getSubtask(subtaskId1);
        taskTracker.getSubtask(subtaskId2);
        taskTracker.getTask(taskId);
        taskTracker.getTask(taskId);
        taskTracker.getTask(taskId);

        assertEquals(3, historyManager.getHistory().size());

        historyManager.remove(subtaskId2);//из середины

        assertEquals(2, historyManager.getHistory().size());

        historyManager.remove(taskId); //из конца

        assertEquals(1, historyManager.getHistory().size());

        historyManager.remove(subtaskId1);

        assertEquals(0, historyManager.getHistory().size());

        assertEquals(new ArrayList<>(), historyManager.getHistory());
    }
}