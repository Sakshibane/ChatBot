package ChatBotForWhatsApp.Project;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class WhatsAppChatbot extends TelegramLongPollingBot {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String userMessage = update.getMessage().getText();

            // Call the AI Model API
            String aiResponse = getAIResponse(userMessage);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(aiResponse);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private String getAIResponse(String message) {
        String url = "http://localhost:8080/chat"; // Spring Boot API URL

        Map<String, String> request = new HashMap<>();
        request.put("message", message);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return response.getBody();
    }

    @Override
    public String getBotUsername() {
        return "harshitsak_bot";
    }

    @Override
    public String getBotToken() {
        return "7514451867:AAGb4uy65A8pi1xaeZ7qVfsb736uT6-Tr1E";
    }
}
