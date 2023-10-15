package ru.practicum.task_tracker.server;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.practicum.task_tracker.manager.FileBackedTasksManager;
import ru.practicum.task_tracker.manager.HistoryManager;
import ru.practicum.task_tracker.manager.InMemoryTaskManager;
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
    TaskTracker taskTracker = new InMemoryTaskManager();


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
            taskData.forEach(d -> taskTracker.createTask(d));
        }

        String epicDataStr = kvTaskClient.load(EPIC);
        if (epicDataStr != null) {
            List<Epic> epicData = gson.fromJson(epicDataStr, new TypeToken<ArrayList<Epic>>() {
            }.getType());
            epicData.forEach(d -> taskTracker.createEpic(d));
        }

        String subtaskDataStr = kvTaskClient.load(SUBTASK);
        if (subtaskDataStr != null) {
            List<Subtask> subtaskData = gson.fromJson(subtaskDataStr, new TypeToken<ArrayList<Subtask>>() {
            }.getType());
            subtaskData.forEach(d -> taskTracker.addNewSubtask(d));
        }
    }

    @Override
    public long createTask(Task task) {
        if(task.getId() == null){
            task.setId(generateId());
        }
        taskTracker.createTask(task);
        String json = gson.toJson(taskTracker.getTasks());
        kvTaskClient.put(TASK, json);
        return task.getId();
    }
    @Override
    public long createEpic(Epic epic) {
        if(epic.getId() == null){
            epic.setId(generateId());
        }
        taskTracker.createEpic(epic);
        String json = new Gson().toJson(taskTracker.getEpics());
        kvTaskClient.put(EPIC, json);
        return epic.getId();
    }
    @Override
    public Long addNewSubtask(Subtask subtask) {
        if(subtask.getId() == null){
            subtask.setId(generateId());
        }
        taskTracker.addNewSubtask(subtask);
        String json = new Gson().toJson(taskTracker.getSubtasks());
        kvTaskClient.put(SUBTASK, json);
        return subtask.getId();
    }

    @Override
    public Task getTask(Long id) {
        return taskTracker.getTask(id);
    }

    @Override
    public Subtask getSubtask(Long id) {
        return taskTracker.getSubtask(id);
    }

    @Override
    public Epic getEpic(Long id) {
        return taskTracker.getEpic(id);
    }

    @Override
    public HistoryManager getHistoryManager() {
        String json = gson.toJson(taskTracker.getHistoryManager());
        kvTaskClient.put("history", json);
        return taskTracker.getHistoryManager();
    }

    @Override
    public String deleteSubtaskById(Long subtaskId) {
        return taskTracker.deleteSubtaskById(subtaskId);
    }

    @Override
    public String deleteTask(Long id) {
        return taskTracker.deleteTask(id);
    }

    @Override
    public String deleteAllSubtasks(Long epicId) {
        return taskTracker.deleteAllSubtasks(epicId);
    }

    @Override
    public void deleteEpic(Long epicId) {
        taskTracker.deleteEpic(epicId);
    }
}
