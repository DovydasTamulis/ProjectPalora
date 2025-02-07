package com.pal.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Document(collection = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PalTask {
    @Id
    private String id;
    private String userId;
    private String title;
    private String description;
    private long duration;
    private long elapsedTime;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean completed;
    private boolean running;

    public PalTask(String userId, String title, String description, long duration) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.elapsedTime = 0;
        this.createdAt = LocalDateTime.now();
        this.expiresAt =  LocalDateTime.now().plusSeconds(this.duration);
        this.completed = false;
        this.running = false;
    }
}