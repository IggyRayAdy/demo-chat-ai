package com.dragon.ai.bot.service;

import com.dragon.ai.bot.configuration.property.BotProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Slf4j
@Service
public class TelegramFileService {


    private final BotProperties botProperties;
    private final DefaultAbsSender fileDownloader;

    public TelegramFileService(@Lazy DefaultAbsSender fileDownloader, BotProperties botProperties) {
        this.fileDownloader = fileDownloader;
        this.botProperties = botProperties;
    }

    public java.io.File getFile(String fileId) throws TelegramApiException, IOException, URISyntaxException {
        GetFile getFile = GetFile.builder()
                .fileId(fileId)
                .build();

        File tgFile = fileDownloader.execute(getFile);

        String urlToDownloadFile = tgFile.getFileUrl(botProperties.token());

//        String filePath = tgFile.getFilePath();
//        java.io.File downloadedFile = fileDownloader.downloadFile(filePath);

        URL url = new URI(urlToDownloadFile).toURL();

        var fileTemp = java.io.File.createTempFile("telegram", ".ogg");

        try (InputStream inputStream = url.openStream();
             FileOutputStream fileOutputStream = new FileOutputStream(fileTemp)
        ) {
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (Exception e) {
            log.error("Error while downloading file: {}", fileId, e);
            throw new RuntimeException("Error while downloading file: %s".formatted(fileId), e);
        }

        return fileTemp;
    }


}
