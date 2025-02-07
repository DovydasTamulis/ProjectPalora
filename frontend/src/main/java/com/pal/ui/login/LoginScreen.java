package com.pal.ui.login;

import com.pal.ui.api.ApiClient;
import com.pal.ui.MainApp;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginScreen {
    private final MainApp mainApp;
    private TextField emailField;
    private PasswordField passwordField;

    public LoginScreen(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public Scene getScene() {
        GridPane grid = createMainGrid();
        addUIComponents(grid);
        return new Scene(grid, 450, 350);
    }

    private GridPane createMainGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(20);
        grid.setPadding(new Insets(40, 40, 40, 40));
        grid.setStyle("-fx-background-color: #f8f9fa;");
        return grid;
    }

    private void addUIComponents(GridPane grid) {
        // Title
        Label titleLabel = new Label("Palora");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);

        // Subtitle
        Label subtitle = new Label("Your Productivity Companion");
        subtitle.setStyle("-fx-text-fill: #7f8c8d;");
        grid.add(subtitle, 0, 1, 2, 1);
        GridPane.setHalignment(subtitle, HPos.CENTER);

        // Email Field
        emailField = createStyledTextField("Email");
        grid.add(emailField, 0, 2);

        // Password Field
        passwordField = createStyledPasswordField("Password");
        grid.add(passwordField, 0, 3);

        // Button Container
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        Button loginButton = createPrimaryButton("Sign In");
        loginButton.setOnAction(e -> handleLogin());
        Button registerButton = createSecondaryButton("Register");
        registerButton.setOnAction(e -> handleRegistration());
        buttonBox.getChildren().addAll(loginButton, registerButton);
        grid.add(buttonBox, 0, 4);
    }

    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-pref-width: 300px; -fx-padding: 12px; " +
                "-fx-background-radius: 8px; -fx-border-radius: 8px; " +
                "-fx-border-color: #dfe6e9; -fx-border-width: 1px;");
        return field;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.setStyle("-fx-pref-width: 300px; -fx-padding: 12px; " +
                "-fx-background-radius: 8px; -fx-border-radius: 8px; " +
                "-fx-border-color: #dfe6e9; -fx-border-width: 1px;");
        return field;
    }

    private Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 12px 30px; " +
                "-fx-background-radius: 8px; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle() + "-fx-effect: dropshadow(gaussian, rgba(52,152,219,0.5), 10, 0, 0, 0);"));
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replace("-fx-effect: dropshadow(gaussian, rgba(52,152,219,0.5), 10, 0, 0, 0);", "")));
        return btn;
    }

    private Button createSecondaryButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 12px 30px; " +
                "-fx-background-radius: 8px; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle() + "-fx-effect: dropshadow(gaussian, rgba(46,204,113,0.5), 10, 0, 0, 0);"));
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replace("-fx-effect: dropshadow(gaussian, rgba(46,204,113,0.5), 10, 0, 0, 0);", "")));
        return btn;
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Validation Error", "Please fill in all fields", Alert.AlertType.ERROR);
            return;
        }
        try {
            String token = ApiClient.loginUser(email, password);
            mainApp.setAuthToken(token);
            mainApp.setCurrentUserId(email);
            mainApp.showMainInterface();
        } catch (Exception e) {
            showAlert("Login Failed", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void handleRegistration() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Validation Error", "Please fill in all fields", Alert.AlertType.ERROR);
            return;
        }
        try {
            ApiClient.registerUser(email, password);
            showAlert("Registration Successful",
                    "Account created! Please login with your credentials",
                    Alert.AlertType.INFORMATION);
            clearFields();
        } catch (Exception e) {
            showAlert("Registration Failed", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearFields() {
        emailField.clear();
        passwordField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Styling the alert dialog
        alert.getDialogPane().setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: #dfe6e9; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 8px; " +
                        "-fx-background-radius: 8px;");
        alert.showAndWait();
    }
}