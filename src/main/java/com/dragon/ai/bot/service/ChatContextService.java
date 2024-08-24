package com.dragon.ai.bot.service;

import dev.langchain4j.data.message.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
@Deprecated(since = "use conversationalChain")
public class ChatContextService {

    private final Map<Long, LinkedList<ChatMessage>> chatContextMap = new ConcurrentHashMap<>();

    public void updateChatContext(Long chatId, List<ChatMessage> messages) {

        var chatContextList = getContextList(chatId);

        chatContextList.addAll(messages);
    }

    private LinkedList<ChatMessage> getContextList(Long chatId) {
        if (!chatContextMap.containsKey(chatId)) {
            chatContextMap.put(chatId, new LinkedList<ChatMessage>());
        }

        var chatContextList = chatContextMap.get(chatId);

        if (chatContextList.size() == 25) {
            chatContextList.removeFirst();
        }

        return chatContextList;
    }

    public List<ChatMessage> getChatContext(Long chatId, ChatMessage message) {
        var existedContext = chatContextMap.getOrDefault(chatId, new LinkedList<>());

        return Stream.concat(existedContext.stream(), Stream.of(message)).toList();
    }

}
