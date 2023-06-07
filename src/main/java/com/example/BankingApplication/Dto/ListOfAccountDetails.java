package com.example.BankingApplication.Dto;

import lombok.Data;

@Data
public class ListOfAccountDetails {
    private  String usersFirstName;
    private String usersLastName;
    private String bankName;
    private String bankIfscCode;
    private double balance;
    private String accountType;
}
