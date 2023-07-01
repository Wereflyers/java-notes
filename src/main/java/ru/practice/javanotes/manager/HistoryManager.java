package ru.practice.javanotes.manager;

import ru.practice.javanotes.tasksProperties.Task;

import java.util.List;

public interface HistoryManager {
    List<Task> getHistory();
    void add(Task task);
    void remove(int id);
    void clearHistory();
}
