package com.pal.ui.main;

import com.pal.ui.MainApp;
import com.pal.ui.sprite.SpriteAnimation;
import com.pal.ui.task.TaskScreen;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Rectangle2D;

public class MainInterface {
    private final MainApp mainApp;
    private BorderPane root = new BorderPane();
    private ImageView characterView; // Character image for animation
    private SpriteAnimation spriteAnimation; // Sprite animation for the character

    public MainInterface(MainApp mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }

    private void initializeUI() {
        // Navigation Bar
        HBox navBar = new HBox(10);
        Button characterButton = new Button("My Pal");
        Button tasksButton = new Button("Tasks");

        // Set up button actions
        characterButton.setOnAction(e -> showCharacterView());
        tasksButton.setOnAction(e -> showTasksView());

        navBar.getChildren().addAll(characterButton, tasksButton);
        root.setTop(navBar);

        // Initial view (Character View)
        showCharacterView();
    }

    private void showCharacterView() {
        VBox characterViewContainer = new VBox(20);

        // Load the sprite sheet image
        Image characterSheet = new Image("file:C:\\Palora\\Palora\\frontend\\src\\main\\resources\\images\\character_base.png");
        if (characterSheet.isError()) {
            System.err.println("Error loading image: " + characterSheet.getException());
        } else {
            System.out.println("Image loaded successfully.");
        }

        // Create an ImageView for the sprite sheet
        characterView = new ImageView(characterSheet);

        // Set the viewport to display the first sprite (64x64)
        characterView.setViewport(new Rectangle2D(0, 0, 64, 64));

        // Debugging: Print the viewport to confirm it's set
        System.out.println("Viewport: " + characterView.getViewport());

        // Set the size of the ImageView
        characterView.setFitWidth(200);
        characterView.setPreserveRatio(true);

        // Create a SpriteAnimation instance
        spriteAnimation = new SpriteAnimation(characterView, javafx.util.Duration.millis(200));

        // Start the animation
        spriteAnimation.play();

        // Add the character image to the container
        characterViewContainer.getChildren().add(characterView);
        root.setCenter(characterViewContainer);
    }

    private void showTasksView() {
        TaskScreen taskScreen = new TaskScreen(mainApp);
        root.setCenter(taskScreen);
    }

    public Scene getScene() {
        return new Scene(root, 800, 600);
    }
}