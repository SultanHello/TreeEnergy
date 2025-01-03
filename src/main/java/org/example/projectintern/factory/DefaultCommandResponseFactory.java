package org.example.projectintern.factory;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// Factory Method Pattern is used here
@Component
public class DefaultCommandResponseFactory implements CommandResponseFactory {
    // Predefined responses for commands
    private static final String ALLRULES = """
            Commands available in the bot:
            
            1) /viewTree - 📜 View the current category tree.
            
            2) /addElement <element name> - ➕ Add a new root element.
            
            3) /addElement <parent> <child element> - ➕ Add a child element to an existing parent.
            
            4) /removeElement <element name> - 🗑️ Remove a category and all its descendants.
            
            5) /download - 📥 Download the category tree as a table.
            
            6) /help - ℹ️ List of available commands.""";

    private static final String START = """
            Welcome to Category Bot! 👋
            Here you can easily manage the category tree.
            Type "/help" to learn about the bot's capabilities.
            """;

    private static final String UNKNOWN =
            "❌ Command not recognized. Type /help to see the list of available commands.";


    private final Map<String, String> predefinedResponses = new HashMap<>();
    /**
     * Initializes the factory with predefined responses for certain commands.
     */
    public DefaultCommandResponseFactory() {
        predefinedResponses.put("/start", START);
        predefinedResponses.put("/help", ALLRULES);

    }

    /**
     * Creates a response based on the user's message text.
     *
     * @param messageText the command or message text received from the user.
     * @return the generated response, or an unknown command message if the command is not predefined.
     */
    @Override
    public String createResponse(String messageText) {
        if (predefinedResponses.containsKey(messageText)) {
            return predefinedResponses.get(messageText);
        }

        return UNKNOWN;// Return a default unknown command message
    }
}
