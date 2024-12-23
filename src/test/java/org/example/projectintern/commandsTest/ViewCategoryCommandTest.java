package org.example.projectintern.commandsTest;

import org.example.projectintern.Command.ViewCategoryCommand;
import org.example.projectintern.service.CategoryService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ViewCategoryCommandTest {
    /**
     * Test for execute.
     */
    @Test
    void testExecute() {
        // Arrange
        Long chatId = 123L;
        String expectedTree = "Root\n - Child1\n - Child2";

        CategoryService mockCategoryService = mock(CategoryService.class);
        when(mockCategoryService.viewCategoryTree(chatId)).thenReturn(expectedTree);

        ViewCategoryCommand command = new ViewCategoryCommand(mockCategoryService);

        // Act
        String result = command.execute("/view", chatId);

        // Assert
        assertEquals(expectedTree, result);
        verify(mockCategoryService, times(1)).viewCategoryTree(chatId);
        verifyNoMoreInteractions(mockCategoryService);
    }
}
