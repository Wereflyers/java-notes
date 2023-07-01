package ru.practice.javanotes.manager;

import ru.practice.javanotes.exceptions.ManagerSaveException;
import ru.practice.javanotes.tasksProperties.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file = null;
    private static int idNumber;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public FileBackedTasksManager() {
    }

    public void save() {
        //save in file
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("id,type,name,status,description,startTime,duration,epic\n");
            for(Task task:Managers.getDefault().getAll(TypeOfObj.TASK)) {
                bw.write(StringTranslator.toString(task)+"\n");
            }
            for(Task task:Managers.getDefault().getAll(TypeOfObj.EPIC)) {
                bw.write(StringTranslator.toString(task)+"\n");
            }
            for(Task task:Managers.getDefault().getAll(TypeOfObj.SUBTASK)) {
                bw.write(StringTranslator.toString(task)+"\n");
            }
            bw.write("\n");
            bw.write(Objects.requireNonNull(StringTranslator.historyToString(Managers.getDefaultHistory())));
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(file);
        String csv = Files.readString(Path.of(file.getPath()));
        String[] lines = csv.split("\r?\n");
        if (lines.length <= 1) {
            return null;
        }
        for (int i = 1; i < lines.length; i++) {
            if(lines[i].isBlank()) {
                break;
            }
            Task task = StringTranslator.fromString(lines[i]);
            switch (task.type) {
                case TASK:
                    tasksManager.createTask(task,true);
                break;
                case SUBTASK:
                    tasksManager.createSubtask((Subtask)task,true);
                    break;
                case EPIC:
                    tasksManager.createEpic((Epic)task,true);
            }
        }
        if (lines[lines.length-2].isBlank()) {
            List<Integer> historyFromFile = StringTranslator.historyFromString(lines[lines.length - 1]);
            for (int id : historyFromFile) {
                Managers.getDefault().getTask(id);
            }
        }
        return tasksManager;
    }

    public Task createTask(Task task, Boolean isFromFile) {
        idNumber = task.getIdNumber();
        Task newTask = super.createTask(task);
        if(!isFromFile) {
            save();
        } else {
            task.setIdNumber(idNumber);
        }
        return newTask;
    }

    public Epic createEpic(Epic epic, Boolean isFromFile) {
        idNumber = epic.getIdNumber();
        Epic newEpic = super.createEpic(epic);
        if(!isFromFile) {
            save();
        } else {
            epic.setIdNumber(idNumber);
        }
        return newEpic;
    }

    public Subtask createSubtask(Subtask subtask, Boolean isFromFile) {
        idNumber = subtask.getIdNumber();
        if(!isFromFile){
            save();
        } else {
            subtask.setIdNumber(idNumber);
        }
        return subtask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask newSubtask = super.createSubtask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public List<Task> getAll(TypeOfObj typeOfObject) {
        List<Task> allTasks = super.getAll(typeOfObject);
        save();
        return allTasks;
    }

    @Override
    public Task getTask(int idNumber) {
        Task task = super.getTask(idNumber);
        save();
        return task;
    }

    @Override
    public void removeAll(TypeOfObj typeOfObject) {
        super.removeAll(typeOfObject);
        save();
    }

    @Override
    public void removeTask(TypeOfObj typeOfObject, int idNumber) {
        super.removeTask(typeOfObject, idNumber);
        save();
    }

    @Override
    public List<Subtask> getEpicsSubtasks(int epicNumber) {
        return super.getEpicsSubtasks(epicNumber);
    }

    public static void main(String[] args) {
        File file = new File("src/main/resources/save.csv");
        try {
            if(file.exists()) {
                System.out.println(Managers.getDefault(file).getTask(2).toString());
                System.out.println(Managers.getDefaultHistory().getHistory().toString());
            } else if (file.createNewFile()) {
                Managers.getDefault().createTask(new Task("Мусор", "Выбросить мусор",
                        Status.IN_PROGRESS, LocalDateTime.now(),Duration.ofMinutes(5)));
                Managers.getDefault(file).createEpic(new Epic("Переезд",
                        "Переехать в новую квартиру"));
                Managers.getDefault(file).createSubtask(new Subtask("Работа", "Сходить на собеседование", Status.NEW, 2,
                        LocalDateTime.of(2022,12,1,15,0), Duration.of(1, ChronoUnit.HOURS)));
                System.out.println(Managers.getDefault(file).getTask(1).toString());
                System.out.println(Managers.getDefault(file).getTask(2).toString());
                System.out.println(Managers.getDefault(file).getTask(3));
                System.out.println(Managers.getDefaultHistory().getHistory().toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
