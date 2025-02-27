import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.web.client.RestTemplate;

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
        String url = "http://localhost:5000/chat";  // Ollama API URL
        return restTemplate.postForObject(url, "{\"message\": \"" + message + "\"}", String.class);
    }

    @Override
    public String getBotUsername() {
        return "my_chat_bot";
    }

    @Override
    public String getBotToken() {
        return "YOUR_BOT_TOKEN_HERE";
    }
}
