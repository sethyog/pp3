package com.springapp.entity;


import java.sql.Timestamp;

public class TransferForm {
    private String receiver;
    private double amount;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
