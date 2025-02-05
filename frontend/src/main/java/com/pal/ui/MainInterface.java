package com.pal.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MainInterface {
    private final MainApp mainApp;
    private ImageView characterView;

    public MainInterface(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();

        // Character Display Area
        VBox characterArea = new VBox(20);
        characterArea.setAlignment(Pos.CENTER);
        characterArea.setPadding(new Insets(20));

        // Initialize characterView
        characterView = new ImageView(new Image("C:\\Palora\\Palora\\frontend\\src\\main\\resources\\images\\character_base.png"));
        characterView.setFitWidth(200);
        characterView.setPreserveRatio(true);

        // Level Up Button
        Button levelUpButton = new Button("Train!");
        levelUpButton.setOnAction(e -> handleLevelUp());

        characterArea.getChildren().addAll(characterView, levelUpButton);
        root.setCenter(characterArea);

        return new Scene(root, 600, 400);
    }

    private void handleLevelUp() {
        try {
            // Example: Add 10 XP to character
            HttpClient client = HttpClient.newHttpClient();
            String url = String.format("%s/characters/%s/level-up?xpToAdd=10",
                    ApiClient.BASE_URL, "character-id-here");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + mainApp.getAuthToken())
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());

            // Update character image
            characterView.setImage(new Image("C:\\Palora\\Palora\\frontend\\src\\main\\resources\\images\\character_base.png"));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Level up failed: " + e.getMessage()).show();
        }
    }
}
