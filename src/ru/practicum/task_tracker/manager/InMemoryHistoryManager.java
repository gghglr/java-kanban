package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private List<Task> history = new ArrayList<>();
    private final int HISTORY_MANAGER = history.size();

    @Override
    public void add(Task task) {
        if(task == null){
            return;
        }
        else if(HISTORY_MANAGER == 10){
            history.remove(0);
            history.add(task);
        }
        else{
            history.add(task);
        }
    }
    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }
}
