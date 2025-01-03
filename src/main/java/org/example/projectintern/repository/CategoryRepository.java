package org.example.projectintern.repository;

import org.example.projectintern.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
/**
 * Repository interface for managing category data in the database.
 * Extends JpaRepository to provide standard CRUD operations for the Category entity.
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {

    List<Category> findByChatId(Long aLong);


    /**
     * Finds a category by its name and associated chat ID.
     *
     * @param name the name of the category.
     * @param chatId the ID of the chat to which the category belongs.
     * @return an Optional containing the category, if found.
     */

    Optional<Category> findByNameAndChatId(String name, Long chatId);
    /**
     * Finds all root categories (categories without a parent) for a specific chat ID.
     *
     * @param chatId the ID of the chat to which the categories belong.
     * @return a list of root categories for the given chat ID.
     */
    List<Category> findByParentIsNullAndChatId(Long chatId);
}
