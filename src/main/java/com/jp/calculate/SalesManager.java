package com.jp.calculate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesManager implements SalesDataInterface  {
    
    private List<SalesRecord> salesRecords = new ArrayList<>();

    @Override
    public void addSalesRecord(SalesRecord record) {
        salesRecords.add(record);
    }

    @Override
    public void displaySalesRecords() {
        for (SalesRecord record : salesRecords) {
            System.out.println(record);
        }
    }

    @Override
    public double calculateTotalSales() {
        double total = 0;
        for (SalesRecord record : salesRecords) {
            total += record.getPrice() * record.getQuantity();
        }
        return total;
    }

    @Override
    public Map<LocalDate, Double> calculateDailySales() {
        Map<LocalDate, Double> dailySales = new HashMap<>();
        for (SalesRecord record : salesRecords) {
            dailySales.put(record.getDate(), dailySales.getOrDefault(record.getDate(), 0.0) + record.getPrice() * record.getQuantity());
        }
        return dailySales;
    }

    @Override
    public Map<String, Double> calculateProductSales() {
        Map<String, Double> productSales = new HashMap<>();
        for (SalesRecord record : salesRecords) {
            productSales.put(record.getProductName(), productSales.getOrDefault(record.getProductName(), 0.0) + record.getPrice() * record.getQuantity());
        }
        return productSales;
    }

    public void loadSalesRecordsFromFile(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String productName = data[0];
                int quantity = Integer.parseInt(data[1]);
                double price = Double.parseDouble(data[2]);
                LocalDate date = LocalDate.parse(data[3]);
                SalesRecord record = new SalesRecord(productName, quantity, price, date);
                addSalesRecord(record);
            }
        }
    }

    public void saveSalesRecordsToFile(String filename) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (SalesRecord record : salesRecords) {
                bw.write(record.getProductName() + "," + record.getQuantity() + "," + record.getPrice() + "," + record.getDate());
                bw.newLine();
            }
        }
    }
}
