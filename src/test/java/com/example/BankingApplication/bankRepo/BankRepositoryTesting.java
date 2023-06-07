package com.example.BankingApplication.bankRepo;

import com.example.BankingApplication.bank.Bank;
import com.example.BankingApplication.bank.BankRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;


@DataJpaTest
public class BankRepositoryTesting {
   @Autowired
    private BankRepository bankRepository;

   private  Bank bank1;
   private  Bank bank2;

    @BeforeEach
    void init() {
        bank1 = new Bank();
        bank1.setName("Union");
        bank1.setBranch("Hyderabad");
        bank1.setIfscCode("U1234");


        bank2 =new Bank();
        bank2.setName("Sbi");
        bank2.setBranch("Chennai");
        bank2.setIfscCode("S1234");


    }
    @Test
    void createBankTest(){
        Bank newBank= bankRepository.save(bank1);
        assertThat(newBank.getId()).isNotEqualTo(null);

    }

    @Test
    void findAllBanksTest() {

        bankRepository.save(bank1);
        bankRepository.save(bank2);
        // When
        List<Bank> banks = bankRepository.findAll();
        System.out.println(banks);
        // Then
        assertNotNull(banks);
        assertThat(banks).isNotNull();
        assertEquals(2, banks.size());
    }

    @Test
    void findOneBankTest() {
        bankRepository.save(bank1);
        //when
        Optional<Bank> bank=bankRepository.findById(bank1.getId());
        // Then
        assertTrue(bank.isPresent());
    }
    @Test
    void updateBankTest(){
        bankRepository.save(bank2);
        Bank bank = bankRepository.findById(bank2.getId()).get();
        bank.setName("Hdfc");
        Bank updateStatus=bankRepository.save(bank);
        assertEquals("Hdfc", updateStatus.getName());
    }
    @Test
    void deleteBankTest(){
        bankRepository.save(bank2);
        bankRepository.deleteById(bank2.getId());
        assertThat(bankRepository.existsById(bank2.getId())).isFalse();

    }
}
