package ru.practice.javanotes.manager;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.practice.javanotes.server.KVTaskClient;
import ru.practice.javanotes.tasksProperties.*;

import java.time.LocalDateTime;

public class HttpTaskManager extends FileBackedTasksManager {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new TimeAdapter())
            .create();
    public static KVTaskClient kvTaskClient;
    protected String path;

    public HttpTaskManager(String path) {
        this.path = path;
    }

    public void getToken() {
        kvTaskClient = new KVTaskClient(path);
        kvTaskClient.register();
    }

    @Override
    public void save() {
        if (kvTaskClient == null) {
            System.out.println("Требуется регистрация");
            return;
        }
        kvTaskClient.put("/tasks", gson.toJson(getAll(TypeOfObj.TASK)));
        kvTaskClient.put("/epics", gson.toJson(getAll(TypeOfObj.EPIC)));
        kvTaskClient.put("/subtasks", gson.toJson(getAll(TypeOfObj.SUBTASK)));
        kvTaskClient.put("/history", gson.toJson(getHistory()));
    }

    public void loadTasks() {
        if (kvTaskClient == null) {
            System.out.println("Nothing to load");
            return;
        }
        String json = kvTaskClient.load("/tasks");
        String[] split = json.split("/n?/r");
        for (int i = 0; i < split.length; i++) {
            createTask(gson.fromJson(json, Task.class));
        }

        json = kvTaskClient.load("/epics");
        split = json.split("/n?/r");
        for (int i = 0; i < split.length; i++) {
            createEpic(gson.fromJson(json, Epic.class));
        }

        json = kvTaskClient.load("/subtasks");
        split = json.split("/n?/r");
        for (int i = 0; i < split.length; i++) {
            createSubtask(gson.fromJson(json, Subtask.class));
        }

        json = kvTaskClient.load("/history");
        String historyLine = json.substring(1, json.length() - 1);
        if (!historyLine.equals("/r?/n")) {
            String[] historyLineContents = historyLine.split(",");
            CustomLinkedList history = new CustomLinkedList();
            for (String s : historyLineContents) {
                history.linkLast(getTask(Integer.parseInt(s)));
            }
            InMemoryHistoryManager.setHistory(history);
        }
        save();
    }
}
