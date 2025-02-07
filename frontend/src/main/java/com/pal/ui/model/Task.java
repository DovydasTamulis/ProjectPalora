package com.pal.ui.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Task {
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final BooleanProperty completed = new SimpleBooleanProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final LongProperty duration = new SimpleLongProperty();
    private final LongProperty elapsedTime = new SimpleLongProperty();
    private final ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> expiresAt = new SimpleObjectProperty<>();

    public Task(String id, String title, String description, long duration, long elapsedTime,
                boolean completed, String status, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.id.set(id);
        this.title.set(title);
        this.description.set(description);
        this.duration.set(duration);
        this.elapsedTime.set(elapsedTime);
        this.completed.set(completed);
        this.status.set(status);
        this.createdAt.set(createdAt);
        this.expiresAt.set(expiresAt);
    }

    // Getters and Setters
    public StringProperty idProperty() { return id; }
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public StringProperty titleProperty() { return title; }
    public String getTitle() { return title.get(); }
    public void setTitle(String title) { this.title.set(title); }

    public StringProperty descriptionProperty() { return description; }
    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public BooleanProperty completedProperty() { return completed; }
    public boolean isCompleted() { return completed.get(); }
    public void setCompleted(boolean completed) { this.completed.set(completed); }

    public StringProperty statusProperty() { return status; }
    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }

    public LongProperty durationProperty() { return duration; }
    public long getDuration() { return duration.get(); }
    public void setDuration(long duration) { this.duration.set(duration); }

    public LongProperty elapsedTimeProperty() { return elapsedTime; }
    public long getElapsedTime() { return elapsedTime.get(); }
    public void setElapsedTime(long elapsedTime) { this.elapsedTime.set(elapsedTime); }

    public ObjectProperty<LocalDateTime> createdAtProperty() { return createdAt; }
    public LocalDateTime getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt.set(createdAt); }

    public ObjectProperty<LocalDateTime> expiresAtProperty() { return expiresAt; }
    public LocalDateTime getExpiresAt() { return expiresAt.get(); }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt.set(expiresAt); }
}