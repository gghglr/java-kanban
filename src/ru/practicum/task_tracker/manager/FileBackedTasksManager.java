package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;
import ru.practicum.task_tracker.task.TaskType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{
    File file;
    public FileBackedTasksManager(File file) {
        this.file = file;
    }
    private static List<String> lines = new ArrayList<>();

    public void loadFromFile(){
        try(Reader fileReader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(fileReader);
            while (br.ready()){
                String line = br.readLine();
                if(!line.isEmpty()){
                    lines.add(line);
                }
                else if(line.isEmpty()){
                    sortTasks();
                    List<Integer> historyInt = CSVFormatter.historyFromString(br.readLine());
                    for(Long i = 0L; i < historyInt.size(); i++){
                        if(super.getEpic(i) != null){
                            Epic epic = super.getEpic(i);
                        }else if(super.getTask(i) != null){
                            Task task = super.getTask(i);
                        } else if (super.getSubtask(i) != null) {
                            Subtask subtask = super.getSubtask(i);
                        }
                    }
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sortTasks() throws IOException {
        for(int i = 0; i < lines.size(); i++){
            String s = lines.get(i);
            Task task = CSVFormatter.fromString(s);
            if(task.getTaskType().equals(TaskType.EPIC)){
                Task newEpic = new Epic(task.getName(), task.getDescription(), task.getStatus());
                newEpic.setId(task.getId());
                super.createEpic((Epic) newEpic);
                //super.reCreateEpic((Epic) task);
            }
            else if(task.getTaskType().equals(TaskType.TASK)){
                Task newTask = new Task(task.getName(), task.getDescription(), task.getStatus());
                newTask.setId(task.getId());
                super.createTask(newTask);
                //super.reCreateTask(task);
            }else if(task.getTaskType().equals(TaskType.SUBTASK)){
                Subtask newSubtask = new Subtask(task.getName(), task.getDescription(), task.getStatus(), task.getEpicId());
                newSubtask.setId(task.getId());
                super.addNewSubtask((Subtask) task);
            }
        }
    }

    public static List<String> getLines() {
        return lines;
    }


    @Override
    public long createEpic(Epic epic) throws IOException {
        long id = super.createEpic(epic);
        save(epic);
        return id;
    }

    @Override
    public long createTask(Task task) throws IOException {
        long id = super.createTask(task);
        save(task);
        return id;
    }

    @Override
    public Long addNewSubtask(Subtask subtask) throws IOException {
        Long id = super.addNewSubtask(subtask);
        saveSubtask(subtask);
        return id;
    }

    private void save(Task task) throws IOException {
        try(Writer fileWriter = new FileWriter(file, true);) {
            fileWriter.write(CSVFormatter.toString(task) + "\n");

        }
        catch (FileNotFoundException e){
            System.out.println("Поймано исключение при охраниении в файл");
        }

    }

    private void saveSubtask(Subtask subtask) throws IOException{
        try(Writer fileWriter = new FileWriter(file, true);) {
            fileWriter.write(CSVFormatter.toString(subtask) + "\n");
        }
        catch (FileNotFoundException e){
            System.out.println("Поймано исключение при сохраниении в файл");
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
        //проверить таски
        boolean tasksEquals = getTasks().equals(o.getTasks());
        return tasksEquals;
    }
}
