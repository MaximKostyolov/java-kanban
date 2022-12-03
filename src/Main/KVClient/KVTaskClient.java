package Main.KVClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private URI uri;
    private String apiToken;

    public KVTaskClient (URI uri) {
        this.uri = uri;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri + "/register"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apiToken = response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException();
        }
    }

    public void put(String key, String json) {
        URI postUri = URI.create(uri + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(postUri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Данные менеджера задач успешно обновлены на сервере");
            } else {
                System.out.println("При сохранении менеджера задач произошла ошибка");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException();
        }
    }

    public String load(String key) {
        String manager = " ";
        URI getUri = URI.create(uri + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(getUri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                manager = response.body();
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return manager;
    }

    public String getApiToken() {
        return apiToken;
    }
}
