package com.carsharing.util;

import com.carsharing.model.Rental;
import java.util.List;

public interface DataExporter {
    void export(List<Rental> rentals, String filePath) throws Exception;
}
