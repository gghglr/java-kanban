package ru.practicum.task_tracker.manager;

public class Managers {
    private Managers(){};
    public static TaskTracker getDefault() {
        return new FileBackedTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
