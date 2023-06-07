package com.example.BankingApplication.recipients;

import com.example.BankingApplication.Dto.RecipientDelete;
import com.example.BankingApplication.Dto.RecipientRequestDto;
import com.example.BankingApplication.Dto.UpdateRecipient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipient")
public class RecipientController {
    @Autowired
    private  RecipientService recipientService;
    @GetMapping("/get-all-recipients")
    public ResponseEntity<List<Recipient>> getAllRecipient(){
        return new ResponseEntity<>(recipientService.getAllRecipient(), HttpStatus.OK);
    }
    @PostMapping("/create-recipient")
    public ResponseEntity<Object> createRecipient(@RequestBody RecipientRequestDto recipientRequestDto){
        var account=recipientService.createRecipient(recipientRequestDto);
        if(Boolean.TRUE.equals(account.get("isSuccess"))){
            return ResponseEntity.ok(account.get("message"));
        }else
            return ResponseEntity.badRequest().body(account.get("message"));
    }
    @PostMapping("/update-recipient")
    public ResponseEntity<Object> updateRecipient(@RequestBody UpdateRecipient updateRecipient){
        var recipient = recipientService.updateRecipient(updateRecipient);
        if(Boolean.TRUE.equals(recipient.get("isSuccess"))){
            return ResponseEntity.ok(recipient.get("message"));
        }else
            return ResponseEntity.badRequest().body(recipient.get("message"));
    }
    @PostMapping("/delete-recipient")
    public ResponseEntity<Object> deleteRecipient(@RequestBody RecipientDelete recipientDelete){
        var recipient=recipientService.deleteRecipient(recipientDelete);
        if(Boolean.TRUE.equals(recipient.get("isSuccess"))){
            return ResponseEntity.ok(recipient.get("message"));
        }else
            return ResponseEntity.badRequest().body(recipient.get("message"));
    }
}
