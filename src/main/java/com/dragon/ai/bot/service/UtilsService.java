package com.dragon.ai.bot.service;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@UtilityClass
public class UtilsService {

    public final static String EMPTY_MESSAGE = "Your response is empty";
    public final static String NOT_HANDLED_MESSAGE = "Sorry, we cant handle our message, try again later :(";
    public final static String NOT_IMPLEMENTED_MESSAGE = "Sorry, we cant handle our message yet :(";

    public static SendMessage buildSendMessage(Long chatId, String outMessage) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(escapeText(outMessage))
                .build();
    }

    private String escapeText(String text) {
        final String[] SPECIAL_CHARACTERS = new String[]{
                "_", "*", "[", "]", "(", ")", "~", "`", ">", "#", "+", "-", "=", "|", "{", "}", ".", "!"
        };

        if (text == null || text.isEmpty()) {
            text = EMPTY_MESSAGE;
        }

        for (String charToEscape : SPECIAL_CHARACTERS) {
            text = text.replace(charToEscape, "\\" + charToEscape);
        }
        return text;
    }
}
