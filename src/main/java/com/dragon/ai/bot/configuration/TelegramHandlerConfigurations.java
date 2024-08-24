package com.dragon.ai.bot.configuration;

import com.dragon.ai.bot.service.TelegramService;
import com.dragon.ai.bot.configuration.property.BotProperties;
import com.dragon.ai.bot.telegram.TelegramHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TelegramHandlerConfigurations {

    private final BotProperties botProperties;
    private final TelegramService telegramService;

    @Bean
    public TelegramHandler telegramBot() {

        TelegramHandler bot = new TelegramHandler(new DefaultBotOptions(),
                botProperties.token(),
                botProperties.login(),
                telegramService
        );

        registrationBot(bot);

        return bot;
    }


    @Bean
    public DefaultBotOptions botOptions() {
        DefaultBotOptions defaultBotOptions = new DefaultBotOptions();

        if (botProperties.optionsEnable()) {
            customizeBotOptions(defaultBotOptions);
        }

        return defaultBotOptions;
    }

    private void customizeBotOptions(DefaultBotOptions options) {
        //todo: research
        options.setProxyPort(Integer.parseInt(botProperties.getProperty("proxyPort")));
        options.setProxyHost(botProperties.getProperty("proxyHost"));
        options.setProxyHost(botProperties.getProperty("proxyType"));
    }

    private void registrationBot(TelegramHandler bot) {
        try {

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("Failed to register Telegram bot {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to register Telegram bot", e);
        }
    }

}
