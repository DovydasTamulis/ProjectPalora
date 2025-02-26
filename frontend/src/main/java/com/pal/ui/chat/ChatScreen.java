package com.pal.ui.chat;

import com.pal.ui.MainApp;
import com.pal.ui.character.AnimatedCharacter;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import static com.pal.ui.api.ApiClient.sendMessageWithAuth;

public class ChatScreen extends HBox {
    private final MainApp mainApp;
    private AnimatedCharacter character;
    private TextArea conversationArea;
    private TextField userInputField;
    private Button sendButton;

    public ChatScreen(MainApp mainApp) {
        this.mainApp = mainApp;
        getStylesheets().add(getClass().getResource("/css/main-styles.css").toExternalForm());
        initializeUI();
    }

    private void initializeUI() {
        // Character on the left
        character = new AnimatedCharacter();
        ImageView characterView = character.getImageView();
        characterView.setFitWidth(192);
        characterView.setFitHeight(288);
        characterView.setPreserveRatio(true);
        character.playAnimation("idle");

        // Chat components on the right
        conversationArea = new TextArea();
        conversationArea.setEditable(false);
        conversationArea.setWrapText(true);
        conversationArea.getStyleClass().add("chat-text-area");

        userInputField = new TextField();
        userInputField.setPromptText("Type your message to Lumi...");
        userInputField.getStyleClass().add("chat-input-field");

        sendButton = new Button("Send");
        sendButton.getStyleClass().add("chat-send-button");
        sendButton.setOnAction(e -> sendMessage());

        VBox chatBox = new VBox(10, conversationArea, userInputField, sendButton);
        chatBox.getStyleClass().add("chat-box");
        chatBox.setPadding(new Insets(10));

        // Layout
        getChildren().addAll(characterView, chatBox);
        setHgrow(chatBox, Priority.ALWAYS);
    }

    private void sendMessage() {
        String userMessage = userInputField.getText().trim();
        if (userMessage.isEmpty()) return;

        conversationArea.appendText("You: " + userMessage + "\n");
        userInputField.clear();

        try {
            String response = sendMessageWithAuth(mainApp.getAuthToken(), userMessage);
            javafx.application.Platform.runLater(() -> {
                conversationArea.appendText("Lumi: " + formatResponse(response) + "\n");
            });
        } catch (Exception e) {
            javafx.application.Platform.runLater(() -> {
                conversationArea.appendText("Error: " + e.getMessage() + "\n");
            });
        }
    }

    private String formatResponse(String response) {
        String[] lines = response.split("\n");
        StringBuilder formatted = new StringBuilder();
        for (String line : lines) {
            if (line.trim().startsWith("-") || line.trim().startsWith("*")) {
                formatted.append("  • ").append(line.trim().substring(1).trim()).append("\n");
            } else if (line.trim().matches("\\d+\\..*")) {
                formatted.append("  ").append(line.trim()).append("\n");
            } else {
                formatted.append(line).append("\n");
            }
        }
        return response
                .replaceAll("(?m)^-", "\u2605 ")
                .replaceAll("(?m)^\\*", "\u2728 ")
                .replaceAll("(?m)^\\d+\\.", "\uD83D\uDCDD ");
    }
}