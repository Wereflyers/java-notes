package ru.practice.javanotes.tasksProperties;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
       private int epicNumber;

    public Subtask(String name, String description, Status status, int epicNumber, LocalDateTime startTime,
                   Duration duration) {
        super(name, description, status, startTime, duration);
        this.epicNumber = epicNumber;
        type = TypeOfObj.SUBTASK;
    }

    public Subtask(String name, String description, int idNumber, Status status, int epicNumber,
                   LocalDateTime startTime, Duration duration) {
        super(name, description, idNumber, status, startTime, duration);
        this.epicNumber = epicNumber;
        type = TypeOfObj.SUBTASK;
    }

    public int getEpicNumber() {
        return epicNumber;
    }

    public String SubtaskToString() {
        return this.toString()+this.epicNumber;
    }
}
