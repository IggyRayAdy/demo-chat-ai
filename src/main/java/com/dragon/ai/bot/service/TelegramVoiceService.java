package com.dragon.ai.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramVoiceService {

    private final ChatGptService chatGptService;
    private final TelegramFileService telegramFileService;

    public SendMessage handleVoiceMessage(Message inMessage) {
        var chatId = inMessage.getChatId();
        var fileId = inMessage.getVoice().getFileId();

        var file = reciveFile(fileId);

        var outMessage = chatGptService.transcribeAudio(file);

        return UtilsService.buildSendMessage(chatId, outMessage);
    }

    private File reciveFile(String fileId) {
        File file = null;

        try {
            file = telegramFileService.getFile(fileId);
        } catch (Exception e) {
            log.error("Exception on file {} download: {}", fileId, e.getMessage(), e);
        }

        return file;
    }


}
