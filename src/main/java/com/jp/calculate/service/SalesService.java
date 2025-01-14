package com.jp.calculate.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jp.calculate.model.SalesRecord;
import com.jp.calculate.repository.SalesRecordRepository;

@Service
public class SalesService {
    private final SalesRecordRepository salesRecordRepository;

    @Autowired
    public SalesService(SalesRecordRepository salesRecordRepository) {
        this.salesRecordRepository = salesRecordRepository;
    }

    public List<SalesRecord> getAllSalesRecords() {
        return salesRecordRepository.findAll();
    }

    public SalesRecord saveSalesRecord(SalesRecord record) {
	// TODO 自動生成されたメソッド・スタブ
	return salesRecordRepository.save(record);
    }
    
    public Map<String, Double> calculateProductSales() {
	    List<SalesRecord> salesRecords = salesRecordRepository.findAll();
	    Map<String, Double> productSales = new HashMap<>();

	    for (SalesRecord record : salesRecords) {
	        productSales.put(record.getProductName(), 
	            productSales.getOrDefault(record.getProductName(), 0.0) + record.getPrice() * record.getQuantity());
	    }

	    return productSales;
	}
}
