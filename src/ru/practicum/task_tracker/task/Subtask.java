package ru.practicum.task_tracker.task;

public class Subtask extends Task {

    public Long epicId;

    public Subtask(String name, String desc, Status status, Long epicId) {
        super(name, desc, status);
        this.epicId = epicId;
        setTaskType(TaskType.SUBTASK);
    }

    public Subtask(Long id, String name, String desc, Status status, Long epicId, TaskType taskType) {
        super(id, name, desc, status, taskType);
        this.epicId = epicId;
    }


    public Long getEpicId() {
       return epicId;
    }

    public void setEpicId(Long epicId) {
        this.epicId = epicId;
    }
}
