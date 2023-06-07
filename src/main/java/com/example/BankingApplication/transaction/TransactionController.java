package com.example.BankingApplication.transaction;

import com.example.BankingApplication.Dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("")
    public ResponseEntity<Object> generateTransactions(@RequestBody TransactionRequestDto transactionRequestDto){
        var transaction=transactionService.createTransaction(transactionRequestDto);
        if(Boolean.TRUE.equals(transaction.get("isSuccess"))){
            return ResponseEntity.ok(transaction.get("message"));
        }else
            return ResponseEntity.badRequest().body(transaction.get("message"));
    }
    @GetMapping("getAll")
    public ResponseEntity<List<Transaction>> getAllTransaction(){
        return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
    }
    @PostMapping("get-transaction-by-accountNo")
    public ResponseEntity<Object> getAllTransactionsByAccountNo(@RequestBody TransactionAccountDetails transactionAccountDetails){
        var transaction=transactionService.getAllTransactionsByAccountNo(transactionAccountDetails);
        if(Boolean.TRUE.equals(transaction.get("isSuccess"))){
            return ResponseEntity.ok(transaction.get("message"));
        }else
            return ResponseEntity.badRequest().body(transaction.get("message"));
    }
    @PostMapping("/get")
    public ResponseEntity<Object> getAllTransactionByTypeAndDate(@RequestBody TransactionFilterDto transactionFilterDto){
        var transaction=transactionService.getAllTransactionByTypeAndDate(transactionFilterDto);
        return new ResponseEntity<>(transaction,HttpStatus.OK);
    }
    @GetMapping("/get-bank-statement-pdf")
    public ResponseEntity<InputStreamResource> createPdfTransaction(@RequestBody TransactionPdf transactionPdf){
        ByteArrayInputStream pdf=transactionService.createPdfForTransaction(transactionPdf);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateFormat=dateFormat.format(new Date());
        String headerValue="attachment;filename=pdf_"+currentDateFormat+".pdf";

        HttpHeaders httpHeaders =new HttpHeaders();
        httpHeaders.add("Content-Disposition",headerValue);
        return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

    @PostMapping("/create-transaction")
    public ResponseEntity<Object> createTransactions(@RequestBody RecipientTransactionDto recipientTransactionDto){
        var transaction=transactionService.createRecipientTransaction(recipientTransactionDto);
        if(Boolean.TRUE.equals(transaction.get("isSuccess"))){
            return ResponseEntity.ok(transaction.get("message"));
        }else
            return ResponseEntity.badRequest().body(transaction.get("message"));
    }
}
