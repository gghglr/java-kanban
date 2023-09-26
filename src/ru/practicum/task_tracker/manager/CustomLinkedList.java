package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomLinkedList {
    private final Map<Long, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    private static class Node {
        private final Task task;
        private Node prev;
        private Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    public void remove(long id) {
        Node node = nodeMap.get(id);
        if (node == null) {
            return;
        }
        removeNode(node);
    }

    public void addTask(Task task) {
        if (task == null) {
            return;
        }
        Long id = task.getId();
        remove(id);
        linkLast(task);
        nodeMap.put(id, last);

    }

    private void linkLast(Task task) {
        Node node = new Node(task, last, null);
        if (first != null) {
            last.next = node;
        }
        last = node;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Node node : nodeMap.values()) {
            tasks.add(node.task);
        }
        return tasks;
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
            if (node.next == null) {
                last = node.prev;
            } else {
                node.next.prev = node.prev;
            }
        } else {
            first = node.next;
            if (first == null) {
                last = null;
            } else {
                first.prev = null;
            }
        }
    }

}
