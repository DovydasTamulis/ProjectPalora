package com.pal.ui;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;

public class LoginScreen {
    private final MainApp mainApp;

    public LoginScreen(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public Scene getScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(15);
        grid.setHgap(10);

        // Email Field
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        GridPane.setConstraints(emailField, 0, 0);

        // Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        GridPane.setConstraints(passwordField, 0, 1);

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin(emailField.getText(), passwordField.getText()));
        GridPane.setConstraints(loginButton, 0, 2);

        // Register Button
        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> handleRegistration(emailField.getText(), passwordField.getText()));
        GridPane.setConstraints(registerButton, 1, 2);

        grid.getChildren().addAll(emailField, passwordField, loginButton, registerButton);
        return new Scene(grid, 300, 200);
    }

    private void handleLogin(String email, String password) {
        try {
            String token = ApiClient.loginUser(email, password);
            mainApp.setAuthToken(token);
            mainApp.setCurrentUserId(email);
            mainApp.showMainInterface();
        } catch (Exception e) {
            showAlert("Login Failed", e.getMessage());
        }
    }

    private void handleRegistration(String email, String password) {
        try {
            ApiClient.registerUser(email, password);
            showAlert("Success", "Registration successful! Please login.");
        } catch (Exception e) {
            showAlert("Registration Failed", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}