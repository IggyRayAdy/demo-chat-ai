package com.dragon.ai.bot.service;

import com.dragon.ai.bot.model.TranscriptionResponse;
import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.AudioContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.List;

@Service
public class ChatGptService {

    private final RestClient openAiHttpClient;
    private final ChatContextService chatContextService;
    private final OpenAiChatModel openAiChatModel;
    private final ChatLanguageModel customChatLanguageModel;

    public ChatGptService(RestClient openAiHttpClient,
                          ChatContextService chatContextService,
                          @Qualifier("openAiChatModel") OpenAiChatModel openAiChatModel,
                          @Qualifier("customChatLanguageModel") ChatLanguageModel customChatLanguageModel
    ) {
        this.openAiHttpClient = openAiHttpClient;
        this.chatContextService = chatContextService;
        this.openAiChatModel = openAiChatModel;
        this.customChatLanguageModel = customChatLanguageModel;
    }

    public String chatMessaging(Long chatId, String userTextMessage) {

        UserMessage userMessage = UserMessage.userMessage(userTextMessage);

        var messageWithContext = chatContextService.getChatContext(chatId, userMessage);

        AiMessage aiResponse = openAiChatModel.generate(messageWithContext).content();

        chatContextService.updateChatContext(chatId, List.of(userMessage, aiResponse));

        return aiResponse.text();
    }

    @SneakyThrows
    @Deprecated(forRemoval = true, since = "уточнить как чз langchain4j прокидывать аудио")
    public String transcribeAudio(String fileUrl) {

        URL url = new URL(fileUrl);

        byte[] audioBytes;

        try (InputStream in = url.openStream()) {
            audioBytes = IOUtils.toByteArray(in);
        }

        String base64Data = Base64.getEncoder().encodeToString(audioBytes);
        String mimeType = "audio/ogg";

        Audio audio = Audio.builder()
                .url(fileUrl)
                .base64Data(base64Data)
                .mimeType(mimeType)
                .build();

        AudioContent audioContent = AudioContent.from(audio);

        UserMessage message = UserMessage.from(audioContent);

        Response<AiMessage> response = customChatLanguageModel.generate(message);

        return response.content().text();
    }

    public String transcribeAudio(File file) {
        if (file == null) {
            return UtilsService.NOT_HANDLED_MESSAGE;
        }

        var entity = getHttpEntity(file);

        TranscriptionResponse response = openAiHttpClient.post()
                .body(entity.getBody())
                .retrieve()
                .body(TranscriptionResponse.class);

        return response.text();
    }

    private HttpEntity<MultiValueMap<String, Object>> getHttpEntity(File file) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(file));
        body.add("model", "whisper-1");

        return new HttpEntity<>(body);
    }
}
