package org.example.projectintern.serviceTest;

import org.example.projectintern.model.Category;
import org.example.projectintern.repository.CategoryRepository;
import org.example.projectintern.service.CategoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test for adding a root category.
     */
    @Test
    void addRoot_Success() {
        // Given
        String name = "Root";
        Long chatId = 123L;
        when(categoryRepository.findByNameAndChatId(name, chatId)).thenReturn(Optional.empty());

        // When
        String result = categoryService.addRoot(name, chatId);

        // Then
        assertEquals("Successfully added root category with name: Root", result);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    /**
     * Test for adding a duplicate root category.
     */
    @Test
    void addRoot_Duplicate() {
        // Given
        String name = "Root";
        Long chatId = 123L;
        when(categoryRepository.findByNameAndChatId(name, chatId)).thenReturn(Optional.of(new Category()));

        // When
        String result = categoryService.addRoot(name, chatId);

        // Then
        assertEquals("A category with the name Root already exists. Please provide another name for the root category.", result);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    /**
     * Test for adding a child category.
     */
    @Test
    void addChild_Success() {
        // Given
        String parentName = "Root";
        String childName = "Child";
        Long chatId = 123L;
        Category parentCategory = new Category();
        parentCategory.setId(1L);
        when(categoryRepository.findByNameAndChatId(parentName, chatId)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findByNameAndChatId(childName, chatId)).thenReturn(Optional.empty());

        // When
        String result = categoryService.addChild(parentName, childName, chatId);

        // Then
        assertEquals("Successfully added child: Child to parent: Root", result);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    /**
     * Test for adding a child category as its own parent.
     */
    @Test
    void addChild_SelfParent() {
        // Given
        String parentName = "Root";
        String childName = "Root";
        Long chatId = 123L;

        // When
        String result = categoryService.addChild(parentName, childName, chatId);

        // Then
        assertEquals("Please enter a valid category name.", result);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    /**
     * Test for removing a category.
     */
    @Test
    void remove_Success() {
        // Given
        String name = "Root";
        Long chatId = 123L;
        Category category = new Category();
        when(categoryRepository.findByNameAndChatId(name, chatId)).thenReturn(Optional.of(category));

        // When
        String result = categoryService.remove(name, chatId);

        // Then
        assertEquals("Successfully removed category with name: Root", result);
        verify(categoryRepository, times(1)).delete(category);
    }

    /**
     * Test for removing a non-existent category.
     */
    @Test
    void remove_NotFound() {
        // Given
        String name = "NonExistent";
        Long chatId = 123L;
        when(categoryRepository.findByNameAndChatId(name, chatId)).thenReturn(Optional.empty());

        // When
        String result = categoryService.remove(name, chatId);

        // Then
        assertEquals("Category with name NonExistent does not exist. Please enter an existing category.", result);
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    /**
     * Test for viewing an empty category tree.
     */
    @Test
    void viewCategoryTree_Empty() {
        // Given
        Long chatId = 123L;
        when(categoryRepository.findByParentIsNullAndChatId(chatId)).thenReturn(List.of());

        // When
        String result = categoryService.viewCategoryTree(chatId);

        // Then
        assertEquals("There are no categories", result);
    }

    /**
     * Test for viewing a category tree with nested categories.
     */
    @Test
    void viewCategoryTree_WithNestedCategories() {
        // Given
        Long chatId = 123L;
        Category root = new Category();
        root.setName("Root");

        Category child = new Category();
        child.setName("Child");
        child.setParent(root);
        root.setChildren(List.of(child));

        when(categoryRepository.findByParentIsNullAndChatId(chatId)).thenReturn(List.of(root));

        // When
        String result = categoryService.viewCategoryTree(chatId);

        // Then
        assertTrue(result.contains("- Root"));
        assertTrue(result.contains("    - Child"));
    }
}
