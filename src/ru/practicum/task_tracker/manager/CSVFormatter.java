package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVFormatter {

    private CSVFormatter() {
    }

    public static String toString(Task task) {
        //id,type,name,status,description,epic,startTime,duration
        String id = String.valueOf(task.getId());
        TaskType type = task.getTaskType();
        String currType = "";
        String name = task.getName();
        String status = task.getStringStatus();
        String desc = task.getDescription();
        String startTime = String.valueOf(task.getStartTime());
        String duration = String.valueOf(task.getDuration());
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
                return id + "," + currType + "," + name + "," + status + "," + desc  + "," + startTime +
                        "," + duration + "," + epicId;
        }
        return id + "," + currType + "," + name + "," + status + "," + desc + "," + startTime +
                "," + duration;
    }



    public static Task fromString(String taskStr) {
        String[] tokens = taskStr.split(",");
        //id,type,name,status,description,epic
        Long id = Long.parseLong(tokens[0]);
        TaskType type = TaskType.valueOf(tokens[1]);
        String name = String.valueOf(tokens[2]);
        Status status = Status.valueOf(tokens[3]);
        String description = String.valueOf(tokens[4]);
        LocalDateTime startTime = LocalDateTime.parse(tokens[5]);
        long duration = Long.parseLong(tokens[6]);
        switch (type) {
            case TASK:
                Task task = new Task(name, description, status, startTime, duration);
                task.setId(id);
                return task;
            case SUBTASK:
                long epicId = Long.parseLong(tokens[7]);
                Subtask subtask = new Subtask(name, description, status, epicId, startTime, duration);
                subtask.setId(id);
                return subtask;
            case EPIC:
              Epic epic = new Epic( name, description, status, startTime, duration);
              epic.setId(id);
              return epic;
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
