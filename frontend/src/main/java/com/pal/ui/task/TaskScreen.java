package com.pal.ui.task;

import com.pal.ui.MainApp;
import com.pal.ui.api.ApiClient;
import com.pal.ui.model.Task;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class TaskScreen extends VBox {
    private static final Logger log = LoggerFactory.getLogger(TaskScreen.class);
    private final MainApp mainApp;
    private TableView<Task> taskTable = new TableView<>();
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();

    public TaskScreen(MainApp mainApp) {
        this.mainApp = mainApp;

        // Link the CSS file
        getStylesheets().add(getClass().getResource("/css/task-styles.css").toExternalForm());

        initializeUI();
        loadTasks();
    }

    private void initializeUI() {
        // Table Columns
        TableColumn<Task, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        titleColumn.setPrefWidth(150);

        TableColumn<Task, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        descriptionColumn.setPrefWidth(250);

        TableColumn<Task, Boolean> completedColumn = new TableColumn<>("Completed");
        completedColumn.setCellValueFactory(cellData -> cellData.getValue().completedProperty());
        completedColumn.setPrefWidth(100);

        TableColumn<Task, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        statusColumn.setPrefWidth(100);

        TableColumn<Task, Number> durationCol = new TableColumn<>("Duration (min)");
        durationCol.setCellValueFactory(cellData -> cellData.getValue().durationProperty().divide(60));
        durationCol.setPrefWidth(120);

        TableColumn<Task, Number> elapsedCol = new TableColumn<>("Elapsed (min)");
        elapsedCol.setCellValueFactory(cellData -> cellData.getValue().elapsedTimeProperty().divide(60));
        elapsedCol.setPrefWidth(120);

        TableColumn<Task, LocalDateTime> expiresCol = new TableColumn<>("Expires At");
        expiresCol.setCellValueFactory(cellData -> cellData.getValue().expiresAtProperty());
        expiresCol.setPrefWidth(150);

        taskTable.getColumns().addAll(titleColumn, descriptionColumn, completedColumn, statusColumn, durationCol, elapsedCol, expiresCol);
        taskTable.setItems(tasks);

        // Buttons
        Button createButton = new Button("Create Task");
        createButton.getStyleClass().add("button");
        createButton.setOnAction(e -> createTaskDialog());

        Button startButton = new Button("Start Task");
        startButton.getStyleClass().add("button");
        startButton.setOnAction(e -> startSelectedTask());

        Button pauseButton = new Button("Pause Task");
        pauseButton.getStyleClass().add("button");
        pauseButton.setOnAction(e -> pauseSelectedTask());

        Button deleteButton = new Button("Delete Task");
        deleteButton.getStyleClass().add("button");
        deleteButton.setOnAction(e -> deleteSelectedTask());

        HBox buttonBar = new HBox(10, createButton, startButton, pauseButton, deleteButton);
        buttonBar.setPadding(new Insets(10));
        buttonBar.getStyleClass().add("button-bar");

        // Layout
        VBox.setMargin(taskTable, new Insets(10));
        getChildren().addAll(taskTable, buttonBar);
    }

    private void createTaskDialog() {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Create New Task");

        // Link the CSS file to the dialog pane
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/task-styles.css").toExternalForm());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Input fields
        TextField titleField = new TextField();
        titleField.setPromptText("Enter task title");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Enter task description");

        // Task type selection
        Label typeLabel = new Label("Task Type:");
        ToggleGroup taskTypeGroup = new ToggleGroup();

        RadioButton reminderButton = new RadioButton("Reminder");
        reminderButton.setToggleGroup(taskTypeGroup);
        reminderButton.setSelected(true); // Default selection

        RadioButton focusButton = new RadioButton("Focus");
        focusButton.setToggleGroup(taskTypeGroup);

        // Duration spinner
        Spinner<Integer> durationSpinner = new Spinner<>(1, 240, 30);

        // Add components to the grid
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(typeLabel, 0, 2);
        grid.add(reminderButton, 1, 2);
        grid.add(focusButton, 1, 3);
        grid.add(new Label("Duration (minutes):"), 0, 4);
        grid.add(durationSpinner, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String taskType = ((RadioButton) taskTypeGroup.getSelectedToggle()).getText();
                return new Task(
                        null,
                        titleField.getText(),
                        descriptionField.getText(),
                        durationSpinner.getValue() * 60L,
                        0L,
                        false,
                        taskType.toUpperCase(), // Store task type in status field
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(durationSpinner.getValue())
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newTask -> {
            try {
                Task createdTask = ApiClient.createTask(mainApp.getAuthToken(), newTask);
                tasks.add(createdTask);
                log.info("Task created successfully: {}", createdTask.getTitle());

                // Handle specific task type behavior
                if ("REMINDER".equals(createdTask.getStatus())) {
                    scheduleReminder(createdTask);
                } else if ("FOCUS".equals(createdTask.getStatus())) {
                    startFocusMode(createdTask);
                }
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
            List<Task> userTasks = ApiClient.getTasks(mainApp.getAuthToken());
            tasks.setAll(userTasks);
        } catch (Exception e) {
            showAlert("Error Loading Tasks", e.getMessage());
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
                        if (updated.isCompleted() || updated.getExpiresAt().isBefore(LocalDateTime.now())) {
                            ((Timeline) e.getSource()).stop();
                        }
                    } catch (Exception ex) {
                        showAlert("Timer Error", ex.getMessage());
                        ((Timeline) e.getSource()).stop();
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

        // Link the CSS file to the alert dialog pane
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/task-styles.css").toExternalForm());

        alert.showAndWait();
    }

    private void scheduleReminder(Task task) {
        long delayInSeconds = task.getDuration();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(delayInSeconds), e -> {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Reminder");
                        alert.setHeaderText("Task Reminder");
                        alert.setContentText("It's time to work on: " + task.getTitle());

                        // Link the CSS file to the alert dialog pane
                        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/task-styles.css").toExternalForm());

                        alert.show(); // Use show() instead of showAndWait()
                    });
                })
        );
        timeline.play();
    }

    private void startFocusMode(Task task) {
        // Example: Restrict access to certain apps
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Focus Mode");
        alert.setHeaderText("Focus Mode Activated");
        alert.setContentText("You are now in focus mode for: " + task.getTitle());
        alert.showAndWait();

        // TODO: Implement app restriction logic here
        // This could involve disabling other apps or monitoring system activity.
    }
}