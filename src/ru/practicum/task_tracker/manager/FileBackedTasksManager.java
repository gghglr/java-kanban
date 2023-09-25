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
                    sortTasks();
                    List<Integer> historyInt = CSVFormatter.historyFromString(reader.readLine());
                    for (Long i = 0L; i < historyInt.size(); i++) {
                        if (super.getEpic(i) != null) {
                            Epic epic = super.getEpic(i);
                        } else if (super.getTask(i) != null) {
                            Task task = super.getTask(i);
                        } else if (super.getSubtask(i) != null) {
                            Subtask subtask = super.getSubtask(i);
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
                Subtask newSubtask = new Subtask(task.getName(), task.getDescription(), task.getStatus(), subtask.getEpicId());
                newSubtask.setId(task.getId());
                super.addNewSubtask(newSubtask);
            }
        }
    }




    public void save(){

        try (Writer fileWriter = new FileWriter(file, false)) {
            for(Task task : getTasks().values()){
                fileWriter.write(CSVFormatter.toString(task) + "\n");
            }

            for(Epic epic : getEpics().values()){
                fileWriter.write(CSVFormatter.toString(epic) + "\n");
            }

            for(Subtask subtask : getSubtasks().values()){
                fileWriter.write(CSVFormatter.toString(subtask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }

    }
    @Override
    public Task getTask(Long id) {
        Task task = super.getTask(id);
        return task;
    }

    @Override
    public Subtask getSubtask(Long id) {
        Subtask subtask = super.getSubtask(id);
        return subtask;
    }

    @Override
    public Epic getEpic(Long id) {
        Epic epic = super.getEpic(id);
        return epic;
    }

    @Override
    public boolean equals(Object obj) {
        FileBackedTasksManager o = (FileBackedTasksManager) obj;
        boolean tasksEquals = getTasks().equals(o.getTasks());
        return tasksEquals;
    }

    public void saveHistory(File file) {
        try (Writer fileWriter = new FileWriter(file, true);) {
            fileWriter.write("\n" + CSVFormatter.historyToString(super.getHistoryManager()) + "\n");
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }
}
