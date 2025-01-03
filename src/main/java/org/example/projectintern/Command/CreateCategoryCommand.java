package org.example.projectintern.Command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.projectintern.config.TelegramBotConfig;
import org.example.projectintern.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Command to add a category, either as a root or as a child of an existing parent.
 */
@Component


public class CreateCategoryCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(TelegramBotConfig.class);


    private final CategoryService categoryService;
    // Constructor for creating the command to add a category
    public CreateCategoryCommand(CategoryService categoryService){
        this.categoryService = categoryService; // Store the reference to the category service
    }


    /**
     * Executes the add category command.
     * Validates the command format and adds a category accordingly.
     *
     * @param command full command text
     * @param chatId user chat ID
     * @return response message indicating success or failure
     */
    @Override
    public String execute(String command, Long chatId) {
        log.info("Executing add category command for chat ID: {}", chatId);

        String[] args = command.split(" ");

        // Check if the command format is valid
        if (args.length < 2) {
            log.error("Invalid command format for chat ID: {}. Command: {}", chatId, command);
            return "Invalid format. Use /addElement <parent> <child> or /addElement <root>.";
        }

        String categoryName = args[1].trim();

        if (args.length == 2) {
            // Root category
            return addRootCategory(categoryName, chatId);
        } else {
            // Child category
            return addChildCategory(args, chatId);
        }
    }

    /**
     * Adds a root category.
     *
     * @param categoryName name of the root category
     * @param chatId the chat identifier
     * @return response message
     */
    private String addRootCategory(String categoryName, Long chatId) {
        log.info("Adding root category '{}' for chat ID: {}", categoryName, chatId);
        return categoryService.addRoot(categoryName, chatId);
    }

    /**
     * Adds a child category under an existing parent.
     *
     * @param args command arguments
     * @param chatId the chat identifier
     * @return response message
     */
    private String addChildCategory(String[] args, Long chatId) {
        // Extract the parent category
        String parentCategory = extractParentCategory(args, chatId);
        if (parentCategory == null) {
            log.warn("Parent category does not exist for chat ID: {}", chatId);
            return "Parent category does not exist.";
        }

        // Extract the child category
        String childCategory = extractChildCategory(args);
        log.info("Adding child category '{}' under parent '{}' for chat ID: {}", childCategory, parentCategory, chatId);
        return categoryService.addChild(parentCategory, childCategory, chatId);
    }

    /**
     * Extracts the parent category from the command arguments.
     *
     * @param args command arguments
     * @param chatId the chat identifier
     * @return the parent category if it exists, otherwise null
     */
    private String extractParentCategory(String[] args, Long chatId) {
        StringBuilder parentBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            parentBuilder.append(args[i]).append(" ");
            String potentialParent = parentBuilder.toString().trim();
            if (categoryService.categoryExists(potentialParent, chatId)) {
                log.debug("Found parent category: {}", potentialParent);
                return potentialParent;
            }
        }
        log.warn("Parent category not found in the arguments for chat ID: {}", chatId);
        return null;
    }

    /**
     * Extracts the child category from the command arguments.
     *
     * @param args command arguments
     * @return the child category as a string
     */
    private String extractChildCategory(String[] args) {
        StringBuilder childBuilder = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            childBuilder.append(args[i]).append(" ");
        }
        return childBuilder.toString().trim();
    }
}

