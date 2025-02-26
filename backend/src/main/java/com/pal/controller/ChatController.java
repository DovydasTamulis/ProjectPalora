package com.pal.controller;

import com.pal.service.ChatService;
import com.pal.service.LumiService;
import com.pal.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private LumiService lumiService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/send")
    public String sendMessage(@RequestHeader("Authorization") String token,
                              @RequestParam String message) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        chatService.saveMessage(email, "user", message);

        List<Map<String, String>> messages = chatService.getConversationHistory(email).stream()
                .map(entry -> Map.of(
                        "role", entry.getContent().startsWith("user:") ? "user" : "assistant",
                        "content", entry.getContent()))
                .collect(Collectors.toList());

        String lumiResponse = lumiService.getLumiResponse(messages);
        chatService.saveMessage(email, "lumi", lumiResponse);
        return lumiResponse;
    }
}