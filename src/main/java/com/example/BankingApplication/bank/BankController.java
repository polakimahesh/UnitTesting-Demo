package com.example.BankingApplication.bank;

import com.example.BankingApplication.Dto.BankDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bank")
public class BankController {
    @Autowired
    private  BankService bankService;
    @PostMapping("/register-bank")
    public ResponseEntity<Object> createBank(@RequestBody @Valid BankDto bankDto){
        BankDto bank = bankService.createBank(bankDto);
        if(bank!=null){
            return  new ResponseEntity<>(bank, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Bank not created",HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("")
    public ResponseEntity<List<Bank>> getAllBanks(){
        return new ResponseEntity<>(bankService.getAllBanks(),HttpStatus.OK);
    }
    @GetMapping("/get-single-bank/{id}")
    public ResponseEntity<Object> getSingleBank(@PathVariable int id){
        return new ResponseEntity<>(bankService.getSingleBank(id),HttpStatus.OK);
    }

}
