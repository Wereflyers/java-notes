import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.practice.javanotes.manager.HttpTaskManager;
import ru.practice.javanotes.manager.Managers;
import ru.practice.javanotes.server.KVServer;
import ru.practice.javanotes.tasksProperties.Epic;
import ru.practice.javanotes.tasksProperties.Status;
import ru.practice.javanotes.tasksProperties.Subtask;
import ru.practice.javanotes.tasksProperties.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class KVServerTest {
    static HttpTaskManager taskManager = Managers.getDefault("http://localhost:8078");
    static Epic epic = new Epic("Test epic", "For test");
    static Subtask subtask = new Subtask("Test subtask", "For test", Status.IN_PROGRESS, 1, LocalDateTime.now(), Duration.ofMinutes(10));
    static Task task = new Task("Test task", "For test", Status.IN_PROGRESS, LocalDateTime.now(),Duration.ofMinutes(5));

    @BeforeAll
    public static void getTasksLoaded() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.createTask(task);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);
    }

    @BeforeEach
    public void start() throws IOException {
        KVServer kvserver = new KVServer();
        kvserver.start();
    }

    @Test
    public void checkRegistration() throws IOException {
        taskManager.loadTasks();
        taskManager.save();
        HttpTaskManager manager = Managers.getDefault("http://localhost:8078");
        manager.loadTasks();
        manager.getToken();
    }
}
