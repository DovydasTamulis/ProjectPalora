package com.pal.service;

import com.pal.model.ChatMessage;
import com.pal.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final int MAX_PROMPTS = 1000;

    public void saveMessage(String userId, String sender, String content) {
        ChatMessage.MessageEntry entry = new ChatMessage.MessageEntry(content, LocalDateTime.now());

        Query query = new Query(Criteria.where("userId").is(userId));
        ChatMessage chatMessage = mongoTemplate.findOne(query, ChatMessage.class);

        if (chatMessage == null) {
            chatMessage = new ChatMessage();
            chatMessage.setUserId(userId);
        }

        List<ChatMessage.MessageEntry> prompts;

        if ("user".equals(sender)) {
            prompts = chatMessage.getUserPrompts();
        } else if ("lumi".equals(sender)) {
            prompts = chatMessage.getLumiPrompts();
        } else {
            return;
        }

        if (prompts == null) {
            prompts = new ArrayList<>();
        }

        prompts.add(entry);

        if (prompts.size() > MAX_PROMPTS) {
            prompts = prompts.subList(prompts.size() - MAX_PROMPTS, prompts.size());
        }

        if ("user".equals(sender)) {
            chatMessage.setUserPrompts(prompts);
        } else {
            chatMessage.setLumiPrompts(prompts);
        }

        mongoTemplate.save(chatMessage);
    }


    public List<ChatMessage.MessageEntry> getConversationHistory(String userId) {
        ChatMessage chatMessage = chatMessageRepository.findByUserId(userId);

        if (chatMessage == null) {
            return new ArrayList<>();
        }

        List<ChatMessage.MessageEntry> combined = new ArrayList<>();
        List<ChatMessage.MessageEntry> userPrompts = chatMessage.getUserPrompts();
        List<ChatMessage.MessageEntry> lumiPrompts = chatMessage.getLumiPrompts();

        int maxSize = Math.max(userPrompts.size(), lumiPrompts.size());
        for (int i = 0; i < maxSize; i++) {
            if (i < userPrompts.size()) {
                combined.add(userPrompts.get(i));
            }
            if (i < lumiPrompts.size()) {
                combined.add(lumiPrompts.get(i));
            }
        }

        combined.sort((e1, e2) -> e1.getTimestamp().compareTo(e2.getTimestamp()));
        return combined;
    }
}