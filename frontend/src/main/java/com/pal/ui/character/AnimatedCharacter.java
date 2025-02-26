package com.pal.ui.character;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.HashMap;
import java.util.Map;

public class AnimatedCharacter extends Pane {
    private final int FPS = 8; // Frames per second
    private Map<String, Image[]> animations = new HashMap<>();
    private ImageView imageView;
    private AnimationTimer timer;
    private String currentAnimation;
    private int currentFrame = 0;
    private long lastUpdate = 0;

    public AnimatedCharacter() {
        imageView = new ImageView();
        getChildren().add(imageView);
        loadAnimations();
        setupAnimationTimer();
    }

    private void loadAnimations() {
        // Load all animations from spriteConfig directory
        animations.put("idle", loadFrames("idle", 4));
        animations.put("run", loadFrames("run", 4));
        animations.put("hurt", loadFrames("hurt", 4));
        animations.put("die", loadFrames("die", 4));
    }

    private Image[] loadFrames(String action, int frameCount) {
        Image[] frames = new Image[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String path = String.format("/spriteConfig/sprite__%s_%d.png", action, i);
            frames[i] = new Image(getClass().getResourceAsStream(path));
        }
        return frames;
    }

    private void setupAnimationTimer() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 1_000_000_000L / FPS) {
                    updateFrame();
                    lastUpdate = now;
                }
            }
        };
    }

    private void updateFrame() {
        if (currentAnimation == null) return;
        Image[] currentFrames = animations.get(currentAnimation);
        imageView.setImage(currentFrames[currentFrame]);
        currentFrame = (currentFrame + 1) % currentFrames.length;
    }

    // Public control methods
    public void playAnimation(String animationName) {
        if (!animations.containsKey(animationName)) return;
        currentAnimation = animationName;
        currentFrame = 0;
        timer.start();
    }

    public void stopAnimation() {
        timer.stop();
    }

    // Getters for UI integration
    public ImageView getImageView() {
        return imageView;
    }

    public String getCurrentAnimation() {
        return currentAnimation;
    }
}