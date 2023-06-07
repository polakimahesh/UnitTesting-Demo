package com.example.BankingApplication.Dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountResponseDto {
    private  long accountNo;
    private ListOfAccountDetails listOfAccountDetails;


}
