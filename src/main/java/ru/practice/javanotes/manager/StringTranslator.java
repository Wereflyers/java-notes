package ru.practice.javanotes.manager;

import ru.practice.javanotes.tasksProperties.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StringTranslator {

    public static String toString(Task task) {
        if (task.type == TypeOfObj.SUBTASK) {
            Subtask sub = (Subtask) task;
            return sub.SubtaskToString();
        }
        return task.toString();
    }

    public static Task fromString(String value) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        Status status = Status.valueOf(split[4]);
        LocalDateTime startTime = null;
        Duration duration = null;
        if (split.length > 5) {
            startTime = LocalDateTime.parse(split[5]);
            duration = Duration.ofSeconds(Long.parseLong(split[6]));
        }
        if (split[1].equals("SUBTASK")) {
            int epic = Integer.parseInt(split[7]);
            return new Subtask(split[2],split[3],id,status,epic,startTime,duration);
        } else if (split[1].equals("EPIC")) {
            return new Epic(split[2],split[3],id,status);
        }
        return new Task(split[2],split[3],id,status,startTime,duration);
    }

    public static String historyToString(HistoryManager manager) {
        if (manager.getHistory().isEmpty()) {
            return "\n";
        }
        StringBuilder history = null;
        for (Task task : manager.getHistory()) {
            if (history == null) {
                history = new StringBuilder(String.valueOf(task.getIdNumber()));
            } else history.append(",").append(task.getIdNumber());
        }
        return history != null ? history.toString() : null;
    }

    public static List<Integer> historyFromString(String value) {
        String[] historyInString = value.split(",");
        List<Integer> historyList = new ArrayList<>();
        for (String s : historyInString) {
            historyList.add(Integer.parseInt(s));
        }
        return historyList;
    }
}
