import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.practice.javanotes.manager.FileBackedTasksManager;
import ru.practice.javanotes.tasksProperties.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileBackedTasksManagerTest extends TaskManagerTest <FileBackedTasksManager> {
    File file = new File("src/main/resources/save.csv");
    Epic testEpic = new Epic("Test epic", "For test");
    Task testTask = new Task("Test task", "For test", Status.IN_PROGRESS, LocalDateTime.of(2022,1,1,1,1), Duration.ofMinutes(10));
    Subtask testSubtask = new Subtask("Test subtask", "For test", Status.IN_PROGRESS, 1, LocalDateTime.of(2022,1,1,1,1), Duration.ofMinutes(10));

    @BeforeEach
     void setUp() {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        taskManager = new FileBackedTasksManager(file);
        taskManager.createTask(testTask);
        taskManager.createEpic(testEpic);
        taskManager.createSubtask(testSubtask);
    }

    @AfterEach
    public void clearFile() {
        file.delete();
    }

    @Test
    public void save() {
        taskManager.save();
        assertNotNull (file, "File is not exist");
    }

    @Test
    public void loadFromFile() {
        taskManager.save();
        FileBackedTasksManager manager;
        try {
            manager = FileBackedTasksManager.loadFromFile(file);
        } catch (IOException e) {
            System.out.println("Test failed");
            return;
        }
        assertNotNull(manager, "Файл не сохранен");
        assertEquals(2, manager.getAll(TypeOfObj.TASK).size(), "Некорректно загружены задачи");
        assertEquals(2, manager.getAll(TypeOfObj.EPIC).size(), "Некорректно загружены задачи");
        assertEquals(1, manager.getAll(TypeOfObj.SUBTASK).size(),
                "Некорректно загружены подзадачи");
        assertEquals(1, manager.getEpicsSubtasks(2).size(),
                "Некорректно загружена связь эпика и подзадачи");
    }
}