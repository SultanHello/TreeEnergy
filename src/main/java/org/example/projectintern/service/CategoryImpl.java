package org.example.projectintern.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.projectintern.config.TelegramBotConfig;
import org.example.projectintern.model.Category;
import org.example.projectintern.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for adding, removing, and retrieving categories from the database.
 */
@Service

@Transactional(readOnly = true)

public class CategoryImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    /**
     * Constructor for CategoryImpl to initialize the repository.
     */
    public CategoryImpl(CategoryRepository categoryRepository){
        this.categoryRepository =categoryRepository;

    }
    private static final Logger log = LoggerFactory.getLogger(TelegramBotConfig.class);



    /**
     * Adding a root category.
     *
     * @param name  category name
     * @param chatId chat identifier
     * @return result message
     */
    @Override
    @Transactional
    public String addRoot(String name, Long chatId) {
        log.info("Attempting to add root category with name: {} for chatId: {}", name, chatId);

        boolean check = categoryRepository.findByNameAndChatId(name, chatId).isPresent();
        if (check) {
            log.warn("Category with name {} already exists for chatId: {}", name, chatId);
            return "A category with the name " + name + " already exists. Please provide another name for the root category.";
        }

        Category category = createCategory(name, chatId);
        categoryRepository.save(category);
        log.info("Root category with name: {} successfully added for chatId: {}", name, chatId);
        return "Successfully added root category with name: " + name;
    }

    /**
     * Creates a new category.
     *
     * @param name  category name
     * @param chatId chat identifier
     * @return new category
     */
    public Category createCategory(String name, Long chatId) {
        return Category.builder()
                .name(name)
                .chatId(chatId)
                .build();
    }

    /**
     * Adding a child category.
     *
     * @param name  parent category name
     * @param child child category name
     * @param chatId chat identifier
     * @return result message
     */
    @Override
    @Transactional
    public String addChild(String name, String child, Long chatId) {
        log.info("Attempting to add child category with name: {} under parent category: {} for chatId: {}", child, name, chatId);

        if (name.equals(child)) {
            log.warn("User tried to add a category as its own child. Invalid input: {}", name);
            return "Please enter a valid category name.";
        }

        Optional<Category> parentOpt = categoryRepository.findByNameAndChatId(name, chatId);
        if (parentOpt.isEmpty()) {
            log.warn("Parent category with name {} does not exist for chatId: {}", name, chatId);
            return "Category with name " + name + " does not exist. Please specify an existing parent category.";
        }
        Category parentCategory = parentOpt.get();
        Category childCategory = findOrCreateChildCategory(child, chatId);

        // Checking if the child category is already assigned to the same parent category
        if (childCategory.getParent() != null && childCategory.getParent().getId().equals(parentCategory.getId())) {
            log.warn("The category {} is already a child of the parent category {} for chatId: {}", child, name, chatId);
            return "Please enter a valid category.";
        }

        // Setting the parent-child relationship and saving the changes
        childCategory.setParent(parentCategory);
        parentCategory.getChildren().add(childCategory);
        categoryRepository.save(childCategory);
        log.info("Successfully added child category: {} to parent category: {} for chatId: {}", child, name, chatId);
        return "Successfully added child: %s to parent: %s".formatted(child, name);
    }

    /**
     * Finds or creates a child category.
     *
     * @param child child category name
     * @param chatId chat identifier
     * @return found or created child category
     */
    private Category findOrCreateChildCategory(String child, Long chatId) {
        return categoryRepository.findByNameAndChatId(child, chatId)
                .orElseGet(() -> createCategory(child, chatId)); // Uses Factory Pattern
    }

    /**
     * Removing a category.
     *
     * @param name  category name
     * @param chatId chat identifier
     * @return result message
     */
    @Override
    @Transactional
    public String remove(String name, Long chatId) {
        log.info("Attempting to remove category with name: {} for chatId: {}", name, chatId);

        Optional<Category> categoryOpt = categoryRepository.findByNameAndChatId(name, chatId);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();

            // Removing the category from the parent's list of children if the category has a parent
            if (category.getParent() != null) {
                category.getParent().getChildren().remove(category);
                categoryRepository.save(category.getParent());
            }

            categoryRepository.delete(category);
            log.info("Successfully removed category with name: {} for chatId: {}", name, chatId);
            return "Successfully removed category with name: " + name;
        }
        log.warn("Category with name {} does not exist for chatId: {}", name, chatId);
        return "Category with name " + name + " does not exist. Please enter an existing category.";
    }

    /**
     * Checking if the category exists.
     *
     * @param name  category name
     * @param chatId chat identifier
     * @return true if the category exists, otherwise false
     */
    @Override
    public boolean categoryExists(String name, Long chatId) {
        return categoryRepository.findByNameAndChatId(name, chatId).isPresent();
    }

    /**
     * Retrieving the list of root categories for the given chat.
     *
     * @param chatId chat identifier
     * @return list of root categories
     */
    @Override
    public List<Category> findByParentIsNullAndChatId(Long chatId) {
        return categoryRepository.findByParentIsNullAndChatId(chatId);
    }



    /**
     * Retrieves a list of categories associated with a specific chat ID.
     *
     * @param chatId the ID of the chat to search for categories
     * @return a list of categories belonging to the given chat ID
     */
    @Override
    public List<Category> findByChatId(Long chatId) {
        return categoryRepository.findByChatId(chatId);
    }




    /**
     * Viewing the category tree for the given chat.
     *
     * @param chatId chat identifier
     * @return category tree as a string
     */
    @Override
    public String viewCategoryTree(Long chatId) {
        log.info("Attempting to view category tree for chatId: {}", chatId);

        List<Category> list = findByParentIsNullAndChatId(chatId);
        if (list.isEmpty()) {
            log.info("No categories found for chatId: {}", chatId);
            return "There are no categories";
        }
        StringBuilder tree = new StringBuilder();
        for (Category category : list) {
            buildTree(category, tree, 0);
        }
        log.info("Category tree generated successfully for chatId: {}", chatId);
        return tree.toString();
    }




    /**
     * Recursively builds a tree-like string representation of categories with indentation to represent hierarchical levels.
     *
     * @param category the category to be processed and added to the tree
     * @param builder a StringBuilder object used to accumulate the string representation of the tree
     * @param level the current level of indentation, indicating the depth of the category in the hierarchy
     */

    private void buildTree(Category category, StringBuilder builder, int level) {
        // Using StringBuilder to create a string with indentations

        StringBuilder indentation = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indentation.append("    "); // repeat "    " for every level
        }

        builder.append(indentation).append("- ").append(category.getName()).append("\n");

        // Iterating over child categories, if they exist
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            for (Category child : category.getChildren()) {
                buildTree(child, builder, level + 1); // Recursive call to build subcategories
            }
        }

    }


}
