package Test;

import Main.HTTPTaskServer.LocalDateTimeAdapter;
import Main.HTTPTaskServer.TaskHandler;
import Main.KVServer.KVServer;
import Main.Manager.Managers;
import Main.Manager.TaskManager;
import Main.Models.Epic;
import Main.Models.Subtask;
import Main.Models.Task;
import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class НTTPTaskServerAPITest {

    private static String URL = "http://localhost:8098";
    private HttpServer httpTestServer;
    private HttpClient client;
    private Gson gson;
    private static TaskManager manager;

    @BeforeAll
    static void addManager() {
        try {
            KVServer server = new KVServer();
            server.start();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        manager = Managers.getDefault();
        Task task = new Task("Задача 1", "Тестовая задача 1");
        Epic epic = new Epic("Эпик 2", "Тестовый эпик 2");
        Subtask subtask = new Subtask("Подзадача 3", "Тестовая подзадача 3");
        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask, epic.getId());
        task = manager.getTaskById(task.getId());

    }

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        httpTestServer = HttpServer.create(new InetSocketAddress("localhost", 8098), 0);
        httpTestServer.createContext("/tasks", new TaskHandler(manager));
        httpTestServer.start();

        gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
    }

    @AfterEach
    void stopServer() {
        httpTestServer.stop(0);
    }

    @Test
    void getAllTasks() throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL))
                .build(), HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void getAllEpic() throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/epic"))
                .build(), HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/history"))
                .build(), HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }
}
