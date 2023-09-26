package ru.practicum.task_tracker.manager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import ru.practicum.task_tracker.exception.ManagerSaveException;
import ru.practicum.task_tracker.task.*;

public class InMemoryTaskManager implements TaskTracker {
    private final Map<Long, Epic> epics = new HashMap<>();
    private final Map<Long, Subtask> subtasks = new HashMap<>();
    private final Map<Long, Task> tasks = new HashMap<>();
    private long currentId = 0;
    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public long createTask(Task task){
        if (task.getId() == null) {
            task.setId(generateId());
        }
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public long createEpic(Epic epic){
        if (epic.getId() == null) {
            epic.setId(generateId());
        }
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public Long addNewSubtask(Subtask subtask){
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return null;
        }
        if (subtask.getId() == null) {
            subtask.setId(generateId());
        }
        subtasks.put(subtask.getId(), subtask);
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(subtask.getEpicId());
        return subtask.getId();
    }

    @Override
    public String updateTaskStatus(Task task) {
        if (task.getStatus().equals(Status.IN_PROGRESS)) {
            task.setStatus(Status.DONE);
        } else {
            task.setStatus(Status.IN_PROGRESS);
        }
        return task.getStringStatus();
    }

    @Override
    public String updateSubtaskStatus(Subtask subtask) {
        if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
            subtask.setStatus(Status.DONE);
        } else {
            subtask.setStatus(Status.IN_PROGRESS);
        }
        updateEpicStatus(subtask.getEpicId());
        return subtask.getStringStatus();
    }

    @Override
    public void updateEpicStatus(long epicId) {
        Epic epic = epics.get(epicId);
        List<Long> subtasksIds = epic.getSubtaskIds();
        if (subtasksIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        Status status2 = null;
        for (long subtaskId : subtasksIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (status2 == null) {
                status2 = subtask.getStatus();
                if (subtasksIds.size() == 1) {
                    epic.setStatus(status2);
                }
            } else if (status2.equals(subtask.getStatus()) && !status2.equals(Status.IN_PROGRESS)) {
                epic.setStatus(status2);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    @Override
    public void updateEpicInfo(Epic epic, Long id) {
        Epic oldEpicInfo = epics.get(id);
        if (oldEpicInfo != null) {
            oldEpicInfo.setDescription(epic.getDescription());
            oldEpicInfo.setSubtaskIds(epic.getSubtaskIds());
        }
        updateEpicStatus(id);
    }

    @Override
    public void updateSubtaskInfo(Subtask subtask, Long id) {
        Subtask oldSubtaskInfo = subtasks.get(id);
        subtask.setId(oldSubtaskInfo.getId());
        subtask.setEpicId(oldSubtaskInfo.getEpicId());
        subtasks.put(id, subtask);
    }

    @Override
    public void updateTaskInfo(Task task, Long id) {
        Task newTaskInfo = tasks.get(id);
        if (newTaskInfo == null) {
            return;
        }
        tasks.put(id, task);
    }

    @Override
    public void deleteSubtaskById(Long subtaskId) { //удаление конкретной сабтаски
        subtasks.remove(subtaskId);
    }

    @Override
    public void deleteTask(Long id) { //удаление таска
        tasks.remove(id);
    }

    public void deleteAllSubtasks(Long epicId, Epic epic) { //удаление всех сабтасков у конкретного эпика
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId().equals(epicId)) {
                epic.clearSubtaskIds(subtask.getId());
            }
            subtasks.remove(subtask);
            updateEpicStatus(epic.getId());
        }
    }

    //все сабтаски
    @Override
    public List<String> getNamesOfEpicSubtasks (Epic epic) {
        List<String> allSubtasksForCurrentEpic = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
                if (subtask.getId().equals(epic.getSubtaskIds().get(i))) {
                    allSubtasksForCurrentEpic.add(subtask.getName());
                }
            }
        }
        return allSubtasksForCurrentEpic;
    }

    @Override
    public void deleteEpic(Long epicId) { //метод удаления эпика
        epics.remove(epicId);
    }

    public long generateId() {
        return currentId++;
    }

    @Override
    public Task getTask(Long id) {
            if (tasks.containsKey(id)) {
                historyManager.add(tasks.get(id));
                return tasks.get(id);
            }

        return null;
    }

    @Override
    public Subtask getSubtask(Long id) {
            if (subtasks.containsKey(id)) {
                historyManager.add(subtasks.get(id));
                return subtasks.get(id);
            }
        return null;
    }

    @Override
    public Epic getEpic(Long id) {
        if(epics.containsKey(id)){
            historyManager.add(epics.get(id));
            return epics.get(id);
        }

        return null;
    }

    @Override
    public void print() {
        System.out.println("Печать task");
        for (Task task : tasks.values()) {
            System.out.println(task.getName() + " Описание: " + task.getDescription() + " Статус: " + task.getStatus());
        }
        for (Epic epic : epics.values()) {
            System.out.println("Эпик: " + epic.getName() + " Описание: " + epic.getDescription() +
                    " Статус: " + epic.getStatus());
            for (Subtask subtask : subtasks.values()) {
                for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
                    if (subtask.getId().equals(epic.getSubtaskIds().get(i))) {
                        System.out.println("● " + subtask.getName() + " Описание: " + epic.getDescription() +
                                " Статус " + subtask.getStatus());
                    }
                }
            }
        }
    }

    public void printHistory() {
        for (int i = 0; i < historyManager.getHistory().size(); i++) {
            System.out.println(historyManager.getHistory().get(i).getName());
        }
    }
    public Map<Long, Task> getTasks(){
        return tasks;
    }

    public Map<Long, Epic> getEpics() {
        return epics;
    }

    public Map<Long, Subtask> getSubtasks() {
        return subtasks;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
