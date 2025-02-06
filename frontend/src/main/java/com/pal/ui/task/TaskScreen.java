package com.pal.ui.task;

import com.pal.ui.ApiClient;
import com.pal.ui.MainApp;
import com.pal.ui.Task;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.util.List;

public class TaskScreen extends VBox {
    private final MainApp mainApp;
    private TableView<Task> taskTable = new TableView<>(); // Use CustomTask instead of PalTask
    private final ObservableList<Task> tasks = FXCollections.observableArrayList(); // Use CustomTask

    public TaskScreen(MainApp mainApp) {
        this.mainApp = mainApp;
        initializeUI();
        loadTasks();
    }

    private void initializeUI() {
        // Initialize UI components (e.g., table columns, buttons)
        TableColumn<Task, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());

        TableColumn<Task, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        TableColumn<Task, Boolean> completedColumn = new TableColumn<>("Completed");
        completedColumn.setCellValueFactory(cellData -> cellData.getValue().completedProperty());

        TableColumn<Task, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        TableColumn<Task, Number> durationCol = new TableColumn<>("Duration (min)");
        durationCol.setCellValueFactory(cellData -> cellData.getValue().durationProperty().divide(60));

        TableColumn<Task, Number> elapsedCol = new TableColumn<>("Elapsed (min)");
        elapsedCol.setCellValueFactory(cellData -> cellData.getValue().elapsedTimeProperty().divide(60));

        TableColumn<Task, String> expiresCol = new TableColumn<>("Expires At");
        expiresCol.setCellValueFactory(cellData -> cellData.getValue().expiresAtProperty().asString());

        taskTable.getColumns().addAll(titleColumn, descriptionColumn, completedColumn, statusColumn);
        taskTable.setItems(tasks);

        Button createButton = new Button("Create Task");
        createButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Create Task");
            dialog.setHeaderText("Enter task title:");
            dialog.showAndWait().ifPresent(title -> createTask(title));
        });

        Button startButton = new Button("Start Task");
        startButton.setOnAction(e -> startSelectedTask());

        Button pauseButton = new Button("Pause Task");
        pauseButton.setOnAction(e -> pauseSelectedTask());

        Button deleteButton = new Button("Delete Task");
        deleteButton.setOnAction(e -> deleteSelectedTask());

        getChildren().addAll(taskTable, createButton, startButton, pauseButton, deleteButton);
    }

    private void createTask() {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Create New Task");

        GridPane grid = new GridPane();
        TextField titleField = new TextField();
        TextField descriptionField = new TextField();
        Spinner<Integer> durationSpinner = new Spinner<>(1, 240, 30); // 30 minutes default

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Duration (minutes):"), 0, 2);
        grid.add(durationSpinner, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return new Task(
                        null,
                        titleField.getText(),
                        descriptionField.getText(),
                        durationSpinner.getValue() * 60L, // Convert minutes to seconds
                        0L, // Initial elapsed time
                        false,
                        "CREATED",
                        java.time.LocalDateTime.now(),
                        java.time.LocalDateTime.now().plusMinutes(durationSpinner.getValue())
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newTask -> {
            try {
                Task createdTask = ApiClient.createTask(mainApp.getAuthToken(), newTask);
                tasks.add(createdTask);
            } catch (Exception e) {
                showAlert("Creation Error", e.getMessage());
            }
        });
    }


    private void deleteSelectedTask() {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                ApiClient.deleteTask(mainApp.getAuthToken(), selected.getId());
                tasks.remove(selected);
            } catch (Exception e) {
                showAlert("Error Deleting Task", e.getMessage());
            }
        } else {
            showAlert("Error", "No task selected");
        }
    }

    private void pauseSelectedTask() {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                Task updated = ApiClient.pauseTask(mainApp.getAuthToken(), selected.getId());
                updateTaskInList(updated);
            } catch (Exception e) {
                showAlert("Error Pausing Task", e.getMessage());
            }
        } else {
            showAlert("Error", "No task selected");
        }
    }

    private void loadTasks() {
        try {
            List<Task> userTasks = ApiClient.getTasks(mainApp.getAuthToken()); // Returns CustomTask objects
            tasks.setAll(userTasks);
        } catch (Exception e) {
            showAlert("Error Loading Tasks", e.getMessage());
        }
    }

    private void createTask(String title) {
        if (title.isEmpty()) {
            showAlert("Error", "Title is required");
            return;
        }
        Task newTask = new Task(null, title, "", false, "created"); // Create a CustomTask object
        try {
            Task createdTask = ApiClient.createTask(mainApp.getAuthToken(), newTask); // Pass CustomTask
            tasks.add(createdTask);
        } catch (Exception e) {
            showAlert("Error Creating Task", e.getMessage());
        }
    }

    private void startSelectedTask() {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                Task updated = ApiClient.startTask(mainApp.getAuthToken(), selected.getId());
                updateTaskInList(updated);
                startTimer(updated);
            } catch (Exception e) {
                showAlert("Error Starting Task", e.getMessage());
            }
        } else {
            showAlert("Error", "No task selected");
        }
    }

    private void updateTaskInList(Task updatedTask) {
        int index = tasks.indexOf(updatedTask);
        if (index >= 0) {
            tasks.set(index, updatedTask);
        }
    }

    private void startTimer(Task task) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    try {
                        Task updated = ApiClient.tickTime(mainApp.getAuthToken(), task.getId(), 1);
                        updateTaskInList(updated);
                    } catch (Exception ex) {
                        showAlert("Timer Error", ex.getMessage());
                    }
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}