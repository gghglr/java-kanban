package ru.practicum.task_tracker.server;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.practicum.task_tracker.manager.FileBackedTasksManager;
import ru.practicum.task_tracker.manager.HistoryManager;
//import ru.practicum.task_tracker.manager.LocalDateTimeTypeAdapter;
import ru.practicum.task_tracker.manager.TaskTracker;
import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

public class HttpTaskManager extends FileBackedTasksManager implements TaskTracker {
    public static final String TASK = "task";
    public static final String EPIC = "epic";
    public static final String SUBTASK = "subtask";

    private final KVTaskClient kvTaskClient;
    private final Gson gson;


    public HttpTaskManager() {

        kvTaskClient = new KVTaskClient();
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        load();
    }

    public HttpTaskManager(KVTaskClient kvTaskClient, boolean isLoad) {
        this.kvTaskClient = kvTaskClient;
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        if (isLoad) {
            load();
        }
    }

    private void load() {
        String taskDataStr = kvTaskClient.load(TASK);
        if (taskDataStr != null) {
            List<Task> taskData = gson.fromJson(taskDataStr, new TypeToken<ArrayList<Task>>() {
            }.getType());
            taskData.forEach(d -> super.createTask(d));
        }

        String epicDataStr = kvTaskClient.load(EPIC);
        if (epicDataStr != null) {
            List<Epic> epicData = gson.fromJson(epicDataStr, new TypeToken<ArrayList<Epic>>() {
            }.getType());
            epicData.forEach(d -> super.createEpic(d));
        }

        String subtaskDataStr = kvTaskClient.load(SUBTASK);
        if (subtaskDataStr != null) {
            List<Subtask> subtaskData = gson.fromJson(subtaskDataStr, new TypeToken<ArrayList<Subtask>>() {
            }.getType());
            subtaskData.forEach(d -> super.addNewSubtask(d));
        }
    }

    @Override
    public long createTask(Task task) {
        if(task.getId() == null){
            task.setId(generateId());
        }
        super.createTask(task);
        String json = gson.toJson(super.getTasks());
        kvTaskClient.put(TASK, json);
        return task.getId();
    }
    @Override
    public long createEpic(Epic epic) {
        if(epic.getId() == null){
            epic.setId(generateId());
        }
        super.createEpic(epic);
        String json = new Gson().toJson(super.getEpics());
        kvTaskClient.put(EPIC, json);
        return epic.getId();
    }
    @Override
    public Long addNewSubtask(Subtask subtask) {
        if(subtask.getId() == null){
            subtask.setId(generateId());
        }
        super.addNewSubtask(subtask);
        String json = new Gson().toJson(super.getSubtasks());
        kvTaskClient.put(SUBTASK, json);
        return subtask.getId();
    }

    @Override
    public Task getTask(Long id) {
        return super.getTask(id);
    }

    @Override
    public Subtask getSubtask(Long id) {
        return super.getSubtask(id);
    }

    @Override
    public Epic getEpic(Long id) {
        return super.getEpic(id);
    }

    @Override
    public HistoryManager getHistoryManager() {
        String json = gson.toJson(super.getHistoryManager());
        kvTaskClient.put("history", json);
        return super.getHistoryManager();
    }

}
