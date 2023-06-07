package com.example.BankingApplication.Dto;

import lombok.Data;

@Data
public class UpdateRecipient {
    private int recipientId;
    private  String recipientName;
    private  Boolean isActive;
}
