package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.server.HttpTaskManager;

public class Managers {
    private Managers(){};
    public static TaskTracker getDefault() {
        return new HttpTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
