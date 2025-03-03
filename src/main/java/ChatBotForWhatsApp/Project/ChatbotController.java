package ChatBotForWhatsApp.Project;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatbotController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public String getChatResponse(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String ollamaUrl = "http://localhost:11434/api/generate"; // Ollama's API endpoint

        // Request payload for Ollama API
        String requestBody = String.format("{\"model\": \"whatsapp-bot-final\", \"prompt\": \"%s\"}", userMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(ollamaUrl, entity, Map.class);

        // Extract AI response
        return response.getBody().get("response").toString();
    }
}
