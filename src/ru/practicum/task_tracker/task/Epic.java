package ru.practicum.task_tracker.task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Long> subtaskIds; // в этом списке лежат id сабстасков для эпика

    public Epic(String name, String desc, Status status) {
        super(name, desc, status);
        subtaskIds = new ArrayList<>();
        setTaskType(TaskType.EPIC);
    }

    public Epic(Long id, String name, String desc, Status status, TaskType taskType) {
        super(id, name, desc, status, taskType);
        subtaskIds = new ArrayList<>();
    }

    public void addSubtaskId(long subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public List<Long> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(List<Long> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void clearSubtaskIds(Long id) {
        subtaskIds.remove(id);
    }

}
