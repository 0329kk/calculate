package com.jp.calculate.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jp.calculate.SalesDataInterface;
import com.jp.calculate.model.SalesRecord;

@Service
public class SalesManager implements SalesDataInterface  {
    
    private List<SalesRecord> salesRecords = new ArrayList<>();

    // プロパティファイルからデータベース接続情報を取得
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    // 売上データをリストに追加
    @Override
    public void addSalesRecord(SalesRecord record) {
        salesRecords.add(record);
    }

    // 売上データを表示
    @Override
    public void displaySalesRecords() {
        for (SalesRecord record : salesRecords) {
            System.out.println(record);
        }
    }

    // 総売上を計算
    @Override
    public double calculateTotalSales() {
        double total = 0;
        for (SalesRecord record : salesRecords) {
            total += record.getPrice() * record.getQuantity();
        }
        return total;
    }

    // 日別売上を計算
    @Override
    public Map<LocalDate, Double> calculateDailySales() {
        Map<LocalDate, Double> dailySales = new HashMap<>();
        for (SalesRecord record : salesRecords) {
            dailySales.put(record.getDate(), dailySales.getOrDefault(record.getDate(), 0.0) + record.getPrice() * record.getQuantity());
        }
        return dailySales;
    }

    // 商品別売上を計算
    @Override
    public Map<String, Double> calculateProductSales() {
        Map<String, Double> productSales = new HashMap<>();
        for (SalesRecord record : salesRecords) {
            productSales.put(record.getProductName(), productSales.getOrDefault(record.getProductName(), 0.0) + record.getPrice() * record.getQuantity());
        }
        return productSales;
    }

    // テキストファイルから売上データをロード
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

    // テキストファイルに売上データを保存
    public void saveSalesRecordsToFile(String filename) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (SalesRecord record : salesRecords) {
                bw.write(record.getProductName() + "," + record.getQuantity() + "," + record.getPrice() + "," + record.getDate());
                bw.newLine();
            }
        }
    }

    // SQLファイルからクエリを読み込んで実行
    public void executeSQLFromFile(String sqlFilePath, SalesRecord record) {
	System.out.println("Database URL: " + url);
        try (Connection con = DriverManager.getConnection(url, user, password);
             BufferedReader br = new BufferedReader(new FileReader(sqlFilePath))) {

            StringBuilder sqlQuery = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("--") && !line.trim().isEmpty()) {
                    sqlQuery.append(line);
                }
            }

            System.out.println("Executing SQL: " + sqlQuery.toString());
            try (PreparedStatement pst = con.prepareStatement(sqlQuery.toString())) {
                pst.setString(1, record.getProductName());
                pst.setInt(2, record.getQuantity());
                pst.setDouble(3, record.getPrice());
                pst.setDate(4, java.sql.Date.valueOf(record.getDate()));
                pst.executeUpdate();
                System.out.println("SQLファイルを実行し、データベースにデータが追加されました。");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<SalesRecord> getSalesRecords() {
        return salesRecords;
    }
    
//    private List<SalesRecord> salesRecords = new ArrayList<>();
//
//    @Override
//    public void addSalesRecord(SalesRecord record) {
//        salesRecords.add(record);
//    }
//
//    @Override
//    public void displaySalesRecords() {
//        for (SalesRecord record : salesRecords) {
//            System.out.println(record);
//        }
//    }
//
//    @Override
//    public double calculateTotalSales() {
//        double total = 0;
//        for (SalesRecord record : salesRecords) {
//            total += record.getPrice() * record.getQuantity();
//        }
//        return total;
//    }
//
//    @Override
//    public Map<LocalDate, Double> calculateDailySales() {
//        Map<LocalDate, Double> dailySales = new HashMap<>();
//        for (SalesRecord record : salesRecords) {
//            dailySales.put(record.getDate(), dailySales.getOrDefault(record.getDate(), 0.0) + record.getPrice() * record.getQuantity());
//        }
//        return dailySales;
//    }
//
//    @Override
//    public Map<String, Double> calculateProductSales() {
//        Map<String, Double> productSales = new HashMap<>();
//        for (SalesRecord record : salesRecords) {
//            productSales.put(record.getProductName(), productSales.getOrDefault(record.getProductName(), 0.0) + record.getPrice() * record.getQuantity());
//        }
//        return productSales;
//    }
//
//    public void loadSalesRecordsFromFile(String filename) throws IOException {
//        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] data = line.split(",");
//                String productName = data[0];
//                int quantity = Integer.parseInt(data[1]);
//                double price = Double.parseDouble(data[2]);
//                LocalDate date = LocalDate.parse(data[3]);
//                SalesRecord record = new SalesRecord(productName, quantity, price, date);
//                addSalesRecord(record);
//            }
//        }
//    }
//
//    public void saveSalesRecordsToFile(String filename) throws IOException {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
//            for (SalesRecord record : salesRecords) {
//                bw.write(record.getProductName() + "," + record.getQuantity() + "," + record.getPrice() + "," + record.getDate());
//                bw.newLine();
//            }
//        }
//    }
//    
//    public List<SalesRecord> getSalesRecords() {
//	    return salesRecords;
//	}

}
