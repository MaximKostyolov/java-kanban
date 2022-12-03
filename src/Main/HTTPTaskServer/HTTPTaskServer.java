package Main.HTTPTaskServer;

import Main.Manager.TaskManager;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HTTPTaskServer {

    private static final int PORT = 8088;
    private HttpServer httpServer;
    private final TaskManager manager;

    public HTTPTaskServer(TaskManager manager) {
        this.manager = manager;
    }

    public void startServer() {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress("localhostServer", PORT),0);
            httpServer.createContext("/tasks", new TaskHandler(manager));
            httpServer.start();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
