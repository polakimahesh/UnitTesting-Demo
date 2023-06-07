package com.example.BankingApplication.Dto;

import lombok.Data;

@Data
public class AccountDto {
    private  int userId;
    private int bankId;
    private  double balance;
    private String accountType;
    private  Boolean isActive;
}
