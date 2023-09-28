package ru.practicum.task_tracker.manager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

public interface TaskTracker {
    long createTask(Task task);

    long createEpic(Epic epic);

    Long addNewSubtask(Subtask subtask);

    String updateTaskStatus(Task task);

    String updateSubtaskStatus(Subtask subtask);

    void deleteTask(Long id);

    void updateEpicStatus(long epicId);

    void updateEpicInfo(String str, Long id);

    void updateSubtaskInfo(String str, Long id);

    void updateTaskInfo(String str, Long id);

    void deleteSubtaskById(Long subtaskId, Epic epic);

    List<String> getNamesOfEpicSubtasks(Epic epic);

    void deleteEpic(Long epicId);

    void print();

    void printHistory();

    Task getTask(Long id);

    Subtask getSubtask(Long id);

    Epic getEpic(Long id);

    Map<Long, Task> getTasks();

    Map<Long, Epic> getEpics();

    Map<Long, Subtask> getSubtasks();

    HistoryManager getHistoryManager();
    LocalDateTime getEndTime(Epic epic);

    Map<LocalDateTime, Task> createPrioritizedTasks(Task task);
    Map<LocalDateTime, Task> getPrioritizedTasks();
    boolean checkTimeStart();

}
