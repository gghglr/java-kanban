package ru.practicum.task_tracker.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Long> subtaskIds; // в этом списке лежат id сабстасков для эпика

    public Epic(String name, String desc, Status status, LocalDateTime startTime, long duration) {
        super(name, desc, status, startTime, duration);
        subtaskIds = new ArrayList<>();
        setTaskType(TaskType.EPIC);
    }


    public void addSubtaskId(long subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public List<Long> getSubtaskIds() {
        if(subtaskIds.size() != 0){
            return subtaskIds;
        }
        else{
            return null;
        }
    }

    public void setSubtaskIds(List<Long> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void clearSubtaskIds(Long id) {
        subtaskIds.remove(id);
    }

}
