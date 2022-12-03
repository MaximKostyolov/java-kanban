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
import java.time.LocalDateTime;

public class TaskHandler implements HttpHandler {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private static Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private TaskManager managerOnServer;

    public TaskHandler(TaskManager managerOnServer) {
        this.managerOnServer = managerOnServer;
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        String method = httpExchange.getRequestMethod();
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        String query = httpExchange.getRequestURI().getRawQuery();
        switch (method) {
            case "GET":
                String result = getBodyForResponse(httpExchange, path, query); //обрабатываем GET запрос, готовим ответ
                get(httpExchange, result); //посылаем ответ на GET запрос
                break;
            case "POST":
                boolean isCorect = false;
                if (path[2].equals("task")) {
                    isCorect = processPostTaskRequest(httpExchange); //проверяем корректность переданных данных в запросе и выполняем действие в менеджере
                } else if (path[2].equals("subtask")) {
                    isCorect = processPostSubtaskRequest(httpExchange);
                } else if (path[2].equals("epic")) {
                    isCorect = processPostEpicRequest(httpExchange);
                }
                put(httpExchange, isCorect);
                break;
            case "DELETE":
                boolean isDeleteCorect = processDeleteRequest(httpExchange, path, query);
                delete(httpExchange, isDeleteCorect);
                break;
            default:
                try {
                    httpExchange.sendResponseHeaders(405, 0);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                break;
        }

    }

    private String getBodyForResponse(HttpExchange httpExchange, String[] path, String query) {
        String result = "";
        if (path.length < 3) {
            result = gson.toJson(FileBackedTasksManager.getPrioritizedTasks());
        } else if ((path[2].equals("task")) && (query == null)) {
            result = gson.toJson(managerOnServer.getTaskList());
        } else if (path[2].equals("task")) {
            int id = Integer.parseInt(query.substring(3));
            result = gson.toJson(managerOnServer.getTaskById(id));
        } else if (path[2].equals("subtask") && (query == null)) {
            result = gson.toJson(managerOnServer.getSubtaskList());
        } else if (path[2].equals("subtask")) {
            int id = Integer.parseInt(query.substring(3));
            result = gson.toJson(managerOnServer.getSubtaskById(id));
        } else if (path[2].equals("epic") && (query == null)) {
            result = gson.toJson(managerOnServer.getEpicList());
        } else if (path[2].equals("epic")) {
            int id = Integer.parseInt(query.substring(3));
            result = gson.toJson(managerOnServer.getEpicById(id));
        } else if (path[2].equals("history")) {
            result = gson.toJson(managerOnServer.getHistory());
        } else {
            System.out.println("Запрос отправлен некорректно");
        }
        return result;
    }

    private void get(HttpExchange httpExchange, String result) {
        try {
            if (!result.isEmpty()) {
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream os = httpExchange.getResponseBody();
                os.write(result.getBytes());
            } else {
                httpExchange.sendResponseHeaders(400, 0);
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

    }

    private String getRequestBody(HttpExchange httpExchange) {
        String body = "";
        try {
            InputStream inputStream = httpExchange.getRequestBody();
            body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return body;
    }
    private boolean processPostTaskRequest(HttpExchange httpExchange) {
        boolean isCorect = false;
        String body = getRequestBody(httpExchange);
        Task task = gson.fromJson(JsonParser.parseString(body).toString(), Task.class);
        if (task.getId() == 0) {
            task.setId(InMemoryTaskManager.getIdentificator());
        }
        boolean isContain = false;
        if (task != null) {
            for (Task taskFromServer : managerOnServer.getTaskList()) {
                if (taskFromServer.getName().equals(task.getName())) {
                    managerOnServer.updateTask(task, taskFromServer.getId());
                    isContain = true;
                }
            }
            if (!isContain) {
                managerOnServer.createTask(task);
            }
            isCorect = true;
        }
        return isCorect;
    }

    private boolean processPostSubtaskRequest(HttpExchange httpExchange) {
        boolean isCorect = false;
        String body = getRequestBody(httpExchange);
        Subtask subtask = gson.fromJson(JsonParser.parseString(body).toString(), Subtask.class);
        boolean isContain = false;
        if (subtask.getId() == 0) {
            subtask.setId(InMemoryTaskManager.getIdentificator());
        }
        if (subtask != null) {
            for (Subtask subtaskFromServer : managerOnServer.getSubtaskList()) {
                if (subtaskFromServer.getName().equals(subtask.getName())) {
                    managerOnServer.updateSubtask(subtask, subtaskFromServer.getId());
                    isContain = true;
                }
            }
            if (!isContain) {
                managerOnServer.createSubtask(subtask, subtask.getEpicId());
            }
            isCorect = true;
        }
        return isCorect;
    }

    private boolean processPostEpicRequest(HttpExchange httpExchange) {
        boolean isCorect = false;
        String body = getRequestBody(httpExchange);
        Epic epic = gson.fromJson(JsonParser.parseString(body).toString(), Epic.class);
        boolean isContain = false;
        if (epic.getId() == 0) {
            epic.setId(InMemoryTaskManager.getIdentificator());
        }
        if (epic != null) {
            for (Epic epicFromServer : managerOnServer.getEpicList()) {
                if (epicFromServer.getName().equals(epic.getName())) {
                    managerOnServer.updateTask(epic, epicFromServer.getId());
                    isContain = true;
                }
            }
            if (!isContain) {
                managerOnServer.createEpic(epic);
            }
            isCorect = true;
        }
        return isCorect;

    }

    private void put(HttpExchange httpExchange, Boolean isCorect) {
        try {
            if (isCorect) {
                httpExchange.sendResponseHeaders(201, 0);
            } else {
                httpExchange.sendResponseHeaders(404, 0);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean processDeleteRequest(HttpExchange httpExchange, String[] path, String query) {
        boolean isCorect = true;
        if (query == null) {
            if (path[2].equals("task")) {
                managerOnServer.removeTaskList();
            } else if (path[2].equals("subtask")) {
                managerOnServer.removeSubtaskList();
            } else if (path[2].equals("epic")) {
                managerOnServer.removeEpicList();
            }
        } else if (path[2].equals("task")) {
            int id = Integer.parseInt(query.substring(3));
            managerOnServer.removeTaskById(id);
        } else if (path[2].equals("subtask")) {
            int id = Integer.parseInt(query.substring(3));
            managerOnServer.removeSubtaskById(id);
        } else if (path[2].equals("epic")) {
            int id = Integer.parseInt(query.substring(3));
            managerOnServer.removeEpicById(id);
        } else {
            isCorect = false;
        }
        return isCorect;
    }

    private void delete(HttpExchange httpExchange, boolean isCorect) {
        try {
            if (isCorect) {
                httpExchange.sendResponseHeaders(204, 0);
            } else {
                httpExchange.sendResponseHeaders(400, 0);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}