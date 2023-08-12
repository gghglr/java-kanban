package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private List<Task> history = new ArrayList<>();
    private static int MAX_HISTORY_SIZE = 10;

    @Override
    public void add(Task task) {
        if(task == null){
            return;
        }
        else if(history.size() == MAX_HISTORY_SIZE){
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
