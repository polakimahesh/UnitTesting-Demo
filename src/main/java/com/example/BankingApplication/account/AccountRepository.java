package com.example.BankingApplication.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
    Account findByAccountNo(long accountNo);

    Account findByAccountNoAndAccountType(long accountNo, String accountType);





}
