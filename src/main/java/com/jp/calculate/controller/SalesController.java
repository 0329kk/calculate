package com.jp.calculate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jp.calculate.model.SalesRecord;
import com.jp.calculate.service.SalesService;

@Controller
public class SalesController {
    
    private final SalesService salesService;

    @Autowired
    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping("/sales/view")
    public String viewSalesRecords(Model model) {
        List<SalesRecord> salesRecords = salesService.getAllSalesRecords();
        model.addAttribute("salesRecords", salesRecords);
        return "sales";
    }

}
