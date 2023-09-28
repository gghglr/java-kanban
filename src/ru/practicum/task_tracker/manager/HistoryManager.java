package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(Long id);

    List<Task> getHistory();

}
