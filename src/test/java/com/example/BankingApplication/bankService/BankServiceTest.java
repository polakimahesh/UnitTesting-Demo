package com.example.BankingApplication.bankService;

import com.example.BankingApplication.Dto.BankDto;
import com.example.BankingApplication.bank.Bank;
import com.example.BankingApplication.bank.BankRepository;
import com.example.BankingApplication.bank.BankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
@SuppressWarnings(value = "unchecked")
public class BankServiceTest {
    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private BankService bankService;

    @Test
    public void testCreateBank() {
        // create a mock BankDto object
        BankDto bankDto = new BankDto();
        bankDto.setId(1);
        bankDto.setName("HDFC");
        bankDto.setBranch("Hyderabad");
        bankDto.setIfscCode("H1234");

        // create a mock Bank object
        Bank bank = new Bank();
        bank.setId(bankDto.getId());
        bank.setName(bankDto.getName());
        bank.setBranch(bankDto.getBranch());
        bank.setIfscCode(bankDto.getIfscCode());


        // mock the behavior of BankRepository save
        Mockito.when(bankRepository.save(Mockito.any(Bank.class))).thenReturn(bank);

        // call the createBank() method
        BankDto result = bankService.createBank(bankDto);

        // verify the results

        assertEquals(bank.getName(), result.getName());
        assertEquals(bank.getBranch(), result.getBranch());
        assertEquals(bank.getIfscCode(), result.getIfscCode());

        // verify that BankRepository save
        Mockito.verify(bankRepository, Mockito.times(1)).save(Mockito.any(Bank.class));
    }

    @Test
    public void testGetAllBanks() {
        List<Bank> banks = new ArrayList<>();
        Bank bank1 = new Bank();
        bank1.setId(1);
        bank1.setName("HDFC");
        bank1.setIfscCode("H1234");
        bank1.setBranch("Hyderabad");
        Bank bank2 = new Bank();
        bank2.setId(2);
        bank2.setName("SBI");
        bank2.setIfscCode("S1234");
        bank2.setBranch("Chennai");
        banks.add(bank1);
        banks.add(bank2);

        when(bankRepository.findAll()).thenReturn(banks);

        List<Bank> allBanks = bankService.getAllBanks();
        assertEquals(2, allBanks.size());
        assertEquals(bank1.getId(), allBanks.get(0).getId());
        assertEquals(bank1.getBranch(), allBanks.get(0).getBranch());
        assertEquals(bank1.getName(), allBanks.get(0).getName());
        assertEquals(bank1.getIfscCode(), allBanks.get(0).getIfscCode());
        assertEquals(bank2.getId(), allBanks.get(1).getId());
        assertEquals(bank2.getBranch(), allBanks.get(1).getBranch());
        assertEquals(bank2.getName(), allBanks.get(1).getName());
        assertEquals(bank2.getIfscCode(), allBanks.get(1).getIfscCode());

        verify(bankRepository, times(1)).findAll();
    }
    @Test
//    @SuppressWarnings(value = "unchecked")
    public void testGetSingleBank() {
        // create a mock Bank object
        Bank bank = new Bank();
        bank.setId(1);
        bank.setBranch("Hyderabad");
        bank.setName("HDFC");
        bank.setIfscCode("H1234");

        // mock the behavior of BankRepository findById
        Mockito.when(bankRepository.findById(1)).thenReturn(Optional.of(bank));

        // call the getSingleBank() method
        HashMap<String, Object> result = bankService.getSingleBank(1);

        // verify the results
        assertTrue((Boolean) result.get("isSuccess"));
        Optional<Bank> message = (Optional<Bank>) result.get("message");
        assertTrue(message.isPresent());
        assertEquals(bank.getId(), message.get().getId());
        assertEquals(bank.getBranch(), message.get().getBranch());
        assertEquals(bank.getName(), message.get().getName());
        assertEquals(bank.getIfscCode(), message.get().getIfscCode());

        // verify that BankRepository findById()
        Mockito.verify(bankRepository, Mockito.times(1)).findById(1);
    }

}