package com.example.BankingApplication.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ListOfTransactionFilterDto {
    private Long fromAccountNo;
    private Long toAccountNo;
    private double amount;

}
