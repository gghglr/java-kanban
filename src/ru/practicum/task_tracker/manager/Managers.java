package ru.practicum.task_tracker.manager;

public class Managers {
    private Managers(){};
    public static TaskTracker getDefault() {
        return new HttpTaskManager("http://localhost:8078/");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
