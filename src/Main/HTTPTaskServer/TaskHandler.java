package Main.HTTPTaskServer;

import Main.Manager.FileBackedTasksManager;
import Main.Manager.InMemoryTaskManager;
import Main.Manager.TaskManager;
import Main.Models.Epic;
import Main.Models.Subtask;
import Main.Models.Task;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public class TaskHandler implements HttpHandler {

    //private static final int PORT = 8088;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private static Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private TaskManager managerOnServer = FileBackedTasksManager.loadFromFile(Paths.get("resourses", "taskManagerOnServer.csv").toFile());
     //

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        //List<Task> history = managerOnServer.getHistory();
        String result = "";
        String method = httpExchange.getRequestMethod();
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        String query = httpExchange.getRequestURI().getRawQuery();
        switch (method) {
            case "GET":
                if (path.length < 3) {
                    result = gson.toJson(FileBackedTasksManager.getPrioritizedTasks());
                    httpExchange.sendResponseHeaders(200, 0);
                } else if ((path[2].equals("task")) && (query == null)) {
                    result = gson.toJson(managerOnServer.getTaskList());
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path[2].equals("task")) {
                    int id = Integer.parseInt(query.substring(3));
                    result = gson.toJson(managerOnServer.getTaskById(id));
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path[2].equals("subtask") && (query == null)) {
                    result = gson.toJson(managerOnServer.getSubtaskList());
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path[2].equals("subtask")) {
                    int id = Integer.parseInt(query.substring(3));
                    result = gson.toJson(managerOnServer.getSubtaskById(id));
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path[2].equals("epic") && (query == null)) {
                    result = gson.toJson(managerOnServer.getEpicList());
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path[2].equals("epic")) {
                    int id = Integer.parseInt(query.substring(3));
                    result = gson.toJson(managerOnServer.getEpicById(id));
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path[2].equals("history")) {
                    result = gson.toJson(managerOnServer.getHistory());
                    httpExchange.sendResponseHeaders(200, 0);
                }
                break;
            case "POST":
                if (path[2].equals("task")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Task task = gson.fromJson(JsonParser.parseString(body).toString(), Task.class);
                    if (task.getId() == 0) {
                        task.setId(InMemoryTaskManager.getIdentificator());
                    }
                    boolean isContain = false;
                    if (task != null) {
                        for (Task taskFromServer : managerOnServer.getTaskList()) {
                            if (taskFromServer.getName().equals(task.getName())) {
                                managerOnServer.updateTask(task, taskFromServer.getId());
                                httpExchange.sendResponseHeaders(201, 0);
                                isContain = true;
                            }
                        }
                        if (!isContain) {
                            managerOnServer.createTask(task);
                            httpExchange.sendResponseHeaders(201, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                    }
                }
                if (path[2].equals("subtask")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Subtask subtask = gson.fromJson(JsonParser.parseString(body).toString(), Subtask.class);
                    boolean isContain = false;
                    if (subtask.getId() == 0) {
                        subtask.setId(InMemoryTaskManager.getIdentificator());
                    }
                    if (subtask != null) {
                        for (Subtask subtaskFromServer : managerOnServer.getSubtaskList()) {
                            if (subtaskFromServer.getName().equals(subtask.getName())) {
                                managerOnServer.updateSubtask(subtask, subtaskFromServer.getId());
                                httpExchange.sendResponseHeaders(201, 0);
                                isContain = true;
                            }
                        }
                        if (!isContain) {
                            managerOnServer.createSubtask(subtask, subtask.getEpicId());
                            httpExchange.sendResponseHeaders(201, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                    }
                }
                if (path[2].equals("epic")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Epic epic = gson.fromJson(JsonParser.parseString(body).toString(), Epic.class);
                    boolean isContain = false;
                    if (epic.getId() == 0) {
                        epic.setId(InMemoryTaskManager.getIdentificator());
                    }
                    if (epic != null) {
                        for (Epic epicFromServer : managerOnServer.getEpicList()) {
                            if (epicFromServer.getName().equals(epic.getName())) {
                                managerOnServer.updateTask(epic, epicFromServer.getId());
                                httpExchange.sendResponseHeaders(201, 0);
                                isContain = true;
                            }
                        }
                        if (!isContain) {
                            managerOnServer.createEpic(epic);
                            httpExchange.sendResponseHeaders(201, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                    }
                }
                break;
            case "DELETE":
                if (query == null) {
                    if (path[2].equals("task")) {
                        managerOnServer.removeTaskList();
                        httpExchange.sendResponseHeaders(204, 0);
                    } else if (path[2].equals("subtask")) {
                        managerOnServer.removeSubtaskList();
                        httpExchange.sendResponseHeaders(204, 0);
                    } else if (path[2].equals("epic")) {
                        managerOnServer.removeEpicList();
                        httpExchange.sendResponseHeaders(204, 0);
                    }
                } else if (path[2].equals("task")) {
                    int id = Integer.parseInt(query.substring(3));
                    managerOnServer.removeTaskById(id);
                    httpExchange.sendResponseHeaders(204, 0);
                } else if (path[2].equals("subtask")) {
                    int id = Integer.parseInt(query.substring(3));
                    managerOnServer.removeSubtaskById(id);
                    httpExchange.sendResponseHeaders(204, 0);
                } else if (path[2].equals("epic")) {
                    int id = Integer.parseInt(query.substring(3));
                    managerOnServer.removeEpicById(id);
                    httpExchange.sendResponseHeaders(204, 0);
                }
                break;
            default:
                httpExchange.sendResponseHeaders(405, 0);
                break;
        }
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(result.getBytes());
        } catch (NoSuchElementException e) {
            httpExchange.sendResponseHeaders(404, 0);
        } catch (IllegalArgumentException e) {
            httpExchange.sendResponseHeaders(400, 0);
        } catch (Error error) {
            httpExchange.sendResponseHeaders(500, 0);
        }
    }
}