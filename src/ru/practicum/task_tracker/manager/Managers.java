package ru.practicum.task_tracker.manager;

public class Managers {
    private Managers(){};
    public static TaskTracker getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
