package com.healthcare.ai.controller;

import com.healthcare.ai.dto.AiRequest;
import com.healthcare.ai.dto.AiResponse;
import com.healthcare.ai.service.AiService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ask")
    public ResponseEntity<AiResponse> askQuestion(
            @Valid @RequestBody AiRequest request) {

        String answer = aiService.askQuestion(
                request.question()
        );

        return ResponseEntity.ok(
                new AiResponse(answer)
        );
    }
}