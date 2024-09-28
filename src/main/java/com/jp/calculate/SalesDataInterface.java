package com.jp.calculate;

import java.time.LocalDate;
import java.util.Map;

public interface SalesDataInterface {
    
    void displaySalesRecords();
    double calculateTotalSales();
    Map<LocalDate, Double> calculateDailySales();
    Map<String, Double> calculateProductSales();
    
}
