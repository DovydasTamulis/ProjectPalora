package com.pal.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LumiService {
    @Value("${lumi.prompt}")
    private String SYSTEM_PROMPT;


    public String getLumiResponse(List<Map<String, String>> messages) {
        List<Map<String, String>> fullMessages = new ArrayList<>();
        fullMessages.add(Map.of("role", "system", "content", SYSTEM_PROMPT));
        fullMessages.addAll(messages);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", fullMessages);
        requestBody.put("model", "qwen2.5-7b-instruct-1m");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        String response = restTemplate.postForObject("http://localhost:1234/v1/chat/completions", request, String.class);

        return parseLumiReply(response);
    }

    private String parseLumiReply(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Lumi couldn’t process that right now!";
        }
    }

}
