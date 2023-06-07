package com.example.BankingApplication.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class UsersDto {
    @NotBlank(message = "first name is required")
    private String firstName;
    @NotBlank(message = "last name is required")
    private String lastName;
    @Pattern(regexp = "^\\d{10}$", message = "Please Enter 10 digit mobile number")
    private String mobileNo;
    @Email(message = "Email id is required")
    private String email;
    @NotNull(message = "Parameter Date Adjusted can not be blank or null")
    private LocalDate dateOfBirth;
    private String password;
}
