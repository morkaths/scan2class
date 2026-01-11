package com.morkath.scan2class.service;

import org.springframework.stereotype.Service;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final Client client;

    public GeminiService(@Value("${gemini.api.key}") String apiKey) {
        // Explicitly pass the API Key from properties to the Client
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public String generateResponse(String prompt) {
        try {
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    prompt,
                    null);
            return response.text();
        } catch (Exception e) {
            // Fallback or retry logic could go here
            e.printStackTrace();
            return "Xin lỗi, tôi đang gặp sự cố kết nối AI (" + e.getMessage() + ")";
        }
    }
}
