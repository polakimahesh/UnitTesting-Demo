package com.example.BankingApplication.Dto;

import lombok.Data;

import java.util.List;

@Data
public class TransactionAccountDetails {
    private  long accountNo;
    private List<ListOfTransaction> listOfTransactions;
}
