package com.example.BankingApplication.recipients;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient,Integer> {
    Recipient findByAccount_AccountNo(Long accountNo);

    Recipient findByAccount_AccountNoAndAccount_Users_Id(Long accountNo, int id);

    Recipient findByAccount_AccountNoAndUserId(Long accountNo, Integer userId);


    List<Recipient> findAllByOrderByIdAsc();
}
