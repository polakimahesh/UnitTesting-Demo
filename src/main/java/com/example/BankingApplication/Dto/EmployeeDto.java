package com.example.BankingApplication.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDto {
    private  long accountNo;
    private double payAmount;
    private double professionalTax;



}
