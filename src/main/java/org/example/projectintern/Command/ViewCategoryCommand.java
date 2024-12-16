package org.example.projectintern.Command;

import lombok.RequiredArgsConstructor;
import org.example.projectintern.service.CategoryService;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * Command to view all categories in a tree format from the database.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ViewCategoryCommand implements Command {

    private final CategoryService categoryService;

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
