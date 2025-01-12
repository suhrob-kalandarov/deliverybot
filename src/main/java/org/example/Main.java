package org.example;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.example.services.TelegramService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static TelegramBot telegramBot = new TelegramBot("7547637717:AAGt5ogiQyPuiDHRlgst0-loAR399SIHSTE");
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        telegramBot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                executorService.execute(() -> {
                    TelegramService.handle(update);
                });
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}