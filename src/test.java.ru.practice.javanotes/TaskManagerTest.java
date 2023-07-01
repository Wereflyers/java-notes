import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import ru.practice.javanotes.manager.TaskManager;
import ru.practice.javanotes.tasksProperties.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager> {
    T taskManager;
    Task testTask;
    Epic testEpic;
    Subtask testSubtask;

    @BeforeEach
    public void setTaskManager() {
        testEpic = new Epic("Test epic", "For test");
        testTask = new Task("Test task", "For test", Status.IN_PROGRESS, LocalDateTime.of(2022,1,1,1,1), Duration.ofMinutes(10));
        testSubtask = new Subtask("Test subtask", "For test", Status.IN_PROGRESS, 2, LocalDateTime.of(2022,1,1,1,1), Duration.ofMinutes(10));
        taskManager.createTask(testTask);
        taskManager.createEpic(testEpic);
        taskManager.createSubtask(testSubtask);
    }

    @Test
    public void createTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW, LocalDateTime.of(2022,1,1,1,1), Duration.ofMinutes(10));
        final Task savedTask = taskManager.createTask(task);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = taskManager.getAll(TypeOfObj.TASK);
        assertNotNull(tasks, "Задачи нe возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    public void createEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final Epic savedEpic = taskManager.createEpic(epic);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        final List<Task> epics = taskManager.getAll(TypeOfObj.EPIC);
        assertNotNull(epics, "Задачи нe возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(1), "Задачи не совпадают.");
    }

    @Test
    public void createSubtask() {
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                Status.IN_PROGRESS, 2, LocalDateTime.of(2022,1,1,1,1), Duration.ofMinutes(10));
        final Subtask savedSubtask = taskManager.createSubtask(subtask);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
        final List<Task> subtasks = taskManager.getAll(TypeOfObj.SUBTASK);
        assertNotNull(subtasks, "Задачи нe возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(1), "Задачи не совпадают.");
    }

    @Test
    public void updateTask() {
        Task task = new Task("Test NewTask", "Test description", 1, Status.NEW);
        taskManager.updateTask(task);
        final Task savedTask = taskManager.getTask(1);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = taskManager.getAll(TypeOfObj.TASK);
        assertNotNull(tasks, "Задачи нe возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void updateEpic() {
        Epic epic = new Epic("Test NewEpic", "Test description",2);
        taskManager.updateEpic(epic);
        final Epic savedEpic = (Epic) taskManager.getTask(2);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        final List<Task> epics = taskManager.getAll(TypeOfObj.EPIC);
        assertNotNull(epics, "Задачи нe возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void updateSubtask() {
        Subtask subtask = new Subtask("Test NewSubtask", "Test description", 3,
                Status.IN_PROGRESS,2, LocalDateTime.of(2022,1,1,1,1), Duration.ofMinutes(10));
        taskManager.updateSubtask(subtask);
        final Subtask savedSubtask = (Subtask) taskManager.getTask(3);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
        final List<Task> subtasks = taskManager.getAll(TypeOfObj.SUBTASK);
        assertNotNull(subtasks, "Задачи нe возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void getAll() {
        List<Task> tasks = List.of (testTask);
        List<Task> inMemoryTasks = taskManager.getAll(TypeOfObj.TASK);
        assertNotNull(inMemoryTasks, "Список не найден.");
        assertIterableEquals(tasks, inMemoryTasks, "Списки не совпадают.");

        List<Task> epics = List.of (testEpic);
        List<Task> inMemoryEpics = taskManager.getAll(TypeOfObj.EPIC);
        assertNotNull(inMemoryEpics, "Список не найден.");
        assertIterableEquals(epics, inMemoryEpics, "Списки не совпадают.");

        List<Task> subtasks = List.of (testSubtask);
        List<Task> inMemorySubtasks = taskManager.getAll(TypeOfObj.SUBTASK);
        assertNotNull(inMemorySubtasks, "Список не найден.");
        assertIterableEquals(subtasks, inMemorySubtasks, "Списки не совпадают.");
    }

    @Test
    public void getTask() {
        Task task = taskManager.getTask(1);
        assertNotNull(task, "Задача не найдена.");
        assertEquals(task, testTask, "Задачи не совпадают.");
    }

    @Test
    public void removeAll() {
        taskManager.removeAll(TypeOfObj.EPIC);
        assertNull(taskManager.getAll(TypeOfObj.EPIC), "Задачи не были удалены");
        assertNull(taskManager.getAll(TypeOfObj.SUBTASK), "Подзадачи эпиков не были удалены");
        assertNotNull(taskManager.getAll(TypeOfObj.TASK), "Был удален неверный тип задач");
    }

    @Test
    public void removeTask() {
        taskManager.removeTask(TypeOfObj.EPIC,2);
        assertNull(taskManager.getAll(TypeOfObj.EPIC), "Задача не были удалена");
        assertNull(taskManager.getEpicsSubtasks(2), "Подзадачи эпика не были удалены");
        assertNotNull(taskManager.getAll(TypeOfObj.TASK), "Был удален неверный тип задач");
    }
}