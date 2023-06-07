package com.example.BankingApplication.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    List<Transaction> findByFromAccountNo_AccountNo(Long accountNo);

    List<Transaction> findByTransactionTypeAndTransactionTimeBetween(String transactionType, LocalDateTime transactionTimeStart, LocalDateTime transactionTimeEnd);








}
