package com.example.BankingApplication.transaction;

import com.example.BankingApplication.account.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne
    private Account fromAccountNo;
    @ManyToOne
    private Account toAccountNo;
    private String transactionType;
    private Double amount;
    private String transactionDescription;
    private LocalDateTime transactionTime;

}
