package ru.practicum.task_tracker.task;

public class Task {
    protected Long id;
    protected String name;
    protected String description;
    protected Status status;
    private TaskType taskType = TaskType.TASK;
    protected Long idS;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(Long id, String name, String description, Status status) {
        this.idS = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStringStatus() {
        return String.valueOf(status);
    }

    public Status getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setStatus(Status status2) {
        this.status = status2;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public Long getEpicId() {
        return null;
    }
}
