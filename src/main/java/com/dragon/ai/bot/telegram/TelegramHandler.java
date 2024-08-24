package com.dragon.ai.bot.telegram;

import com.dragon.ai.bot.service.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class TelegramHandler extends TelegramLongPollingBot {

    private final String login;
    private final TelegramService telegramService;

    public TelegramHandler(DefaultBotOptions options,
                           String token,
                           String login,
                           TelegramService telegramService
    ) {
        super(options, token);
        this.login = login;
        this.telegramService = telegramService;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage()) {
            log.error("Update with id {} has empty message.", update.getUpdateId());
            throw new RuntimeException("todo");
        }

        Message inMessage = update.getMessage();

        SendMessage outMessage = telegramService.recognizeMessageType(inMessage);

        getMessage(outMessage);

    }

    private void getMessage(SendMessage sendMessage) {
        try {
            this.sendApiMethod(sendMessage);
        } catch (Exception e) {
            log.error("{} {}", e.getMessage(), sendMessage.getChatId(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return this.login;
    }
}
