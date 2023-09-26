package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.exception.ManagerSaveException;
import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;
import ru.practicum.task_tracker.task.TaskType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
                Task newEpic = new Epic(task.getName(), task.getDescription(), task.getStatus());
                newEpic.setId(task.getId());
                super.createEpic((Epic) newEpic);
            } else if (task.getTaskType().equals(TaskType.TASK)) {
                Task newTask = new Task(task.getName(), task.getDescription(), task.getStatus());
                newTask.setId(task.getId());
                super.createTask(newTask);
            } else if (task.getTaskType().equals(TaskType.SUBTASK)) {
                Subtask subtask = (Subtask) task;
                Subtask newSubtask = new Subtask(task.getName(), task.getDescription(), task.getStatus(),
                        subtask.getEpicId());
                newSubtask.setId(task.getId());
                super.addNewSubtask(newSubtask);
            }
        }
    }




    public void save(){
        try (Writer fileWriter = new FileWriter(file)) {
            for(Task task : getTasks().values()){
                fileWriter.write(CSVFormatter.toString(task) + "\n");
            }

            for(Epic epic : getEpics().values()){
                fileWriter.write(CSVFormatter.toString(epic) + "\n");
            }

            for(Subtask subtask : getSubtasks().values()){
                fileWriter.write(CSVFormatter.toString(subtask) + "\n");
            }
           if(super.getHistoryManager().getHistory().size() != 0) {
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
    public void updateEpicInfo(Epic epic, Long id) {
        super.updateEpicInfo(epic, id);
        save();
    }

    @Override
    public void updateSubtaskInfo(Subtask subtask, Long id) {
        super.updateSubtaskInfo(subtask, id);
        save();
    }

    @Override
    public void updateTaskInfo(Task task, Long id) {
        super.updateTaskInfo(task, id);
        save();
    }

    @Override
    public void deleteSubtaskById(Long subtaskId) {
        super.deleteSubtaskById(subtaskId);
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

}
