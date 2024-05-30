package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        MyAmazingBot bot = new MyAmazingBot();
            botsApi.registerBot(bot);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();

        }
    }
}