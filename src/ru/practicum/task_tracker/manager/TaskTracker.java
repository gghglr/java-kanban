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

    String deleteTask(Long id);

    String updateEpicStatus(long epicId);

    String updateEpicInfo(String str, Long id);

    String updateSubtaskInfo(String str, Long id);

    String updateTaskInfo(String str, Long id);

    String deleteSubtaskById(Long subtaskId, Epic epic);

    List<String> getNamesOfEpicSubtasks(Epic epic);

    void deleteEpic(Long epicId);

    String deleteAllSubtasks(Long epicId);

    void print();

    void printHistory();

    Task getTask(Long id);

    Subtask getSubtask(Long id);

    Epic getEpic(Long id);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    HistoryManager getHistoryManager();
    //новые методы
    LocalDateTime getEndTime(Epic epic, Subtask subtask);

    Map<LocalDateTime, Task> createPrioritizedTasks(Task task);
    Map<LocalDateTime, Task> getPrioritizedTasks();
    boolean checkTimeStart();

}
