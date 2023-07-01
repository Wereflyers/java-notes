import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import ru.practice.javanotes.manager.Managers;
import ru.practice.javanotes.tasksProperties.Epic;
import ru.practice.javanotes.tasksProperties.Status;
import ru.practice.javanotes.tasksProperties.Subtask;
import ru.practice.javanotes.tasksProperties.TypeOfObj;

import java.time.Duration;
import java.time.LocalDateTime;

public class EpicTest {

    @BeforeAll
    public static void createEpicForTest() {
        Managers.getDefault().createEpic(new Epic("New epic", "For test", 1));
    }

    @AfterEach
    public void removeSubtasksAfterTest() {
        Managers.getDefault().removeAll(TypeOfObj.SUBTASK);
    }

    @Test
    public void statusEpicWithoutSubtasks() {
        Assertions.assertEquals(Status.NEW, Managers.getDefault().getTask(1).getStatus());
    }

    @Test
    public void statusEpicWithNewSubtasks() {
        Managers.getDefault().createSubtask(new Subtask("New subtask", "For test",
                Status.NEW, 1, LocalDateTime.of(2022,1,1,1,1), Duration.ofMinutes(10)));
        Assertions.assertEquals(Status.NEW, Managers.getDefault().getTask(1).getStatus());
    }

    @Test
    public void statusEpicWithDoneSubtasks() {
        Managers.getDefault().createSubtask(new Subtask("Done subtask", "For test",
                Status.DONE, 1, LocalDateTime.of(2022,2,2,2,2), Duration.ofHours(1)));
        Assertions.assertEquals(Status.DONE, Managers.getDefault().getTask(1).getStatus());
    }

    @Test
    public void statusEpicWithNewAndDoneSubtasks() {
        Managers.getDefault().createSubtask(new Subtask("New subtask", "For test",
                Status.NEW, 1, LocalDateTime.of(2022,1,1,1,1), Duration.ofMinutes(10)));
        Managers.getDefault().createSubtask(new Subtask("Done subtask", "For test",
                Status.DONE, 1, LocalDateTime.of(2022,1,1,1,1), Duration.ofMinutes(10)));
        Assertions.assertEquals(Status.IN_PROGRESS, Managers.getDefault().getTask(1).getStatus());
    }

    @Test
    public void statusEpicWithInProgressSubtasks() {
        Managers.getDefault().createSubtask(new Subtask("In progress subtask", "For test",
                Status.IN_PROGRESS, 1, LocalDateTime.of(2022,1,1,1,1), Duration.ofMinutes(10)));
        Assertions.assertEquals(Status.IN_PROGRESS, Managers.getDefault().getTask(1).getStatus());
    }

    @Test
    public void refreshEpicTime() {
        Managers.getDefault().createSubtask(new Subtask("In progress subtask", "For test",
                Status.IN_PROGRESS, 1, LocalDateTime.of(2022,1,1,1,1), Duration.ofMinutes(10)));
        Assertions.assertEquals(Duration.ofMinutes(10), Managers.getDefault().getTask(1).getDuration(),
                "Неверный расчет длительности выполнения эпика");
        Assertions.assertEquals(LocalDateTime.of(2022,1,1,1,1),
                Managers.getDefault().getTask(1).getStartTime(), "Неверный расчет старта выполнения эпика");
    }

}