package com.example.BankingApplication.Dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RecipientRequestDto {
    @NotNull(message = "User id is mandatory")
    private  Integer userId;
    @NotNull(message = "AccountNo must be required")
    private Long accountNo;
    @NotBlank(message = "Name must be required")
    private String name;
}
