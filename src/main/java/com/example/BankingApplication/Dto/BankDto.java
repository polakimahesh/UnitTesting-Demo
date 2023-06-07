package com.example.BankingApplication.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankDto {
    @NotNull
    private  Integer id;
    @NotBlank(message = "Bank name is required")
    private  String name;
    @NotBlank(message = "Ifsc code is required")
    private String ifscCode;
    @NotBlank(message = "branch is required")
    private String branch;
}
