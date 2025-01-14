package com.jp.calculate.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.jp.calculate.model.SalesRecord;
import com.jp.calculate.service.SalesManager;
import com.jp.calculate.service.SalesService;

@Controller
public class SalesController {
    
    private final SalesService salesService;
    private final SalesManager salesManager;

    @Autowired
    public SalesController(SalesService salesService, SalesManager salesManager) {
        this.salesService = salesService;
        this.salesManager = salesManager;
    }

    @GetMapping("/sales/view")
    public String viewSalesRecords(Model model) {
	// 日付と単価をフォーマット
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.JAPAN);
        List<SalesRecord> salesRecords = salesService.getAllSalesRecords();
        for (SalesRecord record : salesRecords) {
            // 日付のフォーマット
            record.setFormattedDate(record.getDate().format(dateFormatter));
            // 単価のフォーマット
            record.setFormattedPrice(currencyFormatter.format(record.getPrice()));
        }
        // 商品ごとの売上を計算してグラフ用のデータを準備
        Map<String, Double> productSales = salesService.calculateProductSales();
        List<String> productNames = new ArrayList<>(productSales.keySet());
        List<Double> salesAmounts = new ArrayList<>(productSales.values());
        model.addAttribute("productNames", productNames);
        model.addAttribute("salesAmounts", salesAmounts);
        model.addAttribute("salesRecords", salesRecords);
        return "sales";
    }
    
    @PostMapping("/sales/add")
    public String addSalesRecord(@Valid @ModelAttribute SalesRecord record, BindingResult result, Model model) {
	if (result.hasErrors()) {
            model.addAttribute("salesRecords", salesService.getAllSalesRecords());
            return "sales";
        }
	salesService.saveSalesRecord(record);
	return "redirect:/sales/view";
    }
    
    @GetMapping("/sales/saveToFile")
    public String saveToFile() {
        try {
            Path projectRootPath = Paths.get("").toAbsolutePath().normalize();
            Path filePath = projectRootPath.resolve("src").resolve("sales_data.txt");
            salesManager.saveDatabaseRecordsToFile(filePath.toString());
            System.out.println("データベースのデータをテキストファイルに保存しました。");
        } catch (IOException e) {
            System.out.println("テキストファイルへの保存に失敗しました: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/sales/view";
    }

}
