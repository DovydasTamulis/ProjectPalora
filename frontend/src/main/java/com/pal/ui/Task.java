package com.pal.ui;

import javafx.beans.property.*;

public class Task {
    private StringProperty id;
    private StringProperty title;
    private StringProperty description;
    private LongProperty duration; // New duration property
    private LongProperty elapsedTime; // New elapsed time property
    private BooleanProperty completed;
    private StringProperty status;
    private ObjectProperty<java.time.LocalDateTime> createdAt; // New timestamp
    private ObjectProperty<java.time.LocalDateTime> expiresAt; // New expiration time

    public Task(String id, String title, String description, long duration,
                long elapsedTime, boolean completed, String status,
                java.time.LocalDateTime createdAt, java.time.LocalDateTime expiresAt) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.duration = new SimpleLongProperty(duration);
        this.elapsedTime = new SimpleLongProperty(elapsedTime);
        this.completed = new SimpleBooleanProperty(completed);
        this.status = new SimpleStringProperty(status);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.expiresAt = new SimpleObjectProperty<>(expiresAt);
    }

    // Add new property getters
    public LongProperty durationProperty() { return duration; }
    public LongProperty elapsedTimeProperty() { return elapsedTime; }
    public ObjectProperty<java.time.LocalDateTime> createdAtProperty() { return createdAt; }
    public ObjectProperty<java.time.LocalDateTime> expiresAtProperty() { return expiresAt; }

    // Add getters/setters for new fields
    public long getDuration() { return duration.get(); }
    public void setDuration(long duration) { this.duration.set(duration); }

    public long getElapsedTime() { return elapsedTime.get(); }
    public void setElapsedTime(long elapsedTime) { this.elapsedTime.set(elapsedTime); }

    // ... keep existing getters/setters ...
}