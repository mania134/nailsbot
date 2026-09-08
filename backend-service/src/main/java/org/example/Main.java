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
            // 1. Запускаем микро-веб-сервер для Render
            int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "10000"));
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            // Обработчик для главной страницы или проверки жизни
            server.createContext("/", exchange -> {
                String response = "Bot is running!";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            });

            // Обработчик для получения услуг (наш API бэкенда)
            server.createContext("/api/v1/services", exchange -> {
                if ("GET".equals(exchange.getRequestMethod())) {
                    String jsonResponse = "[" +
                            "{\"title\": \"Маникюр классический\", \"price\": 2000}," +
                            "{\"title\": \"Наращивание ногтей\", \"price\": 3500}," +
                            "{\"title\": \"Покрытие гель-лак\", \"price\": 1500}" +
                            "]";

                    exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                    byte[] bytes = jsonResponse.getBytes(java.nio.charset.StandardCharsets.UTF_8);

                    exchange.sendResponseHeaders(200, bytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(bytes);
                    os.close();
                } else {
                    exchange.sendResponseHeaders(405, -1);
                }
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