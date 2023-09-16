package ru.practicum.task_tracker.task;

public class Subtask extends Task {

    private Long epicId;
    private TaskType taskType = TaskType.SUBTASK;

    public Subtask(String name, String desc, Status status, Long epicId) {
        super(name, desc, status);
        this.epicId = epicId;
    }

    public Subtask(Long id, String name, String desc, Status status, Long epicId) {
        super(id, name, desc, status);
        this.epicId = epicId;
    }
    @Override
    public Long getEpicId() {
        return epicId;
    }

    public void setEpicId(Long epicId) {
        this.epicId = epicId;
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }
}
