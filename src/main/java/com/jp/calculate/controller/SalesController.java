package com.jp.calculate.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
        List<SalesRecord> salesRecords = salesService.getAllSalesRecords();
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
