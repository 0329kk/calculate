package com.jp.calculate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jp.calculate.service.SalesManager;

@Controller
@RequestMapping("/sales")
public class SalesController {
    private final SalesManager salesManager;

    public SalesController() {
        this.salesManager = new SalesManager();
        // 必要に応じて初期データのロードをここで実行することも可能
    }
    
    @GetMapping("/view")
    public String viewSalesRecords(Model model) {
        model.addAttribute("salesRecords", salesManager.getSalesRecords());
        return "sales";  // "sales.html"を返す
    }

//    // 売上データの追加 (POSTリクエスト)
//    @PostMapping("/add")
//    public String addSalesRecord(@RequestBody SalesRecord record) {
//        salesManager.addSalesRecord(record);
//        return "売上データが正常に追加されました！";
//    }
//
//    // 売上データの表示 (GETリクエスト)
//    @GetMapping("/all")
//    public List<SalesRecord> getAllSalesRecords() {
//        return salesManager.getSalesRecords();
//    }
//
//    // 売上合計の計算 (GETリクエスト)
//    @GetMapping("/total")
//    public double getTotalSales() {
//        return salesManager.calculateTotalSales();
//    }
//
//    // 日別売上の集計 (GETリクエスト)
//    @GetMapping("/daily")
//    public Map<LocalDate, Double> getDailySales() {
//        return salesManager.calculateDailySales();
//    }
//
//    // 商品別売上の集計 (GETリクエスト)
//    @GetMapping("/product")
//    public Map<String, Double> getProductSales() {
//        return salesManager.calculateProductSales();
//    }

}
