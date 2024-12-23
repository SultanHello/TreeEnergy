package org.example.projectintern.commandsTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.projectintern.Command.CreateCategoryCommand;
import org.example.projectintern.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryCommandTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CreateCategoryCommand createCategoryCommand;

    private Long chatId;

    @BeforeEach
    void setUp() {
        chatId = 123L;  // Example chat ID
    }
    /**
     * Test for execute add success category.
     */

    @Test
    void testExecuteAddRootCategorySuccess() {
        // Given
        String command = "/addElement RootCategory";
        when(categoryService.addRoot(anyString(), eq(chatId))).thenReturn("Root category added successfully!");

        // When
        String result = createCategoryCommand.execute(command, chatId);

        // Then
        verify(categoryService).addRoot("RootCategory", chatId);
        assertEquals("Root category added successfully!", result);
    }
    /**
     * Test for execute add child success category.
     */


    @Test
    void testExecuteAddChildCategorySuccess() {
        // Given
        String command = "/addElement ParentCategory ChildCategory";
        when(categoryService.categoryExists("ParentCategory", chatId)).thenReturn(true);
        when(categoryService.addChild(anyString(), anyString(), eq(chatId))).thenReturn("Child category added successfully!");

        // When
        String result = createCategoryCommand.execute(command, chatId);

        // Then
        verify(categoryService).categoryExists("ParentCategory", chatId);
        verify(categoryService).addChild("ParentCategory", "ChildCategory", chatId);
        assertEquals("Child category added successfully!", result);
    }
    /**
     * Test for execute add child parent not found.
     */

    @Test
    void testExecuteAddChildCategoryParentNotFound() {
        // Given
        String command = "/addElement NonExistentParent ChildCategory";
        when(categoryService.categoryExists("NonExistentParent", chatId)).thenReturn(false);

        // When
        String result = createCategoryCommand.execute(command, chatId);

        // Then
        verify(categoryService).categoryExists("NonExistentParent", chatId);
        assertEquals("Parent category does not exist.", result);
    }
    /**
     * Test for execute invalid command.
     */
    @Test
    void testExecuteInvalidCommandFormat() {
        // Given
        String command = "/addElement";

        // When
        String result = createCategoryCommand.execute(command, chatId);

        // Then
        assertEquals("Invalid format. Use /addElement <parent> <child> or /addElement <root>.", result);
    }





}
