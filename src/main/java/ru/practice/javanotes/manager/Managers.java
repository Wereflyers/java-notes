package ru.practice.javanotes.manager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTasksManager getDefault(File file) {
        return new FileBackedTasksManager(file);
    }

    public static HttpTaskManager getDefault(String url) {
        return new HttpTaskManager(url);
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
