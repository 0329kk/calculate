package com.jp.calculate;

import java.time.LocalDate;

public class SalesRecord {
    
    private String productName;
    private int quantity;
    private double price;
    private LocalDate date;

    public SalesRecord(String productName, int quantity, double price, LocalDate date) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SalesRecord{" +
                "productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", date=" + date +
                '}';
    }

}
