package org.example;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class NailBot extends TelegramLongPollingBot {

    public NailBot() {
        super(createOptions());
    }

    private static DefaultBotOptions createOptions() {
        DefaultBotOptions options = new DefaultBotOptions();
        // Используем зеркало Telegram API
        options.setBaseUrl("https://api.telegram.org/bot");
        return options;
    }

    @Override
    public String getBotUsername() {
        return "anastanails_bot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("--> Получено сообщение в бот!");
        if (update.hasMessage() && update.getMessage().hasText()) {
            String userText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Привет! Ты написала: " + userText);

            try {
                execute(message);
                System.out.println("<-- Ответ успешно отправлен!");
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}