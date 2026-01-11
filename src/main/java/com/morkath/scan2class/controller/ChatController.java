package com.morkath.scan2class.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.service.GeminiService;
import com.morkath.scan2class.service.RAGContextService;
import com.morkath.scan2class.service.UserService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private RAGContextService ragContextService;

    @Autowired
    private UserService userService;

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> ask(@RequestBody Map<String, String> payload) {
        try {
            UserEntity currentUser = userService.getCurrent();
            String userQuestion = payload.get("question");

            // 1. Build Context
            String contextJson = ragContextService.getContextForUser(currentUser);
            // 2. Construct Prompt (RAG)
            StringBuilder prompt = new StringBuilder();
            prompt.append("You are 'S2C Bot', a helpful assistant for Scan2Class attendance system.\n");
            prompt.append("Current User: " + currentUser.getFullname() + "\n");
            prompt.append("Context Data (JSON): " + contextJson + "\n");

            prompt.append("Policies: 3 LATE = 1 ABSENT. Max 20% ABSENT allowed.\n");
            prompt.append("User Question: " + userQuestion + "\n");
            prompt.append(
                    "Instruction: Answer the user's question based on the JSON data. The JSON has keys 'teaching' (classes user owns) and 'learning' (classes user joins). Data includes attendance stats and last session time. Answer in Vietnamese. Be short, friendly and helpful.\n");

            // 3. Call AI
            String answer = geminiService.generateResponse(prompt.toString());

            return ResponseEntity.ok(Map.of("answer", answer));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("answer", "Xin lỗi, đã có lỗi xảy ra."));
        }
    }
}
