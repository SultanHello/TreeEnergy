package org.example.projectintern.commandsTest;

import org.example.projectintern.Command.DeleteCategoryCommand;
import org.example.projectintern.service.CategoryService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DeleteCategoryCommandTest {
    /**
     * Test for execute valid.
     */

    @Test
    void testExecuteValidCommand() {
        // Create mocks and an instance of the command
        CategoryService mockCategoryService = mock(CategoryService.class);
        DeleteCategoryCommand command = new DeleteCategoryCommand(mockCategoryService);

        // Input data
        String inputCommand = "/remove ParentCategory";
        Long chatId = 123L;

        // Mock the behavior of CategoryService
        when(mockCategoryService.remove("ParentCategory", chatId)).thenReturn("Category removed successfully!");

        // Execute the command
        String result = command.execute(inputCommand, chatId);

        // Verify the result
        assertEquals("Category removed successfully!", result);
        verify(mockCategoryService).remove("ParentCategory", chatId);
    }
    /**
     * Test for execute invalid command.
     */
    @Test
    void testExecuteInvalidCommandFormat() {
        // Create mocks and an instance of the command
        CategoryService mockCategoryService = mock(CategoryService.class);
        DeleteCategoryCommand command = new DeleteCategoryCommand(mockCategoryService);

        // Input data (incorrect command format)
        String inputCommand = "/remove";
        Long chatId = 123L;

        // Execute the command
        String result = command.execute(inputCommand, chatId);

        // Verify the result
        assertEquals("Invalid command format. Use /remove <element>.", result);
        verifyNoInteractions(mockCategoryService); // Ensure that the remove method was not called
    }
}

