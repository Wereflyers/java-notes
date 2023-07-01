package ru.practice.javanotes.manager;


import ru.practice.javanotes.tasksProperties.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static CustomLinkedList history = new CustomLinkedList();

    @Override
    public void add(Task task) {
            history.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
            return history.getTasks();
    }

    @Override
    public void remove(int id){
        history.remove(id);
    }

    public static void setHistory(CustomLinkedList history) {
        InMemoryHistoryManager.history = history;
    }

    public void clearHistory() {
        history.clear();
    }
}

