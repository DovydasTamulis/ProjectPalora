package com.pal.ui.main;

import com.pal.ui.MainApp;
import com.pal.ui.character.AnimatedCharacter;
import com.pal.ui.chat.ChatScreen;
import com.pal.ui.task.TaskScreen;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class MainInterface {
    private final MainApp mainApp;
    private BorderPane root = new BorderPane();
    private AnimatedCharacter character;

    public MainInterface(MainApp mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }

    private void initializeUI() {
        // Navigation Bar
        HBox navBar = new HBox(10);
        navBar.getStyleClass().add("nav-bar");

        Button characterButton = new Button("My Pal");
        Button tasksButton = new Button("Tasks");
        Button chatButton = new Button("Chat");

        characterButton.getStyleClass().add("nav-button");
        tasksButton.getStyleClass().add("nav-button");
        chatButton.getStyleClass().add("nav-button");

        characterButton.setOnAction(e -> showCharacterView());
        tasksButton.setOnAction(e -> showTasksView());
        chatButton.setOnAction(e -> showChatView());

        navBar.getChildren().addAll(characterButton, tasksButton, chatButton);
        root.setTop(navBar);

        // Show character view initially
        showCharacterView();
    }

    private void showCharacterView() {
        character = new AnimatedCharacter();
        ImageView characterView = character.getImageView();
        characterView.setFitWidth(192);
        characterView.setFitHeight(288);
        characterView.setPreserveRatio(true);

        setupCharacterControls();

        StackPane centerPane = new StackPane(characterView);
        centerPane.getStyleClass().add("character-container");
        root.setCenter(centerPane);

        character.playAnimation("idle");
    }

    private void setupCharacterControls() {
        Scene scene = root.getScene();
        if (scene != null) {
            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.UP) character.playAnimation("run");
                if (e.getCode() == KeyCode.DOWN) character.playAnimation("idle");
                if (e.getCode() == KeyCode.SPACE) character.playAnimation("hurt");
                if (e.getCode() == KeyCode.ESCAPE) character.playAnimation("die");
            });
        }
    }

    private void showTasksView() {
        TaskScreen taskScreen = new TaskScreen(mainApp);
        taskScreen.getStylesheets().add(getClass().getResource("/css/task-styles.css").toExternalForm());
        root.setCenter(taskScreen);
    }

    private void showChatView() {
        ChatScreen chatScreen = new ChatScreen(mainApp);
        root.setCenter(chatScreen);
    }

    public Scene getScene() {
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/css/main-styles.css").toExternalForm());
        return scene;
    }
}

