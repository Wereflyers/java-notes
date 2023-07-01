package ru.practice.javanotes.manager;

import ru.practice.javanotes.tasksProperties.Epic;
import ru.practice.javanotes.tasksProperties.Subtask;
import ru.practice.javanotes.tasksProperties.Task;
import ru.practice.javanotes.tasksProperties.TypeOfObj;

import java.util.List;

public interface TaskManager {
    Task createTask(Task task);
    Epic createEpic(Epic epic);
    Subtask createSubtask(Subtask subtask);
    void updateTask(Task task);
    void updateEpic(Epic newEpic);
    void updateSubtask(Subtask subtask);
    List<Task> getAll(TypeOfObj typeOfObject);
    Task getTask(int idNumber);
    void removeAll(TypeOfObj typeOfObject);
    void removeTask(TypeOfObj typeOfObject, int idNumber);
    List<Subtask> getEpicsSubtasks(int epicNumber);
}

