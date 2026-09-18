package com.healthcare.ai.service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;       
import java.net.http.HttpResponse;

@Service
public class AiService {

    private static final String OLLAMA_URL =
            "http://localhost:11434/api/generate";

    private static final String MODEL =
            "llama3.2:3b";

    private static final String HEALTHCARE_INSTRUCTIONS = """
            You are a Healthcare Management System AI Assistant.

            Your role is to provide general and educational healthcare
            information in simple language.

            Follow these rules:

            1. Do not diagnose diseases or claim certainty about a patient's
               medical condition.

            2. Do not prescribe medicines or recommend changing medication
               dosages.

            3. Do not tell patients to stop prescribed medicines.

            4. If a user describes symptoms, explain that there can be
               multiple possible causes and recommend consulting a qualified
               healthcare professional.

            5. If the situation sounds like a medical emergency, advise the
               user to seek emergency medical care immediately.

            6. Do not invent patient records, medical history, laboratory
               results, prescriptions, or diagnoses.

            7. If patient information is not provided, do not assume it.

            8. Use simple language that patients and healthcare staff can
               understand.

            9. Do not provide dangerous instructions or encourage unsafe
               self-treatment.

            10. When the question involves diagnosis, treatment, medication,
                or serious symptoms, clearly explain that the response is for
                informational purposes and does not replace professional
                medical advice.

            Answer the user's question directly and concisely.
            """;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AiService(ObjectMapper objectMapper) {

        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper;
    }

    public String askQuestion(String question) {

        try {

            String prompt =
                    HEALTHCARE_INSTRUCTIONS
                    + "\n\nUser question:\n"
                    + question;

            String requestBody = objectMapper.writeValueAsString(
                    new OllamaRequest(
                            MODEL,
                            prompt,
                            false
                    )
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OLLAMA_URL))
                    .header("Content-Type", "application/json")
                    .POST(
                            HttpRequest.BodyPublishers.ofString(
                                    requestBody
                            )
                    )
                    .build();

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() != 200) {

                return "The local AI service is currently unavailable. "
                        + "Please make sure Ollama is running.";
            }

            JsonNode jsonResponse =
                    objectMapper.readTree(response.body());

            JsonNode answer =
                    jsonResponse.get("response");

            if (answer == null || answer.asText().isBlank()) {

                return "Sorry, I could not generate an AI response.";
            }

            return answer.asText();

        } catch (Exception e) {

            return "The Healthcare AI Assistant is currently unavailable. "
                    + "Please make sure Ollama is running.";
        }
    }

    private record OllamaRequest(
            String model,
            String prompt,
            boolean stream
    ) {
    }
}