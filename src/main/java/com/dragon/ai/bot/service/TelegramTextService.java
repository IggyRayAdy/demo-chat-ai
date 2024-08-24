package com.dragon.ai.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Service
@RequiredArgsConstructor
public class TelegramTextService {

    private final ChatGptService chatGptService;

    public SendMessage handleTextMessage(Message inMessage) {
        var chatId = inMessage.getChatId();
        var userMessage = inMessage.getText();

        var outMessage = chatGptService.chatMessaging(chatId, userMessage);

        return UtilsService.buildSendMessage(chatId, outMessage);
    }
}
