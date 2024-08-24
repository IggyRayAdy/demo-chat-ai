package com.dragon.ai.bot.configuration.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "telegram.bot")
public record BotProperties(String login, String token, boolean optionsEnable, Map<String, String> options) {

    public String getProperty(String key) {
        return options.get(key);
    }
}
