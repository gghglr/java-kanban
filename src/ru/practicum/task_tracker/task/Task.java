package ru.practicum.task_tracker.task;

import java.time.LocalDateTime;

import java.time.temporal.ChronoUnit;

public class Task{
    protected Long id;
    protected String name;

    protected String description;

    protected Status status;
    protected TaskType taskType = TaskType.TASK;

    private LocalDateTime startTime;

    protected long duration;
    private LocalDateTime endTime;

    public Task(String name, String description, Status status, LocalDateTime startTime, long duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        if(startTime != null){
            this.startTime = startTime;
        }
        else if(startTime == null) { //чтобы положить в конец если дата не указана
            startTime = LocalDateTime.of(9999, 12, 31, 23,59);
            this.startTime = startTime;
        }
        this.duration = duration;
        this.endTime = startTime.plus(duration, ChronoUnit.MINUTES);
        setTaskType(TaskType.TASK);
    }

    public Task(String name, String description, Status status, long duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        setTaskType(TaskType.TASK);
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


    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }


    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    public void endTime(){
        LocalDateTime endTime = startTime.plus(duration, ChronoUnit.MINUTES);
        this.endTime = endTime;
    }

}
