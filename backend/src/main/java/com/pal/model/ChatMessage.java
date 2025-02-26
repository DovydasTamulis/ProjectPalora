package com.pal.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "chat_messages")
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @Id
    private String userId;
    private List<MessageEntry> userPrompts = new ArrayList<>();
    private List<MessageEntry> lumiPrompts = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageEntry {
        private String content;
        private LocalDateTime timestamp;
    }
}