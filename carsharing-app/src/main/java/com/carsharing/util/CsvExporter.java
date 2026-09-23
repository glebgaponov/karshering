package com.carsharing.util;

import com.carsharing.model.Rental;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class CsvExporter implements DataExporter {
    @Override
    public void export(List<Rental> rentals, String filePath) throws Exception {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("ID,UserID,CarID,StartTime,EndTime,TotalCost,Status");
            for (Rental r : rentals) {
                writer.printf("%d,%d,%d,%s,%s,%s,%s\n",
                        r.getId(), r.getUserId(), r.getCarId(),
                        r.getStartTime(),
                        r.getEndTime() != null ? r.getEndTime() : "",
                        r.getTotalCost() != null ? r.getTotalCost() : "",
                        r.getStatus());
            }
        }
    }
}
