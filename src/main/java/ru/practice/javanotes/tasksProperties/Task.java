package ru.practice.javanotes.tasksProperties;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private final String name;
    private final String description;
    private int idNumber;
    private Status status;
    public TypeOfObj type;
    public Duration duration;
    public LocalDateTime startTime;
    public LocalDateTime endTime;

    public Task(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        type = TypeOfObj.TASK;
        endTime = startTime.plus(duration);
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        type = TypeOfObj.TASK;
    }

    public Task(String name, String description, int idNumber, Status status, LocalDateTime startTime,
                Duration duration) {
        this.name = name;
        this.description = description;
        this.idNumber = idNumber;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        type = TypeOfObj.TASK;
        endTime = startTime.plus(duration);
    }

    public Task(String name, String description, int idNumber, Status status) {
        this.name = name;
        this.description = description;
        this.idNumber = idNumber;
        this.status = status;
        type = TypeOfObj.TASK;
    }

    public Task(String name, String description, int idNumber) {
        this.name = name;
        this.description = description;
        this.idNumber = idNumber;
        type = TypeOfObj.TASK;
    }

    @Override
    public String toString() {
        if(duration!=null) {
            return idNumber + "," + type + "," + name + "," + description + "," + status + "," + startTime + "," + duration.toSeconds() + ",";
        }
        return idNumber + "," + type + "," + name + "," + description + "," + status + ",";
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
            this.idNumber=idNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
