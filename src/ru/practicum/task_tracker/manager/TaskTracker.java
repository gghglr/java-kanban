package ru.practicum.task_tracker.manager;

import java.util.ArrayList;
import java.util.HashMap;

import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

public class TaskTracker {
    private HashMap<Long, Epic> epics = new HashMap<>(); // айди эпика и
    private HashMap<Long, Subtask> subtasks = new HashMap<>();
    private HashMap<Long, Task> tasks = new HashMap<>();
    private long currentId = 0;
    public long createTask(Task task){
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task.getId();
    }
    public long createEpic(Epic epic){
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic.getId();
    }
    public Long addNewSubtask(Subtask subtask){
        Epic epic = epics.get(subtask.getEpicId());
        if(epic == null){
            return null;
        }
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(subtask.getEpicId());

        return subtask.getId();
    }

    public String updateTaskStatus(Task task){
        if(task.getStatus().equals("In_Progress")){
            task.setStatus("Done");
        }
        else{
            task.setStatus("In_Progress");
        }
        return task.getStatus();
    }
    public String updateSubtaskStatus(Subtask subtask){
        if(subtask.getStatus().equals("In_Progress")){
            subtask.setStatus("Done");
        }
        else{
            subtask.setStatus("In_Progress");
        }
        updateEpicStatus(subtask.getEpicId());
        return subtask.getStatus();
    }

    public void updateEpicStatus(long epicId){
        Epic epic = epics.get(epicId);
      ArrayList<Long> subtasksIds = epic.getSubtaskIds();
        if(subtasksIds.isEmpty()){
            epic.setStatus("New");
            return;
        }
        String status = null;
        for(long subtaskId : subtasksIds){
            Subtask subtask = subtasks.get(subtaskId);

            if(status == null){
                status = subtask.getStatus();
                    if(subtasksIds.size() == 1){
                        epic.setStatus(status);
                    }
            }
            else if(status.equals(subtask.getStatus()) && !status.equals("In_Progress")){
                epic.setStatus(status);
            }
            else{
                epic.setStatus("In_progress");
            }
        }
    }
    public void updateEpicInfo(Epic epic, Long id){
        Epic oldEpicInfo = epics.get(id);
        epic.setId(id);
        epic.setSubtaskIds(oldEpicInfo.getSubtaskIds());
        if(oldEpicInfo == null){
            return;
        }
        epics.put(id, epic);
        updateEpicStatus(id);
    }
    public void updateSubtaskInfo(Subtask subtask, Long id){
        Subtask oldSubtaskInfo = subtasks.get(id);
        subtask.setId(oldSubtaskInfo.getId());
        subtask.setEpicId(oldSubtaskInfo.getEpicId());
        if(oldSubtaskInfo == null){
            return;
        }
        subtasks.put(id, subtask);
    }
    public void updateTaskInfo(Task task, Long id){
        Task newTaskInfo = tasks.get(id);
        if(newTaskInfo == null){
            return;
        }
        tasks.put(id, task);
    }
    public void deleteSubtaskById(Long subtaskId){ //удаление конкретной сабтаски
        subtasks.remove(subtaskId);
    }
    public void deleteTask(Long id){ //удаление таска
        tasks.remove(id);
    }
    public void deleteAllSubtasks(Long epicId, Epic epic){ //удаление всех сабтасков у конкретного эпика
        for(Subtask subtask : subtasks.values()){
            if(subtask.getEpicId().equals(epicId)){
                epic.clearSubtaskIds(subtask.getId());
            }
            subtasks.remove(subtask);
        }
    }
    //все сабтаски
    public ArrayList<String> printEpicById(Epic epic) { // метод возрщения и печати конкретного эпика
        ArrayList<String> allSubtasksForCurrentEpic = new ArrayList<>();
        System.out.println("Эпик: " + epic.getName() + " Статус: " + epic.getStatus());
        for (Subtask subtask : subtasks.values()) {
            for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
                if (subtask.getId().equals(epic.getSubtaskIds().get(i))) {
                    allSubtasksForCurrentEpic.add(subtask.getName());
                }
            }
        }
        for(int j = 0; j < allSubtasksForCurrentEpic.size(); j++){
            System.out.println(allSubtasksForCurrentEpic.get(j));
        }
        return allSubtasksForCurrentEpic;
    }
    public void deleteEpic(Long epicId){ //метод удаления эпика
        epics.remove(epicId);
    }
    private long generateId(){
        return currentId++;
    }
    public void print(){
        System.out.println("Печать task");
        for(Task task : tasks.values()){
            System.out.println(task.getName() + " Описание: " + task.getDesc() + " Статус: " + task.getStatus());
        }
        for(Epic epic : epics.values()) {
            System.out.println("Эпик: " + epic.getName() + " Описание: " + epic.getDesc() +
                    " Статус: " + epic.getStatus());
            for (Subtask subtask : subtasks.values()) {
                for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
                    if (subtask.getId().equals(epic.getSubtaskIds().get(i))) {
                        System.out.println("● " + subtask.getName() + " Описание: " + epic.getDesc() +
                                " Статус " + subtask.getStatus());
                    }
                }
            }
        }
    }
}
