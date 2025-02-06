package com.pal.ui;

import com.pal.ui.login.LoginScreen;
import com.pal.ui.main.MainInterface;
import com.pal.ui.task.TaskScreen;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApp extends Application {
    private Stage primaryStage;
    private String authToken;
    private String currentUserId;
    private ImageView characterView;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginScreen();
    }

    public void showLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(this);
        primaryStage.setScene(loginScreen.getScene());
        primaryStage.setTitle("Palora - Login");
        primaryStage.show();
    }

    public void showMainInterface() {
        MainInterface mainInterface = new MainInterface(this);
        primaryStage.setScene(mainInterface.getScene());

        characterView = new ImageView(new Image("C:\\Palora\\Palora\\frontend\\src\\main\\resources\\images\\character_base.png"));
        characterView.setFitWidth(200);
        characterView.setPreserveRatio(true);

        SpriteAnimation idleAnimation = new SpriteAnimation(characterView, Duration.millis(300));
        idleAnimation.play();

    }

    public void showTaskScreen() {
        TaskScreen taskScreen = new TaskScreen(this);
        primaryStage.getScene().setRoot(taskScreen);
    }



    public void setAuthToken(String token) { this.authToken = token; }
    public String getAuthToken() { return authToken; }
    public void setCurrentUserId(String userId) { this.currentUserId = userId; }
    public String getCurrentUserId() {return currentUserId;}

    public static void main(String[] args) {
        launch(args);
    }
}
