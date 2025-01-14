package com.jp.calculate.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jp.calculate.SalesDataInterface;
import com.jp.calculate.model.SalesRecord;

@Service
public class SalesManager implements SalesDataInterface  {
    
    private final SalesService salesService;
    
    @Autowired
    public SalesManager(SalesService salesService) {
        this.salesService = salesService;
    }

    // プロパティファイルからデータベース接続情報を取得
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    // 売上データを表示
    // 日付と通貨のフォーマッタを設定
    public void displaySalesRecords() {
	List<SalesRecord> salesRecords = salesService.getAllSalesRecords();
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.JAPAN);

        // テーブルのヘッダーを表示
        System.out.printf("%-10s %-6s %-10s %-12s%n", "商品名", "数量", "単価", "日付");
        System.out.println("---------------------------------------------");

        // 各レコードを表示
        for (SalesRecord record : salesRecords) {
            System.out.printf("%-10s %-6d %-10s %-12s%n",
                record.getProductName(),
                record.getQuantity(),
                currencyFormatter.format(record.getPrice()),
                record.getDate().format(dateFormatter)
            );
        }
    }

    // 総売上を計算
    @Override
    public double calculateTotalSales() {
	List<SalesRecord> salesRecords = salesService.getAllSalesRecords();
        double total = 0;
        for (SalesRecord record : salesRecords) {
            total += record.getPrice() * record.getQuantity();
        }
        return total;
    }

    // 日別売上を計算
    @Override
    public Map<LocalDate, Double> calculateDailySales() {
	List<SalesRecord> salesRecords = salesService.getAllSalesRecords();
        Map<LocalDate, Double> dailySales = new HashMap<>();
        for (SalesRecord record : salesRecords) {
            dailySales.put(record.getDate(), dailySales.getOrDefault(record.getDate(), 0.0) + record.getPrice() * record.getQuantity());
        }
        return dailySales;
    }

    // 商品別売上を計算
    @Override
    public Map<String, Double> calculateProductSales() {
	List<SalesRecord> salesRecords = salesService.getAllSalesRecords();
        Map<String, Double> productSales = new HashMap<>();
        for (SalesRecord record : salesRecords) {
            productSales.put(record.getProductName(), productSales.getOrDefault(record.getProductName(), 0.0) + record.getPrice() * record.getQuantity());
        }
        return productSales;
    }

    // データベースのデータをテキストファイルに保存するメソッド
    public void saveDatabaseRecordsToFile(String filename) throws IOException {
        List<SalesRecord> salesRecords = salesService.getAllSalesRecords();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (SalesRecord record : salesRecords) {
                bw.write(record.getProductName() + "," + record.getQuantity() + "," + record.getPrice() + "," + record.getDate());
                bw.newLine();
            }
        }
    }
    
    //日別売り上げデータ整形 コンソール表示用
    public String formatDailySales(Map<LocalDate, Double> dailySales) {
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
	    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.JAPAN);
	    
	    StringBuilder formattedSales = new StringBuilder();
	    formattedSales.append(String.format("%-12s %-12s%n", "日付", "売上合計"));
	    formattedSales.append("-----------------------------------\n");

	    for (Map.Entry<LocalDate, Double> entry : dailySales.entrySet()) {
	        formattedSales.append(String.format("%-12s %-12s%n",
	            entry.getKey().format(dateFormatter), 
	            currencyFormatter.format(entry.getValue())
	        ));
	    }

	    formattedSales.append("-----------------------------------\n");
	    return formattedSales.toString();
	}

}
