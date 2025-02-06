package com.pal.ui;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

public class ApiClient {
    public static final String BASE_URL = "http://localhost:8080/api";

    public static String loginUser(String email, String password) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String jsonBody = String.format("{\"email\":\"%s\", \"passwordHash\":\"%s\"}", email, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("Login failed: " + response.body());
        }
    }

    public static void registerUser(String email, String password) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String jsonBody = String.format("{\"email\":\"%s\", \"passwordHash\":\"%s\"}", email, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Registration failed: " + response.body());
        }
    }
    public static List<Task> getTasks(String authToken) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Authorization", "Bearer " + authToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONArray jsonArray = new JSONArray(response.body());
            List<Task> tasks = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Task task = new Task(
                        jsonObject.optString("id"),
                        jsonObject.optString("title"),
                        jsonObject.optString("description"),
                        jsonObject.optBoolean("completed"),
                        "active" // Example: Add a default status for frontend logic
                );
                tasks.add(task);
            }
            return tasks;
        }
        throw new RuntimeException("Failed to get tasks: " + response.body());
    }

    public static Task createTask(String authToken, Task task) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Authorization", "Bearer " + authToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            JSONObject jsonObject = new JSONObject(response.body());
            Task createdTask = new Task(
                    jsonObject.optString("id"),
                    jsonObject.optString("title"),
                    jsonObject.optString("description"),
                    jsonObject.optBoolean("completed"),
                    "created" // Example: Add a status for frontend logic
            );
            return createdTask;
        }
        throw new RuntimeException("Failed to create task: " + response.body());
    }

    public static Task startTask(String authToken, String taskId) throws IOException, InterruptedException {
        return modifyTaskStatus(authToken, taskId, "start");
    }

    public static Task pauseTask(String authToken, String taskId) throws IOException, InterruptedException {
        return modifyTaskStatus(authToken, taskId, "pause");
    }

    private static Task modifyTaskStatus(String authToken, String taskId, String action) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String endpoint = String.format("%s/tasks/%s/%s", BASE_URL, taskId, action);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Authorization", "Bearer " + authToken)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONObject jsonObject = new JSONObject(response.body());
            Task updatedTask = new Task(
                    jsonObject.optString("id"),
                    jsonObject.optString("title"),
                    jsonObject.optString("description"),
                    jsonObject.optBoolean("completed"),
                    action // Use the action as the status for frontend logic
            );
            return updatedTask;
        }
        throw new RuntimeException("Failed to " + action + " task: " + response.body());
    }

    public static Task tickTime(String authToken, String taskId, long seconds) throws IOException, InterruptedException {
        return null;
    }

    public static void deleteTask(String authToken, String id) {
    }
}