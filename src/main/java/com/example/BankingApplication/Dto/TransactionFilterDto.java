package com.example.BankingApplication.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TransactionFilterDto {
    private String transactionType;

    private LocalDate transactionTime;
    private List<ListOfTransactionFilterDto> listOfTransactionFilterDtos;
}
