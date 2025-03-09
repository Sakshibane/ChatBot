package ChatBotForWhatsApp.Project;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatbotController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("")

    public ResponseEntity<String> getChatResponse(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String response = callOllamaModel(userMessage);

        System.out.println("Raw Ollama Response: " + response);  // Debugging Log

        return ResponseEntity.ok(response);

        // Extract AI response
       // return response.getBody().get("response").toString();
    }

    private String callOllamaModel(String prompt) {
        try {
            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Include "stream": false to get a full response
            String jsonInput = "{ \"model\": \"whatsapp-bot-final\", \"prompt\": \"" + prompt + "\", \"stream\": false }";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // Convert String response to JSON Object
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Extract only the "response" field
            return jsonResponse.optString("response", "No response received");

        } catch (Exception e) {
            e.printStackTrace();
            return "Error connecting to Ollama.";
        }
    }

}
