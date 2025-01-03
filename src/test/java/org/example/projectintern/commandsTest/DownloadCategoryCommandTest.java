package org.example.projectintern.commandsTest;

import org.example.projectintern.Command.DownloadCategoryCommand;
import org.example.projectintern.model.Category;
import org.example.projectintern.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DownloadCategoryCommandTest {
    /**
     * Test for execute.
     */
    @Test
    void testExecute() throws Exception {
        // Arrange
        Long chatId = 123L;
        String command = "/download";
        CategoryService mockCategoryService = mock(CategoryService.class);

        // Return an empty list of categories
        when(mockCategoryService.findByChatId(chatId))
                .thenReturn(Collections.emptyList());

        DownloadCategoryCommand commandUnderTest = new DownloadCategoryCommand(mockCategoryService);

        // Act
        Object result = commandUnderTest.execute(command, chatId);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof SendDocument);

        SendDocument sendDocument = (SendDocument) result;
        assertEquals(chatId.toString(), sendDocument.getChatId());
        assertNotNull(sendDocument.getDocument());
        assertEquals("categories.xlsx", sendDocument.getDocument().getMediaName());

        // Verify that CategoryService was called
        verify(mockCategoryService, times(1))
                .findByChatId(chatId);

        // Ensure no additional calls were made
        verifyNoMoreInteractions(mockCategoryService);
    }
}

