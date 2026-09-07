package org.example;

import com.sun.net.httpserver.HttpServer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Запускаем микро-веб-сервер для Render, чтобы он видел открытый порт и не выключал бесплатный сервис
            int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "10000"));
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", exchange -> {
                String response = "Bot is running!";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            });
            server.setExecutor(null);
            server.start();
            System.out.println("Web server started on port " + port);

            // 2. Запускаем нашего Telegram-бота
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new NailBot());
            System.out.println("NailBot successfully started!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}