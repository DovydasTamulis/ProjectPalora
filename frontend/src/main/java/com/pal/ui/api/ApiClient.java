package com.pal.ui.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pal.ui.model.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApiClient {
    public static final String BASE_URL = "http://localhost:8080/api";

    /**
     * Logs in a user and returns the authentication token.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @return The authentication token.
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
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

    /**
     * Registers a new user.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
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

    /**
     * Fetches all tasks for the authenticated user.
     *
     * @param authToken The user's authentication token.
     * @return A list of tasks.
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public static List<Task> getTasks(String authToken) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Authorization", "Bearer " + authToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray jsonArray = jsonResponse.getJSONArray("data"); // Adjust based on backend response structure

            List<Task> tasks = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Task task = parseTaskFromJson(jsonObject);
                tasks.add(task);
            }
            return tasks;
        }
        throw new RuntimeException("Failed to get tasks: " + response.body());
    }

    /**
     * Creates a new task for the authenticated user.
     *
     * @param authToken The user's authentication token.
     * @param task      The task to create.
     * @return The created task.
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public static Task createTask(String authToken, Task task) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Serialize the Task object to JSON
        String jsonBody = objectMapper.writeValueAsString(task);

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Authorization", "Bearer " + authToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Send the request and handle the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONObject jsonData = jsonResponse.getJSONObject("data"); // Adjust based on backend response structure
            return parseTaskFromJson(jsonData);
        }

        throw new RuntimeException("Failed to create task: " + response.body());
    }

    /**
     * Starts a task for the authenticated user.
     *
     * @param authToken The user's authentication token.
     * @param taskId    The ID of the task to start.
     * @return The updated task.
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public static Task startTask(String authToken, String taskId) throws IOException, InterruptedException {
        return modifyTaskStatus(authToken, taskId, "start");
    }

    /**
     * Pauses a task for the authenticated user.
     *
     * @param authToken The user's authentication token.
     * @param taskId    The ID of the task to pause.
     * @return The updated task.
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public static Task pauseTask(String authToken, String taskId) throws IOException, InterruptedException {
        return modifyTaskStatus(authToken, taskId, "pause");
    }

    /**
     * Resets a task for the authenticated user.
     *
     * @param authToken The user's authentication token.
     * @param taskId    The ID of the task to reset.
     * @return The updated task.
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public static Task resetTask(String authToken, String taskId) throws IOException, InterruptedException {
        return modifyTaskStatus(authToken, taskId, "reset");
    }

    /**
     * Ticks time for a task for the authenticated user.
     *
     * @param authToken The user's authentication token.
     * @param taskId    The ID of the task to tick.
     * @param seconds   The number of seconds to tick.
     * @return The updated task.
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public static Task tickTime(String authToken, String taskId, long seconds) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String endpoint = String.format("%s/tasks/%s/tick?seconds=%d", BASE_URL, taskId, seconds);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Authorization", "Bearer " + authToken)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONObject jsonData = jsonResponse.getJSONObject("data"); // Adjust based on backend response structure
            return parseTaskFromJson(jsonData);
        }
        throw new RuntimeException("Failed to tick time: " + response.body());
    }

    /**
     * Deletes a task for the authenticated user.
     *
     * @param authToken The user's authentication token.
     * @param taskId    The ID of the task to delete.
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public static void deleteTask(String authToken, String taskId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String endpoint = String.format("%s/tasks/%s", BASE_URL, taskId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Authorization", "Bearer " + authToken)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Delete failed: " + response.body());
        }
    }

    /**
     * Modifies the status of a task (e.g., start, pause, reset).
     *
     * @param authToken The user's authentication token.
     * @param taskId    The ID of the task to modify.
     * @param action    The action to perform (e.g., "start", "pause", "reset").
     * @return The updated task.
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
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
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONObject jsonData = jsonResponse.getJSONObject("data"); // Adjust based on backend response structure
            return parseTaskFromJson(jsonData);
        }
        throw new RuntimeException("Failed to " + action + " task: " + response.body());
    }

    /**
     * Parses a JSON object into a Task object.
     *
     * @param jsonObject The JSON object representing the task.
     * @return The parsed Task object.
     */
    private static Task parseTaskFromJson(JSONObject jsonObject) {
        return new Task(
                jsonObject.optString("id"),
                jsonObject.optString("title"),
                jsonObject.optString("description"),
                jsonObject.optLong("duration"),
                jsonObject.optLong("elapsedTime"),
                jsonObject.optBoolean("completed"),
                jsonObject.optString("status"),
                LocalDateTime.parse(jsonObject.optString("createdAt")),
                LocalDateTime.parse(jsonObject.optString("expiresAt"))
        );
    }
}