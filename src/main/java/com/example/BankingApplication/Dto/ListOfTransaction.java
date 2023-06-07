package com.example.BankingApplication.Dto;

import com.example.BankingApplication.account.Account;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ListOfTransaction {
    private Long fromAccountNo;
    private Long toAccountNo;
    private double amount;
    private LocalDateTime transactionTime;
}
