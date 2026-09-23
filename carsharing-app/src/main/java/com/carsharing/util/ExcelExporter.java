package com.carsharing.util;

import com.carsharing.model.Rental;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

public class ExcelExporter implements DataExporter {
    @Override
    public void export(List<Rental> rentals, String filePath) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Rentals");
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "User ID", "Car ID", "Start Time", "End Time", "Total Cost", "Status"};
            for (int i = 0; i < columns.length; i++) {
                headerRow.createCell(i).setCellValue(columns[i]);
            }
            int rowNum = 1;
            for (Rental rental : rentals) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rental.getId());
                row.createCell(1).setCellValue(rental.getUserId());
                row.createCell(2).setCellValue(rental.getCarId());
                row.createCell(3).setCellValue(rental.getStartTime().toString());
                row.createCell(4).setCellValue(rental.getEndTime() != null ? rental.getEndTime().toString() : "");
                row.createCell(5).setCellValue(rental.getTotalCost() != null ? rental.getTotalCost().doubleValue() : 0.0);
                row.createCell(6).setCellValue(rental.getStatus().name());
            }
            for (int i = 0; i < columns.length; i++) sheet.autoSizeColumn(i);
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }
}
