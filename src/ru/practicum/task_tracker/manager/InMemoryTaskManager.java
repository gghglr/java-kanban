package ru.practicum.task_tracker.manager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import ru.practicum.task_tracker.task.*;

public class InMemoryTaskManager implements TaskTracker{
    private final Map<Long, Epic> epics = new HashMap<>();
    private final Map<Long, Subtask> subtasks = new HashMap<>();
    private final Map<Long, Task> tasks = new HashMap<>();
    private LocalDateTime endTime = null;

    private long currentId = 0;
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private Map<LocalDateTime, Task> prioritizedTasks;
    private Map<LocalDateTime, Task> toEnd = new TreeMap<>();

    @Override
    public long createTask(Task task){
        if (task.getId() == null) {
            task.setId(generateId());
        }
        task.endTime();
        tasks.put(task.getId(), task);
        createPrioritizedTasks(task);
        checkTimeStart();
        return task.getId();
    }

    @Override
    public long createEpic(Epic epic){
        if (epic.getId() == null) {
            epic.setId(generateId());
        }
        epics.put(epic.getId(), epic);
        checkTimeStart();
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
        createPrioritizedTasks((Task) subtask);
        checkTimeStart();
        getEndTime(epic, subtask);
        return subtask.getId();
    }

    @Override
    public String updateTaskStatus(Task task) {
        if(tasks.containsKey(task.getId())){
            if (task.getStatus().equals(Status.IN_PROGRESS)) {
                task.setStatus(Status.DONE);
            } else {
                task.setStatus(Status.IN_PROGRESS);
            }
            return task.getStringStatus();
        }
       else {
           return "Нет такой задачи";
        }
    }

