package ru.practicum.task_tracker.manager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

public interface TaskTracker {
    long createTask(Task task) throws IOException;

    long createEpic(Epic epic) throws IOException;

    Long addNewSubtask(Subtask subtask) throws IOException;

    String updateTaskStatus(Task task);

    String updateSubtaskStatus(Subtask subtask);

    void deleteTask(Long id);

    void updateEpicStatus(long epicId);

    void updateEpicInfo(Epic epic, Long id);

    void updateSubtaskInfo(Subtask subtask, Long id);

    void updateTaskInfo(Task task, Long id);

    void deleteSubtaskById(Long subtaskId);

    List<String> printEpicById(Epic epic);

    void deleteEpic(Long epicId);

    void print();

    void printHistory();

    Task getTask(Long id);

    Subtask getSubtask(Long id);

    Epic getEpic(Long id);
    void saveHistory(File file);
    void loadFromFile();
}
