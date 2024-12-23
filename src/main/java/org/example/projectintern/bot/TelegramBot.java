package org.example.projectintern.bot;

import lombok.extern.slf4j.Slf4j;
import org.example.projectintern.Command.*;
import org.example.projectintern.config.TelegramBotConfig;
import org.example.projectintern.factory.CommandResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final String botName; // Name of the bot
    private final Map<String, Command> commands = new HashMap<>(); // List of available commands
    private final CommandResponseFactory commandResponseFactory; // Factory for creating responses
    private static final Logger log = LoggerFactory.getLogger(TelegramBotConfig.class);


    public TelegramBot(String botName, String token, CreateCategoryCommand addCategoryCommand, ViewCategoryCommand viewCategoryCommand, DeleteCategoryCommand deleteCategoryCommand, CommandResponseFactory commandResponseFactory, DownloadCategoryCommand downloadCategoryCommand) {
        super(token);
        this.botName = botName;
        commands.put("/addElement", addCategoryCommand); // Add a command to add a category
        commands.put("/viewTree", viewCategoryCommand);  // Add a command to view categories
        commands.put("/removeElement", deleteCategoryCommand);// Add a command to remove a category
        commands.put("/download", downloadCategoryCommand);// Add a command to download excel
        this.commandResponseFactory = commandResponseFactory; // Initialize the response factory
    }

    @Override
    public void onUpdateReceived(Update update){
        if (update.hasMessage()) {
            Message message = update.getMessage();
            long chatId = message.getChatId();
            if (message.hasText()) {
                String messageText = message.getText();
                String response = "";
                if (messageText.equals("/help") || messageText.equals("/start")) {
                    response = commandResponseFactory.createResponse(messageText);
                }else {
                    // Handle other commands
                    Object responseObject = handleCommand(messageText, chatId);
                    try {
                        if(messageText.equals("/download")){
                            execute((SendDocument) responseObject);
                        }else{
                            response = responseObject.toString();
                        }
                    }catch (TelegramApiException e){
                        throw new RuntimeException("error with telegramApi");
                    }
                }
                sendMessage(chatId, response);

            } else {
                sendMessage(chatId, "Invalid command. Use /help to see the list of commands."); // Response for wrong input
            }
        }
    }

    /**
     * Handles user commands based on the command list.
     *
     * @param messageText the command from the user
     * @param chatId      the chat ID where the command was sent
     * @return the response to the command
     */
    private Object handleCommand(String messageText, Long chatId) {
        for (String commandKey : commands.keySet()) {
            if (messageText.startsWith(commandKey)) { // Check if command exists
                try {
                    return commands.get(commandKey).execute(messageText, chatId); // Execute command
                } catch (Exception e) {
                    log.error("Error when running command: {}", commandKey, e);
                    return "Error while running the command. Please try again.";
                }
            }
        }
        return commandResponseFactory.createResponse(messageText); // Default response
    }

    /**
     * Sends a message to the user.
     *
     * @param chatId the chat ID to send the message
     * @param text   the message content
     */
    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId)); // Set the chat ID
        message.setText(text); // Set the message text
        if(!text.isEmpty()){
            try {
                execute(message); // Send the message
                log.info("Message sent to chat ID {}: {}", chatId, text); // Log success
            } catch (TelegramApiException e) {
                log.error("Error sending message to chat ID {}: {}", chatId, e.getMessage());
                sendMessage(chatId, "Could not send the message. Please try again later."); // Retry message
            }

        }

    }

    @Override
    public String getBotUsername() {
        return this.botName; // Return the bot's name
    }
}
