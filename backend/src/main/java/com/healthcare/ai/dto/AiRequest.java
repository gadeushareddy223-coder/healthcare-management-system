package com.healthcare.ai.dto;

import jakarta.validation.constraints.NotBlank;

public record AiRequest(

        @NotBlank(message = "Question is required")
        String question

) {
}