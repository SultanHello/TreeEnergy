package org.example.projectintern.Command;

import lombok.RequiredArgsConstructor;
import org.example.projectintern.config.TelegramBotConfig;
import org.example.projectintern.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * Command to view all categories in a tree format from the database.
 */
@Component

public class ViewCategoryCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(TelegramBotConfig.class);


    private final CategoryService categoryService;
    // Constructor for creating the command to view a category
    public ViewCategoryCommand(CategoryService categoryService){
        this.categoryService = categoryService; // Store the reference to the category service
    }


    /**
     * Executes the command to view all categories.
     *
     * @param command the full text of the command
     * @param chatId the user's chat ID
     * @return returns all data from the database in a tree format
     */
    @Override
    public String execute(String command, Long chatId) {
        log.info("Executing View Category Command for chat ID: {}", chatId);

        // Fetch the category tree from the CategoryService
        String categoryTree = categoryService.viewCategoryTree(chatId);
        log.debug("Category tree fetched for chat ID {}: {}", chatId, categoryTree);

        return categoryTree;
    }
}
