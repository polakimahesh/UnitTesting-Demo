package com.example.BankingApplication.account;

import com.example.BankingApplication.Dto.AccountDto;
import com.example.BankingApplication.Dto.AccountResponseDto;
import com.example.BankingApplication.Dto.ListOfAccountDetails;
import com.example.BankingApplication.bank.Bank;
import com.example.BankingApplication.bank.BankRepository;
import com.example.BankingApplication.enums.AccountType;
import com.example.BankingApplication.users.Users;
import com.example.BankingApplication.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {
    @Autowired
    private  AccountRepository accountRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private UsersRepository usersRepository;

    private Long getAccountNumber(){
        Random random = new Random();
        String accountNo="";
        for(int i=0;i<12;i++){
            int randomNumber=random.nextInt(10);
            accountNo+=randomNumber;
        }
        return  Long.parseLong(accountNo);
    }

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }


    public HashMap<String,Object>  getAccountByAccountNo(AccountResponseDto accountResponseDto){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();
        Account account=accountRepository.findByAccountNo(accountResponseDto.getAccountNo());

        if(account==null){
            response1.put("message","inValid User account no "+ accountResponseDto.getAccountNo());
            response.put("isSuccess",false);
            response.put("message",response1);
        }else {
            ListOfAccountDetails listOfAccountDetails = new ListOfAccountDetails();
            listOfAccountDetails.setUsersLastName(account.getUsers().getLastName());
            listOfAccountDetails.setUsersFirstName(account.getUsers().getFirstName());
            listOfAccountDetails.setAccountType(account.getAccountType());
            listOfAccountDetails.setBalance(account.getBalance());
            listOfAccountDetails.setBankName(account.getBank().getName());
            listOfAccountDetails.setBankIfscCode(account.getBank().getIfscCode());
            accountResponseDto.setListOfAccountDetails(listOfAccountDetails);
            response.put("isSuccess",true);
            response.put("message",accountResponseDto);
        }
        return response;
    }



    public Account createAccount(AccountDto accountDto){
        Account account = new Account();
        Bank bank = bankRepository.findById(accountDto.getBankId()).orElse(null);
        Users users =usersRepository.findById(accountDto.getUserId()).orElse(null);


        account.setAccountNo(getAccountNumber());
        account.setBank(bank);
        account.setUsers(users);
        if(accountDto.getAccountType()!=null && "Current".equalsIgnoreCase(accountDto.getAccountType())){
            account.setAccountType(AccountType.CURRENT.getAccountType());
        }else if(accountDto.getAccountType()!=null && "Salary".equalsIgnoreCase(accountDto.getAccountType())){
            account.setAccountType(AccountType.SALARY.getAccountType());
        }
        else {
            account.setAccountType(AccountType.SAVINGS.getAccountType());
        }
        account.setBalance(accountDto.getBalance());
        account.setIsActive(accountDto.getIsActive());
        accountRepository.save(account);
       return account;
    }


}
