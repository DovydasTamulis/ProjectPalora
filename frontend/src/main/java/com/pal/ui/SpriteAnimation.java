package com.pal.ui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class SpriteAnimation {
    private final ImageView imageView;
    private final Timeline timeline;

    public SpriteAnimation(ImageView imageView, Duration frameDuration) {
        this.imageView = imageView;
        this.timeline = new Timeline(
                new KeyFrame(frameDuration, e -> nextFrame())
        );
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    private void nextFrame() {
        // Example: Cycle through 4 frames horizontally
        Rectangle2D viewport = imageView.getViewport();
        double newX = (viewport.getMinX() + 64) % 256; // 64px per frame, 4 frames
        imageView.setViewport(new Rectangle2D(newX, 0, 64, 64));
    }

    public void play() {
        timeline.play();
    }
}