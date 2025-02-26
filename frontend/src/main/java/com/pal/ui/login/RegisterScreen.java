package com.pal.ui.login;

import com.pal.ui.MainApp;
import com.pal.ui.api.ApiClient;
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

public class RegisterScreen {
    private final MainApp mainApp;
    private TextField usernameField;
    private TextField emailField;
    private PasswordField passwordField;

    public RegisterScreen(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public Scene getScene() {
        GridPane grid = createMainGrid();
        addUIComponents(grid);

        // Load the CSS file
        grid.getStylesheets().add(getClass().getResource("/css/login-styles.css").toExternalForm());

        return new Scene(grid, 450, 400);
    }

    private GridPane createMainGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(20);
        grid.setPadding(new Insets(40, 40, 40, 40));
        grid.getStyleClass().add("grid-pane"); // Apply CSS class
        return grid;
    }

    private void addUIComponents(GridPane grid) {
        // Title
        Label titleLabel = new Label("Register");
        titleLabel.getStyleClass().add("title"); // Apply CSS class
        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);

        // Username Field
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("text-field"); // Apply CSS class
        grid.add(usernameField, 0, 1);

        // Email Field
        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.getStyleClass().add("text-field"); // Apply CSS class
        grid.add(emailField, 0, 2);

        // Password Field
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("password-field"); // Apply CSS class
        grid.add(passwordField, 0, 3);

        // Button Container
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        // Sign In Button
        Button loginButton = new Button("Sign In");
        loginButton.getStyleClass().add("secondary"); // Apply CSS class
        loginButton.setOnAction(e -> mainApp.showLoginScreen());

        // Register Button
        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("primary"); // Apply CSS class
        registerButton.setOnAction(e -> handleRegistration());

        buttonBox.getChildren().addAll(loginButton, registerButton);
        grid.add(buttonBox, 0, 4);
    }

    private void handleRegistration() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Validation Error", "Please fill in all fields", Alert.AlertType.ERROR);
            return;
        }

        try {
            ApiClient.registerUser(username, email, password);
            showAlert("Registration Successful",
                    "Account created! Please log in with your credentials.",
                    Alert.AlertType.INFORMATION);
            clearFields();
        } catch (Exception e) {
            showAlert("Registration Failed", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearFields() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        alert.showAndWait();
    }
}