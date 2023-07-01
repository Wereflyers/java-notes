package ru.practice.javanotes.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.practice.javanotes.manager.FileBackedTasksManager;
import ru.practice.javanotes.manager.InMemoryTaskManager;
import ru.practice.javanotes.manager.Managers;
import ru.practice.javanotes.manager.TaskManager;
import ru.practice.javanotes.tasksProperties.TaskAdapter;
import ru.practice.javanotes.tasksProperties.TimeAdapter;
import ru.practice.javanotes.tasksProperties.TypeOfObj;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new TimeAdapter()).create();
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private static TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress("localhost",8080), 0);
        httpServer.createContext("/tasks", this::handle);
        HttpTaskServer.taskManager = taskManager;

    }

    public static void main(String[] args) throws IOException {
        File file = new File("src/main/resources/save.csv");
        FileBackedTasksManager.loadFromFile(file);
        HttpTaskServer httpTaskServer = new HttpTaskServer(Managers.getDefault(file));
        httpTaskServer.start();
    }

    private void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = null;
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");

        switch (method) {
            case "GET":
                if (splitPath.length == 2) {
                    response = gson.toJson(InMemoryTaskManager.getPrioritizedTasks());
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    } catch (NullPointerException | IOException e) {
                        httpExchange.sendResponseHeaders(404, 0);
                    }
                    break;
                }
                    if (splitPath.length == 3) {
                        if (splitPath[2].equals("history"))
                            response = gson.toJson(Managers.getDefaultHistory().getHistory().toString());
                        else {
                            response = gson.toJson(taskManager.getAll(TypeOfObj.valueOf(splitPath[2].toUpperCase())));
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(response.getBytes());
                            } catch (NullPointerException | IOException e) {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        }
                        break;
                    }
                    if (splitPath[3].equals("epic")) {
                        response = gson.toJson(taskManager.getEpicsSubtasks(Integer.parseInt((String) httpExchange.getAttribute("id"))));
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        } catch (NullPointerException | IOException e) {
                            httpExchange.sendResponseHeaders(404, -1);
                        }
                        break;
                    }
                    try {
                        int id = Integer.parseInt((String) httpExchange.getAttribute("id"));
                        response = gson.toJson(taskManager.getTask(id));
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        } catch (NullPointerException | IOException e) {
                            httpExchange.sendResponseHeaders(404, -1);
                        }
                    } catch (NumberFormatException e) {
                        httpExchange.sendResponseHeaders(404, -1);
                    }
                    break;
                case "DELETE":
                    if (splitPath.length == 3) {
                        taskManager.removeAll(TypeOfObj.valueOf(splitPath[2].toUpperCase()));
                        httpExchange.sendResponseHeaders(200, -1);
                        break;
                    }
                    try {
                        taskManager.removeTask(TypeOfObj.valueOf(splitPath[2].toUpperCase()), Integer.parseInt((String) httpExchange.getAttribute("id")));
                        httpExchange.sendResponseHeaders(200, -1);
                    } catch (NumberFormatException e) {
                        httpExchange.sendResponseHeaders(404, -1);
                    }
                    break;
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes());
                    TypeOfObj taskType = TypeOfObj.valueOf(splitPath[2].toUpperCase());
                    if (taskType == TypeOfObj.EPIC) {
                        response = taskManager.createEpic(TaskAdapter.getEpicFromJson(body)).toString();
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        } catch (NullPointerException | IOException e) {
                            httpExchange.sendResponseHeaders(404, -1);
                        }
                    }
                    if (taskType == TypeOfObj.SUBTASK) {
                        response = taskManager.createSubtask(TaskAdapter.getSubtaskFromJson(body)).toString();
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        } catch (NullPointerException | IOException e) {
                            httpExchange.sendResponseHeaders(404, -1);
                        }
                    }
                    if (taskType == TypeOfObj.TASK){
                        response = taskManager.createTask(TaskAdapter.getTaskFromJson(body)).toString();
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        } catch (NullPointerException | IOException e) {
                            httpExchange.sendResponseHeaders(404, -1);
                        }
                    }
                    break;
                default:
                    httpExchange.sendResponseHeaders(404, -1);
        }
        httpExchange.close();
    }

}
