package ru.practice.javanotes.tasksProperties;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Epic extends Task {
    private Map<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String name, String description) {
        super(name, description);
        type = TypeOfObj.EPIC;
        refreshStatus();
        setTime();
    }

    public Epic(String name, String description, int idNumber) {
        super(name, description,idNumber);
        type = TypeOfObj.EPIC;
        setTime();
    }

    public Epic(String name, String description, int idNumber, Status status) {
        super(name, description, idNumber, status);
        type = TypeOfObj.EPIC;
        setTime();
    }

    public void addSubtask(int idNumber, Subtask subtask) {
        subtasks.put(idNumber,subtask);
    }

    public void setTime () {
        if (!subtasks.isEmpty()) {
            startTime = LocalDateTime.MAX;
            duration = Duration.ZERO;
            for (Subtask subtask: subtasks.values()) {
                if (subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }
                duration = duration.plus(subtask.getDuration());
            }
            endTime = startTime.plus(duration);
        }
    }

    public Boolean isElementOfList(int idNumber) { // проверяем, есть ли сабтаск в этом эпике
        return subtasks.containsKey(idNumber);
    }

    public Task getSubtask(int idNumber) { //Получение подзадачи по номеру
        return subtasks.get(idNumber);
    }

    public List<Subtask> getSubtasks() { //Получение списка подзадач
        List<Subtask> list = new ArrayList<>();
        for (Subtask subtask:subtasks.values()) {
            list.add(subtask);
        }
        return list;
    }

    public Map<Integer, Subtask> getSubtasksMap() { //Получение карты подзадач
        return subtasks;
    }

    public void setSubtasks(Map<Integer, Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void removeSubtask(int idNumber) {
        subtasks.remove(idNumber);
    }

    public void removeSubtasks() {
        if (!subtasks.isEmpty()) {
            subtasks.clear();
        }
    }

    public void refreshStatus() {
        int doneSubtasks = 0; //переменные для подсчета количества сделанных и новых подзадач
        int newSubtasks = 0;
        if (subtasks.isEmpty()) {
            setStatus(Status.NEW);
            return;
        }
        for (Subtask subtask : subtasks.values()) {
            switch (subtask.getStatus()) {
                case DONE:
                    doneSubtasks++;
                    break;
                case NEW:
                    newSubtasks++;
            }
        }
        if (doneSubtasks == subtasks.size()) {
                setStatus(Status.DONE);
        } else if (newSubtasks == subtasks.size()) {
                setStatus(Status.NEW);
        } else {
                setStatus(Status.IN_PROGRESS);
        }
    }
}
