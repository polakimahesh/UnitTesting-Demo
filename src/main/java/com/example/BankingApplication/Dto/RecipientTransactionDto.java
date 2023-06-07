package com.example.BankingApplication.Dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RecipientTransactionDto {
    @NotNull(message = "From account number is required")
    private Long fromAccountNo;
    @NotNull(message = "recipient id  is required")
    private Integer recipientId;
    @NotNull(message = "Amount is required")
    private Double amount;
    private String transactionDescription;
    @NotNull(message ="Must be required boolean value")
    private Boolean isActive;
}
