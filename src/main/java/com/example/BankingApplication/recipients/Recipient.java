package com.example.BankingApplication.recipients;

import com.example.BankingApplication.account.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;
    private  Integer userId;
    @ManyToOne
    private Account account;

    private  String recipientName;
    private Boolean isActive;

}
