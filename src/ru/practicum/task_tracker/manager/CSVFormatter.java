package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.*;

import java.util.ArrayList;
import java.util.List;

public class CSVFormatter {

    private CSVFormatter() {
    }

    public static String toString(Task task) {
        //id,type,name,status,description,epic
        String id = String.valueOf(task.getId());
        TaskType type = task.getTaskType();
        String currType = "";
        String name = task.getName();
        String status = task.getStringStatus();
        String desc = task.getDescription();
        switch (type) {
            case EPIC:
                currType = "EPIC";
                break;
            case TASK:
                currType = "TASK";
                break;
            case SUBTASK:
                Subtask subtask = (Subtask) task;
                currType = "SUBTASK";
                String epicId= String.valueOf(subtask.getEpicId());
                return id + "," + currType + "," + name + "," + status + "," + desc + "," + epicId;
        }
        return id + "," + currType + "," + name + "," + status + "," + desc;
    }



    public static Task fromString(String taskStr) {
        String[] tokens = taskStr.split(",");
        //id,type,name,status,description,epic
        Long id = Long.parseLong(tokens[0]);
        TaskType type = TaskType.valueOf(tokens[1]);
        String name = String.valueOf(tokens[2]);
        Status status = Status.valueOf(tokens[3]);
        String description = String.valueOf(tokens[4]);

        switch (type) {
            case TASK:
                return new Task(id, name, description, status, type);
            case SUBTASK:
                long epicId = Long.parseLong(tokens[5]);
                return new Subtask(id, name, description, status, epicId, type);
            case EPIC:
                return new Epic(id, name, description, status, type);

        }
        return null;
    }

    public static String historyToString(HistoryManager manager) {
        String idStr = "";
        if(manager.getHistory().size() != 0){
            for (int i = 0; i < manager.getHistory().size(); i++) {
                Task task = manager.getHistory().get(i);
                idStr += task.getId() + ",";
            }
            return idStr.substring(0, idStr.length() - 1);
        }
        return idStr;
    }

    public static List<Long> historyFromString(String historyStr) {
        String[] tokens = historyStr.split(",");
        List<Long> historyInt = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            Long id = Long.parseLong(tokens[i]);
            historyInt.add(id);
        }
        return historyInt;
    }
}
