package com.delaroystudios.scanner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaidModel {

    @SerializedName("customer_email")
    @Expose
    private String customer_email;
    @SerializedName("items")
    @Expose
    private String items;
    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("token")
    @Expose
    private String token;

    public PaidModel (String customer_email, String items, int amount, String token) {
        this.customer_email = customer_email;
        this.items = items;
        this.amount = amount;
        this.token = token;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
