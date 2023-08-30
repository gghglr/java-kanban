package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager{
    //private final List<Task> history = new ArrayList<>();
    CustomLinkedList customLinkedList = new CustomLinkedList();
    @Override
    public void add(Task task) {
        customLinkedList.AddTask(task);
    }
    @Override
    public void remove(int id){
        customLinkedList.remove(id);
    }
    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

}