    @Override
    public String updateSubtaskStatus(Subtask subtask) {
        if(subtasks.containsKey(subtask.getId())){
            if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                subtask.setStatus(Status.DONE);
            } else {
                subtask.setStatus(Status.IN_PROGRESS);
            }
            updateEpicStatus(subtask.getEpicId());
            return subtask.getStringStatus();
        }
        else{
            return "Нет такой подзадачи";
        }
    }

    @Override
    public String updateEpicStatus(long epicId) {
        if(epics.containsKey(epicId)){
            Epic epic = epics.get(epicId);
            List<Long> subtasksIds = epic.getSubtaskIds();
            if (subtasksIds.isEmpty()) {
                epic.setStatus(Status.NEW);
                return null;
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
        return "Нет такого эпика";
    }

    @Override
    public String updateEpicInfo(String str, Long id) {
        if(epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epic.setDescription(str);
            return str;
        }
        else{
            return "Нет такого эпика";
        }
    }

    @Override
    public String updateSubtaskInfo(String str, Long id) {
        if(subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            subtask.setDescription(str);
            return str;
        }
        else{
            return "Нет такой подзадачи";
        }
    }

    @Override
    public String updateTaskInfo(String str, Long id) {
        if(tasks.containsKey(id)) {
            Task task = tasks.get(id);
            task.setDescription(str);
            return str;
        }
        else {
            return "Нет такой задачи";
        }
    }

    @Override
    public String deleteSubtaskById(Long subtaskId, Epic epic) {
        if(subtasks.containsKey(subtaskId)){
            subtasks.remove(subtaskId);
            epic.getSubtaskIds().remove(subtaskId);
            return "";
        }
        return "Нет такой подзадачи";
    }

    @Override
    public String deleteTask(Long id) { //удаление таска
        if(tasks.containsKey(id)){
            tasks.remove(id);
            return "";
        }
       return "Нет такой задачи";
    }

    @Override
    public String deleteAllSubtasks(Long epicId) {
        if(epics.containsKey(epicId)){
            for (Subtask subtask : subtasks.values()) {
                if (subtask.getEpicId().equals(epicId)) {
                    epics.get(epicId).clearSubtaskIds(subtask.getId());
                }
                subtasks.remove(subtask);
                updateEpicStatus(epics.get(epicId).getId());
            }
            return "";
        }
        else {
           return  "Нечего удалять";
        }
    }

    @Override
    public List<String> getNamesOfEpicSubtasks (Epic epic) {
        if(epics.containsValue(epic)){
            List<String> allSubtasksForCurrentEpic = new ArrayList<>();
            for (Subtask subtask : subtasks.values()) {
                if (epic.getSubtaskIds().contains(subtask.getId())) {
                    allSubtasksForCurrentEpic.add(subtask.getName());
                }
            }
            return allSubtasksForCurrentEpic;
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteEpic(Long epicId) {
        Epic epic = epics.get(epicId);
        if(epic.getSubtaskIds() != null){
            for (Long subId : epic.getSubtaskIds()) {
                subtasks.remove(subId);
            }
            epics.remove(epicId);
        }
    }

    public long generateId() {
        return currentId++;
    }

    @Override
    public Task getTask(Long id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));

        }
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtask(Long id) {
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
        }
        return subtasks.get(id);
    }

    @Override
    public Epic getEpic(Long id) {
        if(epics.containsKey(id)){
            historyManager.getHistory().add(epics.get(id));
        }
        return epics.get(id);
    }

    @Override
    public void print() {
        System.out.println("Печать task");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd;HH:mm");
        for (Task task : tasks.values()) {
            System.out.println(task.getName() + " Описание: " + task.getDescription() + " Статус: " + task.getStatus()
            + " Время начала: " + task.getStartTime().format(formatter) + " Время окончания: "
                    + task.getEndTime().format(formatter));
        }
        for (Epic epic : epics.values()) {
            System.out.println("Эпик: " + epic.getName() + " Описание: " + epic.getDescription() +
                    " Статус: " + epic.getStatus() + " Время начала: " + epic.getStartTime().format(formatter)
                    + " Время окончания: " + epic.getEndTime().format(formatter));
            for (Subtask subtask : subtasks.values()) {
                for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
                    if (subtask.getId().equals(epic.getSubtaskIds().get(i))) {
                        System.out.println("● " + subtask.getName() + " Описание: " + epic.getDescription() +
                    " Статус " + subtask.getStatus() + " Время начала: " + subtask.getStartTime().format(formatter)
                                + " Время окончания : " + subtask.getEndTime().format(formatter));
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
    @Override
    public List<Task> getTasks(){
        if(!tasks.isEmpty()){
            List<Task> taskList = new ArrayList<>();
            if (!tasks.isEmpty()) {
                for (Task task : tasks.values()) {
                    taskList.add(task);
                }
            }
            return taskList;
        }
        else{
            return new ArrayList<>();
        }
    }
    @Override
    public List<Epic> getEpics() {
        if(!epics.isEmpty()){
            List<Epic> epicList = new ArrayList<>();
            if (!epics.isEmpty()) {
                for (Epic epic : epics.values()) {
                    epicList.add(epic);
                }
            }
            return epicList;
        }
        return new ArrayList<>();
    }
    @Override
    public List<Subtask> getSubtasks() {
        if(!subtasks.isEmpty()){
            List<Subtask> subtaskList = new ArrayList<>();
            if (!subtasks.isEmpty()) {
                for (Subtask subtask : subtasks.values()) {
                    subtaskList.add(subtask);
                }
            }
            return subtaskList;
        }
        return new ArrayList<>();
    }

    @Override
    public HistoryManager getHistoryManager() {
            return historyManager;
    }

    //новые методы
    @Override
    public LocalDateTime getEndTime(Epic epic, Subtask subtask){
        LocalDateTime localDateTime = epic.getStartTime();
            if(subtasks.containsKey(subtask.getId())){
                if(epic.getEndTime() != null){
                    endTime = epic.getEndTime().plus(subtasks.get(subtask.getId()).getDuration(), ChronoUnit.MINUTES);
                    epic.setEndTime(endTime);
                }
                else{
                    endTime = localDateTime.plus(subtasks.get(subtask.getId()).getDuration(), ChronoUnit.MINUTES);
                    epic.setEndTime(endTime);
                }
            }
        return endTime;
    }


    @Override
    public Map<LocalDateTime, Task> createPrioritizedTasks(Task task) {
        prioritizedTasks = getPrioritizedTasks();

        if(task.getStartTime() != null){
            prioritizedTasks.put(task.getStartTime(), task);
        }
        return prioritizedTasks;
    }
    @Override
    public Map<LocalDateTime, Task> getPrioritizedTasks(){
        Comparator<LocalDateTime> comparator = new Comparator<>() {
            @Override
            public int compare(LocalDateTime o1, LocalDateTime o2) {
                return o1.compareTo(o2);
            }
        };
        if(prioritizedTasks != null) {
            return prioritizedTasks;
        }
        else {
            return new TreeMap<>(comparator);
        }
    }
    @Override
    public boolean checkTimeStart(){
        LocalDateTime prevEndTime = null;
        prioritizedTasks = getPrioritizedTasks();
        for (LocalDateTime localDateTime : prioritizedTasks.keySet()){
            if (prevEndTime == null){
                prevEndTime = prioritizedTasks.get(localDateTime).getEndTime();
                continue;
            }
            if(prevEndTime.isAfter(localDateTime)){
                return false;

            }
        }
        return true;
    }
}
