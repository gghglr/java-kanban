package ru.practicum.task_tracker.task;

public class Task {
    protected Long id;
    protected String name;
    protected String desc; // описание
    protected String status;

    public Task(String name, String desc, String status) {
        this.name = name;
        this.desc = desc;
        this.status = status;
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

    public String getStatus() {
        return status;
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

    public void setStatus(String status) {
        this.status = status;
    }
}
