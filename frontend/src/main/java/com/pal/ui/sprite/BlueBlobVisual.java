package com.pal.ui.sprite;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class BlueBlobVisual extends Pane {
    private final Canvas canvas;
    private final int pixelSize = 25; // Size multiplier for scaling
    private double xOffset = 0; // Horizontal offset for animation
    private double yOffset = 0; // Vertical offset for animation
    private long startTime; // Track animation start time

    public BlueBlobVisual() {
        canvas = new Canvas(16 * pixelSize, 16 * pixelSize);
        getChildren().add(canvas);
        startTime = System.nanoTime();

        // Start the animation loop
        startAnimation();
    }

    private void startAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double elapsedTime = (now - startTime) / 1_000_000_000.0; // Convert to seconds

                // Smooth bobbing animation
                xOffset = Math.sin(elapsedTime * 2) * 3; // Horizontal bobbing
                yOffset = Math.cos(elapsedTime * 3) * 3; // Vertical bobbing

                // Redraw the sprite
                drawSprite(elapsedTime);
            }
        };
        timer.start();
    }

    private void drawSprite(double elapsedTime) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Define colors
        Color blobColor = Color.web("#0074D9"); // Blue color
        Color glowColor = Color.web("#7FDBFF", 0.4); // Light blue glow

        // Draw glowing aura around the blob
        RadialGradient glowGradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.6, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new Stop(0, glowColor),
                new Stop(1, Color.TRANSPARENT)
        );
        gc.setFill(glowGradient);
        gc.fillOval(
                (8 - 4 + xOffset) * pixelSize,
                (8 - 4 + yOffset) * pixelSize,
                16 * pixelSize,
                16 * pixelSize
        );

        // Draw the main blob body
        gc.setFill(blobColor);
        gc.fillOval(
                (8 - 4 + xOffset) * pixelSize,
                (8 - 4 + yOffset) * pixelSize,
                8 * pixelSize,
                8 * pixelSize
        );

        // Add animated "eyes" with smooth independent movement
        double eyeMovementX = Math.sin(elapsedTime * 4) * 1.5; // Smoother, more natural eye movement
        double eyeMovementY = Math.cos(elapsedTime * 2) * 1.5;

        gc.setFill(Color.WHITE);
        gc.fillOval(
                (9 + xOffset + eyeMovementX) * pixelSize,
                (9 + yOffset + eyeMovementY) * pixelSize,
                pixelSize,
                pixelSize
        );
        gc.fillOval(
                (12 + xOffset - eyeMovementX) * pixelSize,
                (9 + yOffset + eyeMovementY) * pixelSize,
                pixelSize,
                pixelSize
        );

        // Add pupils inside the eyes
        gc.setFill(Color.BLACK);
        gc.fillOval(
                (9.5 + xOffset + eyeMovementX) * pixelSize,
                (9.5 + yOffset + eyeMovementY) * pixelSize,
                pixelSize / 2,
                pixelSize / 2
        );
        gc.fillOval(
                (12.5 + xOffset - eyeMovementX) * pixelSize,
                (9.5 + yOffset + eyeMovementY) * pixelSize,
                pixelSize / 2,
                pixelSize / 2
        );
    }
}
