package com.pal.ui;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApp extends Application {
    private Stage primaryStage;
    private String authToken; // Store JWT after login
    private String currentUserId;
    private ImageView characterView;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginScreen();
    }

    // Switch between scenes
    public void showLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(this);
        primaryStage.setScene(loginScreen.getScene());
        primaryStage.setTitle("Palora - Login");
        primaryStage.show();
    }

    public void showMainInterface() {
        MainInterface mainInterface = new MainInterface(this);
        primaryStage.setScene(mainInterface.getScene());
        primaryStage.setTitle("Palora - Your Virtual Pal");

        characterView = new ImageView(new Image("character_base.png"));
        characterView.setFitWidth(200);
        characterView.setPreserveRatio(true);

        SpriteAnimation idleAnimation = new SpriteAnimation(characterView, Duration.millis(200));
        idleAnimation.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Getters/Setters for authToken and userId
    public void setAuthToken(String token) { this.authToken = token; }
    public String getAuthToken() { return authToken; }
    public void setCurrentUserId(String userId) { this.currentUserId = userId; }
}
