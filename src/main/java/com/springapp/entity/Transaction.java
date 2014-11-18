package com.springapp.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Transaction {


    private int id;
    private double amount;
    private Timestamp txTime;
    private List<UserTransaction> userTransactions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getTxTime() {
        return txTime;
    }

    public Date getTxDate() {
        return new Date(txTime.getTime());
    }

    public void setTxTime(Timestamp txTime) {
        this.txTime = txTime;
    }

    public List<UserTransaction> getUserTransactions() {
        return userTransactions;
    }

    public void setUserTransactions(List<UserTransaction> userTransactions) {
        this.userTransactions = userTransactions;
    }
}
