package com.example.BankingApplication.account;

import com.example.BankingApplication.bank.Bank;
import com.example.BankingApplication.recipients.Recipient;
import com.example.BankingApplication.users.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;
    private Long accountNo;
    @ManyToOne
    private Users users;
    @ManyToOne
    private Bank bank;
    private Double balance=0.0;
    private String accountType;
    private Boolean isActive;

}
