package ru.practicum.task_tracker.task;

import java.time.LocalDateTime;

public class Subtask extends Task {

    public Long epicId;

    public Subtask(String name, String desc, Status status, Long epicId, LocalDateTime startTime, long duration) {
        super(name, desc, status, startTime, duration);
        this.epicId = epicId;
        setTaskType(TaskType.SUBTASK);
    }

    public Long getEpicId() {
       return epicId;
    }

    public void setEpicId(Long epicId) {
        this.epicId = epicId;
    }
}
