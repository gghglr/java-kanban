package ru.practicum.task_tracker.task;

public enum Status {

    NEW("новый"),
    IN_PROGRESS("В процессе"),
    DONE("готово");

    private final String rus;

    Status(String rus) {
        this.rus = rus;
    }

    @Override
    public String toString() {
        return rus;
    }
}
