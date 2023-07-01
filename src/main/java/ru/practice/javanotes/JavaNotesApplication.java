package ru.practice.javanotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.practice.javanotes.manager.HttpTaskManager;
import ru.practice.javanotes.manager.Managers;
import ru.practice.javanotes.server.KVServer;
import ru.practice.javanotes.tasksProperties.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class JavaNotesApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(JavaNotesApplication.class, args);
		Epic testEpic = new Epic("Test epic", "For test");
		Task testTask = new Task("Test task", "For test", Status.IN_PROGRESS, LocalDateTime.of(2022, 1, 1, 1, 1), Duration.ofMinutes(10));
		Subtask testSubtask = new Subtask("Test subtask", "For test", Status.IN_PROGRESS, 2, LocalDateTime.of(2022, 1, 1, 1, 1), Duration.ofMinutes(10));
		HttpTaskManager newManager = Managers.getDefault("http://localhost:8078");
		newManager.getToken();
		KVServer server = new KVServer();
		server.start();
		newManager.createEpic(testEpic);
		newManager.createTask(testTask);
		newManager.getAll(TypeOfObj.TASK);
		newManager.save();
		server.stop();
	}
}
