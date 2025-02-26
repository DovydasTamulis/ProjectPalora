package com.pal.ui;

import com.pal.ui.login.LoginScreen;
import com.pal.ui.login.RegisterScreen;
import com.pal.ui.main.MainInterface;
import com.pal.ui.task.TaskScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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
        Scene scene = loginScreen.getScene();
        String css = getClass().getResource("/css/login-styles.css") != null ?
                getClass().getResource("/css/login-styles.css").toExternalForm() : null;
        if (css != null) {
            scene.getStylesheets().add(css);
        } else {
            System.err.println("CSS file not found: /css/login-styles.css");
        }
        primaryStage.setScene(scene);
        primaryStage.setTitle("Palora - Login");
        primaryStage.show();
    }

    public void showMainInterface() {
        MainInterface mainInterface = new MainInterface(this);
        Scene scene = mainInterface.getScene();
        String css = getClass().getResource("/css/main-styles.css") != null ?
                getClass().getResource("/css/main-styles.css").toExternalForm() : null;
        if (css != null) {
            scene.getStylesheets().add(css);
        } else {
            System.err.println("CSS file not found: /css/main-styles.css");
        }
        primaryStage.setScene(scene);
    }

    public void showTaskScreen() {
        TaskScreen taskScreen = new TaskScreen(this);
        Scene scene = new Scene(taskScreen, 800, 600);
        String css = getClass().getResource("/css/task-styles.css") != null ?
                getClass().getResource("/css/task-styles.css").toExternalForm() : null;
        if (css != null) {
            scene.getStylesheets().add(css);
        } else {
            System.err.println("CSS file not found: /css/task-styles.css");
        }
        primaryStage.setScene(scene);
    }

    public void showRegisterScreen() {
        RegisterScreen registerScreen = new RegisterScreen(this);
        Scene scene = registerScreen.getScene();
        String css = getClass().getResource("/css/login-styles.css") != null ?
                getClass().getResource("/css/login-styles.css").toExternalForm() : null;
        if (css != null) {
            scene.getStylesheets().add(css);
        } else {
            System.err.println("CSS file not found: /css/login-styles.css");
        }
        primaryStage.setScene(scene);
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setCurrentUserId(String userId) {
        this.currentUserId = userId;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public static void main(String[] args) {
        launch(args);
    }
}