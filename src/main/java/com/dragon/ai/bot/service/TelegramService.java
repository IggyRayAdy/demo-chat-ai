package com.dragon.ai.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService {

    private final TelegramTextService telegramTextService;

    private final TelegramVoiceService telegramVoiceService;

    public SendMessage recognizeMessageType(Message inMessage) {

        if (!inMessage.hasText() && !inMessage.hasVoice()) {
            Long chatId = inMessage.getChatId();
            return UtilsService.buildSendMessage(chatId, UtilsService.NOT_IMPLEMENTED_MESSAGE);
        }

        return handleMessage(inMessage);
    }

    private SendMessage handleMessage(Message inMessage) {
        SendMessage outMessage;

        if (inMessage.hasVoice()) {
            outMessage = telegramVoiceService.handleVoiceMessage(inMessage);
        } else {
            outMessage = telegramTextService.handleTextMessage(inMessage);
        }

        outMessage.setParseMode(ParseMode.MARKDOWNV2);
        return outMessage;
    }


}
