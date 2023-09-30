package ru.practicum.task_tracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskTrackerTest{
    private File file = new File("text.txt");
    private File fileEmpty = new File("textEmpty.txt");//файл, где нет истории просмотра, задач и подзадач

    private FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
    FileBackedTasksManager emptyText = new FileBackedTasksManager(fileEmpty);
    @BeforeEach
    public void init(){
        setUp();
    }

    @Test
    public void fileBackTest(){
        fileBackedTasksManager.loadFromFile();
        //проверим размеры мап после загрузки из файла, если размер мап = количеству тасок в файле, то все ок
        //метод save() тоже проверяется

        assertEquals(1, fileBackedTasksManager.getTasks().size());
        assertEquals(2, fileBackedTasksManager.getEpics().size());
        assertEquals(4, fileBackedTasksManager.getSubtasks().size());

        taskTracker.getTask(taskId);
        taskTracker.getSubtask(subtaskId1);
        fileBackedTasksManager.removeHistoryTask(taskId);

        assertEquals(1, fileBackedTasksManager.getHistoryManager().getHistory().size());
    }

    @Test
    public void fileBackTestWithEmptyFile(){

        emptyText.loadFromFile();

        assertEquals(new ArrayList<>(), emptyText.getTasks());
        assertEquals(1, emptyText.getEpics().size());
        assertEquals(new ArrayList<>(), emptyText.getSubtasks());
        assertEquals(0, emptyText.getHistoryManager().getHistory().size());
    }

}