package ru.practice.javanotes.tasksProperties;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskAdapter {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Task getTaskFromJson(String gsonString) {
        JsonElement jsonElement = JsonParser.parseString(gsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int id = jsonObject.get("id").getAsInt();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        Status status = Status.valueOf(jsonObject.get("status").getAsString().toUpperCase());
        Duration duration = Duration.ofMinutes(jsonObject.get("duration").getAsLong());
        String time = jsonObject.get("time").getAsString();
        LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
        return new Task(name, description, id, status,dateTime, duration);
    }

    public static Epic getEpicFromJson(String gsonString) {
        JsonElement jsonElement = JsonParser.parseString(gsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        return new Epic(name, description);
    }

    public static Subtask getSubtaskFromJson(String gsonString) {
        JsonElement jsonElement = JsonParser.parseString(gsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int id = jsonObject.get("id").getAsInt();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        Status status = Status.valueOf(jsonObject.get("status").getAsString().toUpperCase());
        int epicId = jsonObject.get("epicId").getAsInt();
        Duration duration = Duration.ofMinutes(jsonObject.get("duration").getAsLong());
        String time = jsonObject.get("time").getAsString();
        LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
        return new Subtask(name, description, id, status, epicId,dateTime, duration);
    }
}
