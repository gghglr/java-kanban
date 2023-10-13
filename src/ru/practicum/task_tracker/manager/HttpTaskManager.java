package ru.practicum.task_tracker.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.practicum.task_tracker.server.KVTaskClient;
import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager implements TaskTracker{
    public  static String TASK = "task";
    public  static String EPIC = "epic";
    public  static String SUBTASK = "subtask";

    private final KVTaskClient kvTaskClient;
    private final Gson gson;
    private String URL;


    public HttpTaskManager(String URL){
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.URL = URL;
        kvTaskClient = new KVTaskClient();
        load();
    }

    private void load(){
        String takeTask = kvTaskClient.load(TASK);
        if(takeTask != null){
            List<Task> listTask = gson.fromJson(takeTask, new TypeToken<ArrayList<Task>>(){}.getType());
            listTask.forEach(t -> createTask(t));
        }

        String takeEpic = kvTaskClient.load(EPIC);
        if(takeEpic != null){
            List<Epic> listEpic = gson.fromJson(takeEpic, new TypeToken<ArrayList<Epic>>(){}.getType());
            listEpic.forEach(e -> createEpic(e));
        }

        String takeSubtask = kvTaskClient.load(SUBTASK);
        if(takeSubtask != null){
            List<Subtask> listSubtask = gson.fromJson(takeSubtask, new TypeToken<ArrayList<Subtask>>(){}.getType());
            listSubtask.forEach(t -> addNewSubtask(t));
        }
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
    public long createTask(Task task) {
        super.createTask(task);
        String json = new Gson().toJson(getTasks());
        kvTaskClient.put(TASK, json);
        return task.getId();
    }

    @Override
    public long createEpic(Epic epic) {
        super.createEpic(epic);
        String json = new Gson().toJson(getEpics());
        kvTaskClient.put(EPIC, json);
        return epic.getId();
    }

    @Override
    public Long addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        String json = new Gson().toJson(getSubtasks());
        kvTaskClient.put(SUBTASK, json);
        return subtask.getId();
    }
}
