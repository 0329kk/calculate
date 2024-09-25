package com.jp.calculate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jp.calculate.model.SalesRecord;

@Repository
public interface SalesRecordRepository extends JpaRepository<SalesRecord, Integer> {
    // 追加のクエリが必要な場合はここに記述
}
