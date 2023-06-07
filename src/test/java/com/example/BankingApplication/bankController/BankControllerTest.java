package com.example.BankingApplication.bankController;
import com.example.BankingApplication.Dto.BankDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.BankingApplication.bank.Bank;
import com.example.BankingApplication.bank.BankService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.List;
@SpringBootTest
@AutoConfigureMockMvc
class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankService bankService;

    @Test
    void testCreateBank() throws Exception {
        Bank bank = new Bank();
        bank.setId(1);
        bank.setName("HDFC");
        bank.setBranch("Hyderabad");
        bank.setIfscCode("H12345");

        BankDto bankDto = new BankDto();
        bankDto.setId(bank.getId());
        bankDto.setName(bank.getName());
        bankDto.setBranch(bank.getBranch());
        bankDto.setIfscCode(bank.getIfscCode());

        Mockito.when(bankService.createBank(Mockito.any(BankDto.class))).thenReturn(bankDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/bank/register-bank")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bankDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("HDFC")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.branch", Matchers.is("Hyderabad")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ifscCode", Matchers.is("H12345")));

        Mockito.verify(bankService, Mockito.times(1)).createBank(Mockito.any(BankDto.class));
    }



    @Test
    void testGetAllBanks() throws Exception {
        List<Bank> banks = new ArrayList<>();
        Bank bank1 = new Bank();
        bank1.setId(1);
        bank1.setName("HDFC");
        bank1.setBranch("Hyderabad");
        bank1.setIfscCode("H1234");

        Bank bank2 = new Bank();
        bank2.setId(2);
        bank2.setName("SBI");
        bank2.setBranch("Chennai");
        bank2.setIfscCode("S5678");

        banks.add(bank1);
        banks.add(bank2);

        Mockito.when(bankService.getAllBanks()).thenReturn(banks);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bank"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("HDFC")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].branch", Matchers.is("Hyderabad")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ifscCode", Matchers.is("H1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("SBI")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].branch", Matchers.is("Chennai")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].ifscCode", Matchers.is("S5678")));

        Mockito.verify(bankService, Mockito.times(1)).getAllBanks();
    }



}