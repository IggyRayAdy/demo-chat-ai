package com.dragon.ai.bot.configuration;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Configuration
public class OpenAiRestClientConfigurations {

    private final static String BASE_TRANSCRIPTIONS_URL = "https://api.openai.com/v1/audio/transcriptions";

    @Value("${langchain4j.open-ai.chat-model.api-key}")
    private String chatToken;

    @Bean
    public RestClient openAiHttpClient() {

        return RestClient.builder()
                .baseUrl(BASE_TRANSCRIPTIONS_URL)
                .defaultHeader(AUTHORIZATION, "Bearer %s".formatted(chatToken))
                .defaultHeader(CONTENT_TYPE, MULTIPART_FORM_DATA_VALUE)
                .build();
    }

    @Bean
    public ConversationalChain conversationalChain() {
        return ConversationalChain.builder()
                .chatLanguageModel(OpenAiChatModel.withApiKey(chatToken))
                .build();
    }

    @Bean
    @Qualifier("customChatLanguageModel")
    public OpenAiChatModel customChatLanguageModel() {

        return OpenAiChatModel.builder()
                .apiKey(chatToken)
                .modelName("whisper-1")
                .baseUrl(BASE_TRANSCRIPTIONS_URL)
                .customHeaders(
                        Map.of(
                                AUTHORIZATION, "Bearer %s".formatted(chatToken),
                                CONTENT_TYPE, MULTIPART_FORM_DATA_VALUE
                        )
                )
                .maxRetries(1)
                .build();
    }
}