package org.example.projectintern.Command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.projectintern.generator.ExcelGenerator;
import org.example.projectintern.model.Category;
import org.example.projectintern.service.CategoryService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DownloadCategoryCommand implements Command{
    private final CategoryService categoryService;


    /**
     * Executes the command to download all categories as an Excel file and send it to the user.
     *
     * @param command the full text of the command (currently unused)
     * @param chatId the user's chat ID, used to send the generated file
     * @return a {@link SendDocument} object containing the Excel file to be sent to the user
     * @throws IOException if an error occurs while generating the Excel file
     */
    @Override
    public Object execute(String command, Long chatId) throws IOException {
        List<Category> categories =categoryService.findByParentIsNullAndChatId(chatId);
        byte[] excelFile = ExcelGenerator.generateExcelFile(categories);

        // Sending a file to the user
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setDocument(new InputFile(new ByteArrayInputStream(excelFile), "categories.xlsx"));
        // Sending a file
        return sendDocument;
    }
}
