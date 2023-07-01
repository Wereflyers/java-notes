

import com.google.gson.Gson;
import org.junit.Test;
import ru.practice.javanotes.manager.FileBackedTasksManager;
import ru.practice.javanotes.manager.InMemoryTaskManager;
import ru.practice.javanotes.manager.Managers;
import ru.practice.javanotes.manager.TaskManager;
import ru.practice.javanotes.server.KVServer;
import ru.practice.javanotes.tasksProperties.TypeOfObj;

import java.io.File;
import java.io.IOException;

public class HttpTaskServerTest {

    @Test
    public void setEndPoint() throws IOException {
        File file = new File("src/main/resources/save.csv");
        TaskManager taskManager = FileBackedTasksManager.loadFromFile(file);
        String path = " /tasks/task/";
        String whatToDo = "GET";

        String[] splitPath = path.split("/");
        String response;

        Gson gson = new Gson();

        switch (whatToDo) {
            case "GET":
                if (splitPath.length == 2) {
                    response = gson.toJson(InMemoryTaskManager.getPrioritizedTasks());
                    System.out.println(response);
                    break;
                }
                if (splitPath.length == 3) {
                    if (splitPath[2].equals("history")) {
                        response = gson.toJson(Managers.getDefaultHistory().getHistory().toString());
                        System.out.println(response);
                    } else {
                        try {
                            assert taskManager != null;
                            response = gson.toJson(taskManager.getAll(TypeOfObj.valueOf(splitPath[2].toUpperCase())));
                            System.out.println(response);
                        } catch (Exception e) {
                            break;
                        }
                    }
                    break;
                }
                if (splitPath[3].equals("epic")) {
                    assert taskManager != null;
                    response = gson.toJson(taskManager.getEpicsSubtasks(Integer.parseInt(splitPath[4])));
                    System.out.println(response);
                    break;
                }
                try {
                    int id = Integer.parseInt(splitPath[3]);
                    assert taskManager != null;
                    response = gson.toJson(taskManager.getTask(id));
                    System.out.println(response);
                } catch (NumberFormatException ignored) {
                }
                break;
            case "DELETE":
                if (splitPath.length == 3) {
                    taskManager.removeAll(TypeOfObj.valueOf(splitPath[2].toUpperCase()));
                    break;
                }
                try {
                    taskManager.removeTask(TypeOfObj.valueOf(splitPath[2].toUpperCase()), Integer.parseInt(splitPath[4]));
                } catch (NumberFormatException ignored) {
                }
                break;
            default:
                System.out.println("Wrong path");
        }
    }

    @Test
    public void launchAndCheckKVServer() {
        try {
            KVServer kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
