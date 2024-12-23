package org.example.projectintern.config;

import lombok.extern.slf4j.Slf4j;
import org.example.projectintern.Command.*;
import org.example.projectintern.bot.TelegramBot;
import org.example.projectintern.factory.DefaultCommandResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Configuration class for initializing and registering the Telegram bot.
 */
@Configuration

public class TelegramBotConfig {
    private static final Logger log = LoggerFactory.getLogger(TelegramBotConfig.class);


    /**
     * Bean for creating and registering a Telegram bot.
     *
     * This method initializes a bot instance and registers it with the Telegram API.
     *
     * @param botName bot name
     * @param token bot token
     * @param createCategoryCommand command to add a new category
     * @param viewCategoryCommand command to view categories
     * @param deleteCategoryCommand command to delete a category
     * @param defaultCommandResponseFactory factory for generating default command responses
     * @return a fully initialized TelegramBot instance
     */
    @Bean
    @ConditionalOnProperty(name = "telegram.bot.enabled", havingValue = "true", matchIfMissing = true)
    public TelegramBot telegramBot(@Value("${bot.name}") String botName,
                                   @Value("${bot.token}") String token,
                                   CreateCategoryCommand createCategoryCommand,
                                   ViewCategoryCommand viewCategoryCommand,
                                   DeleteCategoryCommand deleteCategoryCommand,
                                   DefaultCommandResponseFactory defaultCommandResponseFactory,
                                   DownloadCategoryCommand downloadCategoryCommand) {
        log.info("Initializing Telegram bot with name: {}", botName);

        // Create the Telegram bot with provided configuration and commands
        TelegramBot telegramBot = new TelegramBot(botName, token, createCategoryCommand, viewCategoryCommand,
                deleteCategoryCommand, defaultCommandResponseFactory,downloadCategoryCommand);

        try {
            // Register the bot with the Telegram API
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            log.info("Attempting to register the bot...");
            telegramBotsApi.registerBot(telegramBot);
            log.info("Bot registration success.");

        } catch (TelegramApiException e) {
            // Handle errors during bot registration
            log.error("Failed to register the Telegram bot due to an exception: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to register Telegram bot.", e);
        }

        // Return the initialized Telegram bot instance
        return telegramBot;
    }
}
