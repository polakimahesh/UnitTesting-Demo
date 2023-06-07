package com.example.BankingApplication.enums;

public enum TransactionType {
    CREDIT("Credit"),
    DEBIT("Debit");

    private String transactionType;

    public String getTransactionType() {
        return transactionType;
    }

    TransactionType(String transactionType){
        this.transactionType=transactionType;
    }
}
