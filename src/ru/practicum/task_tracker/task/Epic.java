package ru.practicum.task_tracker.task;

import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Long> subtaskIds; // в этом списке лежат id сабстасков для эпика
    public Epic(String name, String desc) {
        super(name, desc, "New");
        subtaskIds = new ArrayList<>();
    }

    public void addSubtaskId(long subtaskId){
        subtaskIds.add(subtaskId);
    }

    public ArrayList<Long> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Long> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void clearSubtaskIds(Long id){
        subtaskIds.remove(id);
    }
}
