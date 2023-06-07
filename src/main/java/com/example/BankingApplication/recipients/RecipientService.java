package com.example.BankingApplication.recipients;

import com.example.BankingApplication.Dto.RecipientDelete;
import com.example.BankingApplication.Dto.RecipientRequestDto;
import com.example.BankingApplication.Dto.UpdateRecipient;

import com.example.BankingApplication.account.Account;
import com.example.BankingApplication.account.AccountRepository;

import com.example.BankingApplication.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class RecipientService {
    @Autowired
    private RecipientRepository recipientRepository;
    @Autowired
    private  AccountRepository accountRepository;


    public List<Recipient> getAllRecipient(){
      return   recipientRepository.findAllByOrderByIdAsc();
    }

    public HashMap<String,Object> createRecipient(RecipientRequestDto recipientRequestDto){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();

        Account account =accountRepository.findByAccountNo(recipientRequestDto.getAccountNo());
        Recipient recipientAcc=recipientRepository.findByAccount_AccountNoAndUserId(recipientRequestDto.getAccountNo(),recipientRequestDto.getUserId());
        if(account!=null ){
            if(recipientAcc==null) {
                Recipient recipient = new Recipient();
                recipient.setUserId(recipientRequestDto.getUserId());
                recipient.setRecipientName(recipientRequestDto.getName());
                recipient.setAccount(account);
                recipient.setIsActive(true);
                recipientRepository.save(recipient);
                response.put("isSuccess", true);
                response.put("message", recipient);
            }else{
                response1.put("message", "Recipient already exist with account no " + recipientRequestDto.getAccountNo());
                response.put("isSuccess", false);
                response.put("message", response1);
            }
            return response;
        }
        response1.put("message", "inValid User details " + recipientRequestDto.getAccountNo());
        response.put("isSuccess", false);
        response.put("message", response1);
        return response;
    }

    public HashMap<String,Object> updateRecipient(UpdateRecipient updateRecipient){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();

        Recipient recipient = recipientRepository.findById(updateRecipient.getRecipientId()).orElse(null);
        if(recipient==null  ){
            response1.put("message","inValid User details");
            response.put("isSuccess",false);
            response.put("message",response1);
        }else{
            recipient.setRecipientName(updateRecipient.getRecipientName());
            recipient.setIsActive(updateRecipient.getIsActive());
            recipientRepository.save(recipient);
            response1.put("message","Update Recipient id "+updateRecipient.getRecipientId()+", Successfully!");
            response.put("isSuccess",true);
            response.put("message","updated User Successfully!");
        }
        return response;
    }

    public  HashMap<String,Object> deleteRecipient(RecipientDelete recipientDelete){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();
        var recipient = recipientRepository.findById(recipientDelete.getRecipientId()).orElse(null);
        if(recipient==null){
            response1.put("message","incorrect Recipient id "+recipientDelete.getRecipientId()+", please enter the valid id!");
            response.put("isSuccess",false);
        }else {
            recipientRepository.deleteById(recipientDelete.getRecipientId());
            response1.put("message","User deleted successfully! "+recipient.getId());
            response.put("isSuccess",true);
        }
        response.put("message",response1);
        return  response;
    }
}
