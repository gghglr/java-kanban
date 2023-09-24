package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.*;

import java.util.ArrayList;
import java.util.List;

public class CSVFormatter {

    private CSVFormatter() {
    }

    public static String toString(Task task) { //строка из такси
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
                currType = "SUBTASK";
                String epicId= String.valueOf(Subtask.getEpicId());
                return id + "," + currType + "," + name + "," + status + "," + desc + "," + epicId;
        }
        return id + "," + currType + "," + name + "," + status + "," + desc;
    }

    public static String toString(Subtask subtask) { //строка из такси
        //id,type,name,status,description,epic
        String id = String.valueOf(subtask.getId());
        String type = "SUBTASK";
        String name = subtask.getName();
        String status = subtask.getStringStatus();
        String desc = subtask.getDescription();
        String epicId = String.valueOf(subtask.getEpicId());
        return id + "," + type + "," + name + "," + status + "," + desc + "," + epicId;
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
        for (int i = 0; i < manager.getHistory().size(); i++) {
            Task task = manager.getHistory().get(i);
            idStr += String.valueOf(task.getId()) + ",";
        }
        return idStr.substring(0, idStr.length() - 1);
    }

    public static List<Integer> historyFromString(String historyStr) {
        String[] tokens = historyStr.split(",");
        List<Integer> historyInt = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            Integer id = Integer.parseInt(tokens[i]);
            historyInt.add(id);
        }
        return historyInt;
    }
}
