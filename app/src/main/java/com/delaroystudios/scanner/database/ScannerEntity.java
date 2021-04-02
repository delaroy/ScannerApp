package com.delaroystudios.scanner.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class ScannerEntity {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @ColumnInfo(name = "title")
    private String title;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "sku")
    private String sku;

    @ColumnInfo(name = "price")
    private int price;

    @ColumnInfo(name = "quantity")
    private int quantity;

    public ScannerEntity(String title, String sku, int price, int quantity ) {
        this.title = title;
        this.sku = sku;
        this.price = price;
        this.quantity = quantity;
    }


}
