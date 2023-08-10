package ru.practicum.task_tracker.task;

public class Subtask extends Task{
    private Long epicId;
    public Subtask(String name, String desc, Status status, Long epicId) {
        super(name, desc, status);
        this.epicId = epicId; // принимает epicId чтобы знать к какому эпикку принадлжит
    }

    public Long getEpicId() {
        return epicId;
    }

    public void setEpicId(Long epicId) {
        this.epicId = epicId;
    }
}
