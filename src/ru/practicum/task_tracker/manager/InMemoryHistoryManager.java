package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList customLinkedList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        customLinkedList.addTask(task);
    }

    @Override
    public void remove(long id) {
        customLinkedList.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

}
