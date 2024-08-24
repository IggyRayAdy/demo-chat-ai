package com.dragon.ai.bot;

import com.dragon.ai.bot.configuration.property.BotProperties;
import com.dragon.ai.bot.configuration.TelegramHandlerConfigurations;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        BotProperties.class
})
public class DemoChatAiApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(DemoChatAiApplication.class, args);

        ctx.getBean(TelegramHandlerConfigurations.class);
    }

}
