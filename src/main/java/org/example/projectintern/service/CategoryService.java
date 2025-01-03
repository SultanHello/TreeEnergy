package org.example.projectintern.service;


import org.example.projectintern.model.Category;

import java.util.List;

/**
 * Service interface for managing categories in the system.
 * Defines operations for adding, removing, checking existence, and viewing categories.
 */
public interface CategoryService {

    /**
     * Adds a new root category.
     *
     * @param name the name of the category.
     * @param chatId the chat ID for the category.
     * @return a response message.
     */
    String addRoot(String name, Long chatId);

    /**
     * Adds a child category to an existing parent category.
     *
     * @param name the name of the parent category.
     * @param child the name of the child category.
     * @param chatId the chat ID for the category.
     * @return a response message.
     */
    String addChild(String name, String child, Long chatId);

    /**
     * Removes a category and its descendants.
     *
     * @param name the name of the category to remove.
     * @param chatId the chat ID for the category.
     * @return a response message.
     */
    String remove(String name, Long chatId);

    /**
     * Checks if a category exists.
     *
     * @param name the name of the category.
     * @param chatId the chat ID for the category.
     * @return true if the category exists, false otherwise.
     */
    boolean categoryExists(String name, Long chatId);

    /**
     * Finds root categories for a specific chat ID.
     *
     * @param chatId the chat ID to find root categories for.
     * @return a list of root categories.
     */
    List<Category> findByParentIsNullAndChatId(Long chatId);


    /**
     * Finds all categories associated with the specified chat ID.
     *
     * @param chatId the ID of the chat whose categories are to be retrieved
     * @return a list of categories for the given chat ID
     */
    List<Category> findByChatId(Long chatId);


    /**
     * Views the category tree for a specific chat ID.
     *
     * @param chatId the chat ID to view the category tree for.
     * @return the category tree as a string.
     */
    String viewCategoryTree(Long chatId);
}
