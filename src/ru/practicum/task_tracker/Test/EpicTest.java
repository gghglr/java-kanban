package ru.practicum.task_tracker.Test;

import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.manager.InMemoryTaskManager;
import ru.practicum.task_tracker.manager.TaskTracker;
import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Status;
import ru.practicum.task_tracker.task.Subtask;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {

    TaskTracker taskTracker = new InMemoryTaskManager();
    Epic epic =new Epic("Убраться дома", "Быстрая уборка", Status.NEW,
            LocalDateTime.of(2023, 9, 27, 19, 00), 0);

    @Test
    public void createEpic(){
        Long epic1Id = taskTracker.createEpic(epic);
        assertEquals(1, taskTracker.getEpics().size());

    }

    @Test
    public void epicWithoutSubtask(){
        Long epic1Id = taskTracker.createEpic(epic);
        assertEquals(null, epic.getSubtaskIds());
    }
    @Test
    public void createEpicWithStatusOfSubtaskNew(){
        Long epic1Id = taskTracker.createEpic(epic);
        Subtask subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status. NEW, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        Long subtaskId1 = taskTracker.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро добавить средство и " +
                "сделать влажную уборку в каждой команте", Status.NEW, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 21), 15);
        Long subtaskId2 = taskTracker.addNewSubtask(subtask2);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void createEpicWithStatusOfSubtaskDone(){
        Long epic1Id = taskTracker.createEpic(epic);
        Subtask subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        Long subtaskId1 = taskTracker.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро добавить средство и " +
                "сделать влажную уборку в каждой команте", Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 21), 15);
        Long subtaskId2 = taskTracker.addNewSubtask(subtask2);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void createEpicWithStatusOfSubtaskDoneAndNew(){
        Long epic1Id = taskTracker.createEpic(epic);
        Subtask subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.DONE, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        Long subtaskId1 = taskTracker.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро добавить средство и " +
                "сделать влажную уборку в каждой команте", Status.NEW, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 21), 15);
        Long subtaskId2 = taskTracker.addNewSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
    @Test
    public void createEpicWithStatusOfSubtaskInProgress(){
        Long epic1Id = taskTracker.createEpic(epic);
        Subtask subtask1 = new Subtask("Пропылесосить", "Включить пылесос и пропылесосить все команты",
                Status.IN_PROGRESS, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 00), 20);
        Long subtaskId1 = taskTracker.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Влажная уборка", "Налить воды в ведро добавить средство и " +
                "сделать влажную уборку в каждой команте", Status.IN_PROGRESS, epic1Id,
                LocalDateTime.of(2023, 9, 27, 19, 21), 15);
        Long subtaskId2 = taskTracker.addNewSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

}
