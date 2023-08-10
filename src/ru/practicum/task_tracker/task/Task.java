package ru.practicum.task_tracker.task;

public class Task {
    protected Long id;
    protected String name;
    protected String desc; // описание
    protected Status status2;

    public Task(String name, String desc, Status status) {
        this.name = name;
        this.desc = desc;
        this.status2 = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getStringStatus() {
        return String.valueOf(status2);
    }

    public Status getStatus() {
        return status2;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setStatus(Status status2) {
        this.status2 = status2;
    }


}
