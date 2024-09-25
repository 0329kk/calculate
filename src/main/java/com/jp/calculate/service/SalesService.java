package com.jp.calculate.service;

import java.util.List;

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
}
