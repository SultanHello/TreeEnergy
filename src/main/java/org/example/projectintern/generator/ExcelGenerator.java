package org.example.projectintern.generator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.projectintern.model.Category;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelGenerator {

    public static byte[] generateExcelFile(List<Category> categories) throws IOException {
        Workbook workbook = new XSSFWorkbook(); // create new excel file
        Sheet sheet = workbook.createSheet("Categories"); // create list

        // create header
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Category");
        headerRow.createCell(1).setCellValue("Parent");

        // add info from categories
        int rowIndex = 1;
        for (Category category : categories) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(category.getName());
            row.createCell(1).setCellValue(category.getParent() != null ? category.getParent().getName() : "None");
        }

        // write to ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();

        return byteArrayOutputStream.toByteArray(); // return array of byte
    }
}
