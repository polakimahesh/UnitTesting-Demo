package com.example.BankingApplication.security;

import lombok.Data;

@Data
public class JwtAuthRequest {
    private  String userName;
    private String password;
}
