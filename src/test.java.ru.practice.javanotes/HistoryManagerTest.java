import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import ru.practice.javanotes.manager.HistoryManager;
import ru.practice.javanotes.manager.InMemoryHistoryManager;
import ru.practice.javanotes.tasksProperties.Epic;
import ru.practice.javanotes.tasksProperties.Status;
import ru.practice.javanotes.tasksProperties.Subtask;
import ru.practice.javanotes.tasksProperties.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    static HistoryManager historyManager = new InMemoryHistoryManager();
    Subtask subtask = new Subtask("Test subtask", "For test", 1, Status.IN_PROGRESS, 3, LocalDateTime.now(), Duration.ofMinutes(10));
    Task task = new Task("Test task", "For test", 2, Status.IN_PROGRESS);
    Epic epic = new Epic("Test epic", "For test", 3, Status.DONE);

    @AfterEach
    public void removeAllHistory() {
        historyManager.clearHistory();
    }

    @Test
    public void add() {
        fillTheHistory();
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(3, history.size(), "История не пустая.");
    }

    @Test
    public void getNullHistory() {
        assertEquals("[]", historyManager.getHistory().toString(), "История пустая");
    }

    @Test
    public void removeLast() {
        fillTheHistory();
        historyManager.remove(3);
        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        List<Task> exampleHistory = Arrays.asList(subtask,task);
        assertIterableEquals(history, exampleHistory, "Успешно удалено");
    }

    @Test
    public void removeFirst() {
        fillTheHistory();
        historyManager.remove(1);
        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        List<Task> exampleHistory = Arrays.asList(task,epic);
        assertIterableEquals(history, exampleHistory, "Успешно удалено");
    }

    @Test
    public void removeMiddle() {
        fillTheHistory();
        historyManager.remove(2);
        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        List<Task> exampleHistory = Arrays.asList(subtask,epic);
        assertIterableEquals(history, exampleHistory, "Успешно удалено");
    }
    
    @Test
    public void addDoubles() {
        fillTheHistory();
        historyManager.add(subtask);
        final List<Task> history = historyManager.getHistory();
        assertEquals(history.size(),3);
    }

    private void fillTheHistory() {
        historyManager.add(subtask);
        historyManager.add(task);
        historyManager.add(epic);
    }
}