package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.exception.ManagerSaveException;
import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;
import ru.practicum.task_tracker.task.TaskType;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static List<String> lines = new ArrayList<>();

    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }
    public FileBackedTasksManager(){};

    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String line = reader.readLine();
                if (!line.isEmpty()) {
                    lines.add(line);
                } else if (line.isEmpty()) {
                    List<Long> historyInt = CSVFormatter.historyFromString(reader.readLine());
                    sortTasks();
                    for (int i = 0; i < historyInt.size(); i++) {
                        if (super.getEpic(historyInt.get(i)) != null) {
                            getEpic(historyInt.get(i));
                        } else if (super.getTask(historyInt.get(i)) != null) {
                            getTask(historyInt.get(i));
                        } else if (super.getSubtask(historyInt.get(i)) != null) {
                            getSubtask(historyInt.get(i));
                        }
                    }
                   break;
                }
            }
        }
        catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public long createTask(Task task) {
        super.createTask(task);
        save();
        return task.getId();
    }

    @Override
    public long createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public Long addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        save();
        return  subtask.getId();
    }

    public void sortTasks(){
        for (int i = 0; i < lines.size(); i++) {
            String s = lines.get(i);
            Task task = CSVFormatter.fromString(s);
            if (task.getTaskType().equals(TaskType.EPIC)) {
                Task newEpic = new Epic(task.getName(), task.getDescription(), task.getStatus(), task.getStartTime(),
                        task.getDuration());
                newEpic.setId(task.getId());
                createEpic((Epic) newEpic);
            } else if (task.getTaskType().equals(TaskType.TASK)) {
                Task newTask = new Task(task.getName(), task.getDescription(), task.getStatus(), task.getStartTime(),
                        task.getDuration());
                newTask.setId(task.getId());
                createTask(newTask);
            } else if (task.getTaskType().equals(TaskType.SUBTASK)) {
                Subtask subtask = (Subtask) task;
                Subtask newSubtask = new Subtask(task.getName(), task.getDescription(), task.getStatus(),
                        subtask.getEpicId(), subtask.getStartTime(), subtask.getDuration());
                newSubtask.setId(task.getId());
                addNewSubtask(newSubtask);
            }
        }
    }




    public void save(){
        try (Writer fileWriter = new FileWriter(file)) {
            if(getTasks() != null){
                for (Task task : getTasks().values()) {
                    fileWriter.write(CSVFormatter.toString(task) + "\n");
                }
            }

            if(getEpics() != null){
                for (Epic epic : getEpics().values()) {
                    fileWriter.write(CSVFormatter.toString(epic) + "\n");
                }
            }

            if(getSubtasks() != null){
                for (Subtask subtask : getSubtasks().values()) {
                    fileWriter.write(CSVFormatter.toString(subtask) + "\n");
                }
            }
           if(getHistoryManager().getHistory() != null) {
                fileWriter.write("\n" + CSVFormatter.historyToString(super.getHistoryManager()));
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }

    }

    @Override
    public Task getTask(Long id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtask(Long id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpic(Long id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }


    @Override
    public String updateTaskStatus(Task task) {
        super.updateTaskStatus(task);
        save();
        return task.getStringStatus();
    }

    @Override
    public String updateSubtaskStatus(Subtask subtask) {
        super.updateSubtaskStatus(subtask);
        save();
        return subtask.getStringStatus();
    }
    @Override
    public void updateEpicStatus(long epicId) {
        super.updateEpicStatus(epicId);
        save();
    }

    @Override
    public void updateEpicInfo(String str, Long id) {
        super.updateEpicInfo(str, id);
        save();
    }

    @Override
    public void updateSubtaskInfo(String str, Long id) {
        super.updateSubtaskInfo(str, id);
        save();
    }

    @Override
    public void updateTaskInfo(String str, Long id) {
        super.updateTaskInfo(str, id);
        save();
    }

    @Override
    public void deleteSubtaskById(Long subtaskId, Epic epic) {
        super.deleteSubtaskById(subtaskId, epic);
        save();
    }

    @Override
    public void deleteTask(Long id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllSubtasks(Long epicId, Epic epic) {
        super.deleteAllSubtasks(epicId, epic);
        save();
    }

    @Override
    public void deleteEpic(Long epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return super.getHistoryManager();
    }

    public void removeHistoryTask(Long id){
        HistoryManager manager = getHistoryManager();
        manager.remove(id);
        save();
    }


    public Map<LocalDateTime, Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }

    @Override
    public boolean checkTimeStart() {
        super.checkTimeStart();
        return false;
    }
}
