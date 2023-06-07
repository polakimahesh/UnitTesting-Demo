package com.example.BankingApplication.Dto;

import lombok.Data;


import javax.validation.constraints.NotNull;

@Data
public class TransactionRequestDto {
    @NotNull(message = "From account number is required")
    private Long fromAccountNo;
    @NotNull(message = "To account number is required")
    private Long toAccountNo;
    private double amount;
    private String transactionDescription;
    private Boolean isActive;

}
