package ru.practice.javanotes.manager;



import ru.practice.javanotes.tasksProperties.Epic;
import ru.practice.javanotes.tasksProperties.Subtask;
import ru.practice.javanotes.tasksProperties.Task;
import ru.practice.javanotes.tasksProperties.TypeOfObj;

import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private static int id = 1; //переменная для присваивания задачам уникального номера
    final private static Map<Integer, Epic> epics = new HashMap<>();
    final private static Map<Integer, Task> tasks = new HashMap<>();
    final private static HistoryManager historyManager = Managers.getDefaultHistory();
    static Comparator<Task> c = (t1, t2) -> {
        if (t1.getStartTime() == null) {
            return 1;
        } else if (t2.getStartTime() == null) {
            return -1;
        } else if (t2.getStartTime().isBefore(t1.startTime)) {
            return -1;
        } else if (t1.getStartTime().isBefore(t2.getStartTime())) {
            return 1;
        }
        return 0;
    };
    static TreeSet<Task> prioritizedTasks = new TreeSet<>(c);

    @Override
    public Task createTask(Task task) {
        task.setIdNumber(id);
        tasks.put(id, task);
        id++;
        prioritizedTasks.add(task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.refreshStatus();
        epic.setIdNumber(id);
        epics.put(id, epic);
        id++;
        prioritizedTasks.add(epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicNumber());
        epic.addSubtask(id, subtask);
        epic.refreshStatus();
        epic.setTime();
        subtask.setIdNumber(id);
        id++;
        prioritizedTasks.add(subtask);
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        Task oldTask = tasks.get(task.getIdNumber());
        tasks.remove(task.getIdNumber());
        tasks.put(task.getIdNumber(),task);
        prioritizedTasks.remove(oldTask);
        prioritizedTasks.add(task);
    }

    @Override
    public void updateEpic(Epic newEpic) {
        int id = newEpic.getIdNumber();
        Epic oldEpic = epics.get(id);
        newEpic.setSubtasks(epics.get(id).getSubtasksMap());
        newEpic.refreshStatus();
        newEpic.setTime();
        epics.remove(id);
        epics.put(id, newEpic);
        prioritizedTasks.remove(oldEpic);
        prioritizedTasks.add(newEpic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicNumber());
        Subtask oldSubtask = epic.getSubtasks().get(subtask.getIdNumber());
        epic.removeSubtask(subtask.getIdNumber());
        epic.addSubtask(subtask.getIdNumber(), subtask);
        epic.refreshStatus();
        epic.setTime();
        prioritizedTasks.remove(oldSubtask);
        prioritizedTasks.add(subtask);
        updateEpic(epic);
    }

    @Override
    public List<Task> getAll(TypeOfObj typeOfObject) { //Получить список задач
        List<Task> listOfTasks = new ArrayList<>();
        switch (typeOfObject) {
            case TASK:
                listOfTasks.addAll(tasks.values());
                break;
            case EPIC:
                listOfTasks.addAll(epics.values());
                break;
            case SUBTASK:
                for (Epic epic : epics.values()) {
                    listOfTasks.addAll(epic.getSubtasks());
                }
        }
        return listOfTasks;
    }

    @Override
    public Task getTask(int idNumber) { //Получить задачу по номеру
        if (tasks.containsKey(idNumber)) {
            historyManager.add(tasks.get(idNumber));
            return tasks.get(idNumber);
        }
        if (epics.containsKey(idNumber)) {
            historyManager.add(epics.get(idNumber));
            return epics.get(idNumber);
        }
        for (Epic epic: epics.values()) {
            if (epic.isElementOfList(idNumber)) {
                historyManager.add(epic.getSubtask(idNumber));
                return epic.getSubtask(idNumber);
            }
        }
        return null;
    }

    @Override
    public void removeAll(TypeOfObj typeOfObject) { //удаление всех задач
        switch (typeOfObject) {
            case TASK:
                if (!tasks.isEmpty()) {
                    getAll(TypeOfObj.TASK).forEach(prioritizedTasks::remove);
                    tasks.clear();
                }
                break;
            case EPIC:
                if (!epics.isEmpty()) {
                    getAll(TypeOfObj.EPIC).forEach(prioritizedTasks::remove);
                    epics.clear();
                }
                break;
            case SUBTASK:
                getAll(TypeOfObj.SUBTASK).forEach(prioritizedTasks::remove);
                for (Epic epic: epics.values()) {
                    epic.removeSubtasks();
                    updateEpic(epic);
                }
        }
    }

    @Override
    public void removeTask(TypeOfObj typeOfObject, int idNumber) {
        switch(typeOfObject){
            case TASK:
                if(tasks.containsKey(idNumber)) {
                    prioritizedTasks.remove(tasks.get(idNumber));
                    tasks.remove(idNumber);
                }
                break;
            case EPIC:
                if(epics.containsKey(idNumber)) {
                    if(epics.get(idNumber).getSubtasksMap().size() > 0) {
                        for (Subtask subtask: epics.get(idNumber).getSubtasksMap().values()) {
                            prioritizedTasks.remove(subtask);
                            historyManager.remove(subtask.getIdNumber());
                        }
                        epics.get(idNumber).removeSubtasks();
                    }
                    prioritizedTasks.remove(epics.get(idNumber));
                    epics.remove(idNumber);
                }
                break;
            case SUBTASK:
                for(Epic epic: epics.values()) {
                    if (epic.isElementOfList(idNumber)) {
                        prioritizedTasks.remove(epic.getSubtask(idNumber));
                        epic.removeSubtask(idNumber);
                        updateEpic(epics.get(idNumber));
                    }
                }
        }
        historyManager.remove(idNumber);
    }

    @Override
    public List<Subtask> getEpicsSubtasks(int epicNumber) { //получение всех подзадач определенного эпика
        return epics.get(epicNumber).getSubtasks();
    }

    public static List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public static List getPrioritizedTasks() {
        return List.of(prioritizedTasks);
    }
}
