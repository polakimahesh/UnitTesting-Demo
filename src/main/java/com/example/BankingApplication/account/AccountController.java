package com.example.BankingApplication.account;

import com.example.BankingApplication.Dto.AccountDto;
import com.example.BankingApplication.Dto.AccountResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
//    @Autowired
//    private AccountRepository accountRepository;

    @GetMapping("")
    public  ResponseEntity<Object> getAllAccounts(){
        return new ResponseEntity<>(accountService.getAllAccounts(),HttpStatus.OK);
    }
    @PostMapping("/get-single-account")
    public ResponseEntity<Object> getSingleAccountByAccountNO(@RequestBody AccountResponseDto accountNoDto){
        var account = accountService.getAccountByAccountNo(accountNoDto);
        if(Boolean.TRUE.equals(account.get("isSuccess"))){
            return ResponseEntity.ok(account.get("message"));
        }else
            return ResponseEntity.badRequest().body(account.get("message"));
    }

    @PostMapping("/create-account")
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto accountDto){
        return  new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.OK);
    }

}
