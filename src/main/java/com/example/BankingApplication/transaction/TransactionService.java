package com.example.BankingApplication.transaction;

import com.example.BankingApplication.Dto.*;
import com.example.BankingApplication.account.Account;
import com.example.BankingApplication.account.AccountRepository;
import com.example.BankingApplication.enums.TransactionType;
import com.example.BankingApplication.recipients.Recipient;
import com.example.BankingApplication.recipients.RecipientRepository;
import com.example.BankingApplication.users.Users;
import com.example.BankingApplication.users.UsersRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RecipientRepository recipientRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Transactional
    public HashMap<String,Object> createTransaction(TransactionRequestDto transactionRequestDto){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();

        Account fromAccountNo= accountRepository.findByAccountNo(transactionRequestDto.getFromAccountNo());
        Account toAccountNo= accountRepository.findByAccountNo(transactionRequestDto.getToAccountNo());



        double fromAccountBalance= fromAccountNo.getBalance();
        double toAccountNoBalance=toAccountNo.getBalance();
        if(fromAccountBalance>=transactionRequestDto.getAmount() && fromAccountNo.getIsActive() && toAccountNo.getIsActive() && fromAccountNo!=toAccountNo) {

            //Debit transaction
            Transaction fromTransaction = new Transaction();
            fromTransaction.setFromAccountNo(fromAccountNo);
            fromTransaction.setToAccountNo(toAccountNo);
            fromTransaction.setTransactionTime(LocalDateTime.now());
            fromTransaction.setAmount(transactionRequestDto.getAmount());
            fromTransaction.setTransactionType(TransactionType.DEBIT.getTransactionType());
            fromTransaction.setTransactionDescription(transactionRequestDto.getTransactionDescription());
            fromAccountNo.setBalance(fromAccountBalance - transactionRequestDto.getAmount());
            transactionRepository.save(fromTransaction);
            accountRepository.save(fromAccountNo);


            //Credit Transaction
            Transaction toTransaction = new Transaction();

            toTransaction.setFromAccountNo(fromAccountNo);
            toTransaction.setToAccountNo(toAccountNo);
            toTransaction.setTransactionTime(LocalDateTime.now());
            toTransaction.setAmount(transactionRequestDto.getAmount());
            toTransaction.setTransactionType(TransactionType.CREDIT.getTransactionType());
            toTransaction.setTransactionDescription(transactionRequestDto.getTransactionDescription());
            transactionRepository.save(toTransaction);
            toAccountNo.setBalance(toAccountNoBalance + transactionRequestDto.getAmount());
            accountRepository.save(toAccountNo);

            response.put("isSuccess",true);
            response.put("message", "transaction done successfully!");
        }else {
            response1.put("message","incorrect Account details, please check it once,\n Thank you!");
            response.put("isSuccess",false);
            response.put("message",response1);
        }
        return response;
    }

    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    public HashMap<String,Object> getAllTransactionsByAccountNo(TransactionAccountDetails accountDetails){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();
        List<Transaction> transactions=  transactionRepository.findByFromAccountNo_AccountNo(accountDetails.getAccountNo());
        if(transactions.isEmpty()){
            response1.put("message","incorrect User Account no "+ accountDetails.getAccountNo());
            response.put("isSuccess",false);
            response.put("message",response1);
        }else {
            List<ListOfTransaction> listOfTransactions = new ArrayList<>();
            for (Transaction transaction : transactions) {
                ListOfTransaction listOfTransaction = new ListOfTransaction();
                listOfTransaction.setFromAccountNo(transaction.getFromAccountNo().getAccountNo());
                listOfTransaction.setToAccountNo(transaction.getToAccountNo().getAccountNo());
                listOfTransaction.setAmount(transaction.getAmount());
                listOfTransaction.setTransactionTime(transaction.getTransactionTime());
                listOfTransactions.add(listOfTransaction);
            }
            accountDetails.setListOfTransactions(listOfTransactions);
            response.put("isSuccess",true);
            response.put("message",accountDetails);

        }
        return response;
    }
    @Transactional
    public HashMap<String,Object> getAllTransactionByTypeAndDate(TransactionFilterDto transactionFilterDto){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();
        LocalDate localDate=transactionFilterDto.getTransactionTime();
        LocalDateTime startOfDay=localDate.atTime(LocalTime.MIDNIGHT);
        LocalDateTime endDay = localDate.atTime(LocalTime.MAX);


        List<Transaction> transactions =transactionRepository.findByTransactionTypeAndTransactionTimeBetween(transactionFilterDto.
                getTransactionType(), startOfDay,endDay);
        if(!transactions.isEmpty()) {

            List<ListOfTransactionFilterDto> listOfTransactionFilterDtos = new ArrayList<>();
            for (Transaction transaction : transactions) {
                ListOfTransactionFilterDto listOfTransactionFilterDto = new ListOfTransactionFilterDto();
                listOfTransactionFilterDto.setFromAccountNo(transaction.getFromAccountNo().getAccountNo());
                listOfTransactionFilterDto.setToAccountNo(transaction.getToAccountNo().getAccountNo());
                listOfTransactionFilterDto.setAmount(transaction.getAmount());
                listOfTransactionFilterDtos.add(listOfTransactionFilterDto);
            }
            transactionFilterDto.setListOfTransactionFilterDtos(listOfTransactionFilterDtos);
            response.put("message", transactionFilterDto);
        }else{
            response1.put("message","Enter correct details ");
            response.put("isSuccess",false);
            response.put("message",response1);
        }
        return response;
    }



    @Transactional()
    public HashMap<String,Object> createRecipientTransaction(RecipientTransactionDto recipientTransactionDto){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();


        Account fromAccountNo= accountRepository.findByAccountNo(recipientTransactionDto.getFromAccountNo());
        Recipient recipient = recipientRepository.findById(recipientTransactionDto.getRecipientId()).orElse(null);


        if(fromAccountNo!=null && recipient!=null) {
            double fromAccountBalance = fromAccountNo.getBalance();
            double toAccountNoBalance=recipient.getAccount().getBalance();
            Recipient recipientCheck= recipientRepository.findByAccount_AccountNoAndUserId(recipient.getAccount().getAccountNo(), recipient.getUserId());

            if (fromAccountBalance >= recipientTransactionDto.getAmount() && fromAccountNo.getIsActive() && recipient.getIsActive() &&
                    fromAccountNo.getUsers().getId() == recipient.getUserId() &&  recipientCheck!=null && fromAccountNo.getAccountNo()!=recipient.getAccount().getAccountNo()) {

                //Debit transaction
                Transaction fromTransaction = new Transaction();
                fromTransaction.setFromAccountNo(fromAccountNo);
                fromTransaction.setToAccountNo(recipient.getAccount());
                fromTransaction.setTransactionTime(LocalDateTime.now());
                fromTransaction.setAmount(recipientTransactionDto.getAmount());
                fromTransaction.setTransactionType(TransactionType.DEBIT.getTransactionType());
                fromTransaction.setTransactionDescription(recipientTransactionDto.getTransactionDescription());
                transactionRepository.save(fromTransaction);
                fromAccountNo.setBalance(fromAccountBalance - recipientTransactionDto.getAmount());
                accountRepository.save(fromAccountNo);


                //Credit Transaction
                Transaction toTransaction = new Transaction();
                toTransaction.setFromAccountNo(fromAccountNo);
                toTransaction.setToAccountNo(recipient.getAccount());
                toTransaction.setTransactionTime(LocalDateTime.now());
                toTransaction.setAmount(recipientTransactionDto.getAmount());
                toTransaction.setTransactionType(TransactionType.CREDIT.getTransactionType());
                toTransaction.setTransactionDescription(recipientTransactionDto.getTransactionDescription());
                transactionRepository.save(toTransaction);
                Account toAccount = recipient.getAccount();
                toAccount.setBalance(toAccountNoBalance+recipientTransactionDto.getAmount());
                accountRepository.save(toAccount);

                response.put("isSuccess", true);
                response.put("message", "transaction done successfully!");
            } else {
                response1.put("message", "insufficient balance or Invalid details, please check it once, Thank you!");
                response.put("isSuccess", false);
                response.put("message", response1);
            }
            return response;
        }
        response1.put("message", "does not exist with  account no,"+recipientTransactionDto.getFromAccountNo()+" and recipient id "+recipientTransactionDto.getRecipientId()+" please enter valid account details");
        response.put("isSuccess", false);
        response.put("message", response1);
        return response;
    }


    public ByteArrayInputStream createPdfForTransaction(TransactionPdf transactionPdf){
        Account account =accountRepository.findByAccountNo(transactionPdf.getAccountNo());
       List<Transaction> transactions=transactionRepository.findByFromAccountNo_AccountNo(transactionPdf.getAccountNo());
        String title="Bank Statement";
        String content=account.getUsers().getFirstName()+" "+account.getUsers().getLastName()+" your bank statement!";

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //create document
        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document,out);

        //page footer
        HeaderFooter footer = new HeaderFooter(true, new Phrase(" page"));
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setBorderWidthBottom(0);
        document.setFooter(footer);

        //document open
        document.open();
        //adding Heading
        Font fontTitle= FontFactory.getFont(FontFactory.HELVETICA_BOLD,20, Color.BLACK);
        Paragraph heading = new Paragraph(title,fontTitle);
        heading.setAlignment(Element.ALIGN_CENTER);
        heading.setSpacingAfter(20);
        document.add(heading);
        //adding paragraph
        Font paraFont=FontFactory.getFont(FontFactory.HELVETICA,15);
        Paragraph paragraph = new Paragraph(content,paraFont);
        paragraph.setSpacingAfter(20);
        document.add(paragraph);

        //users details
        document.add(new Paragraph("Name : "+account.getUsers().getFirstName()));
        document.add(new Paragraph("Account No: "+account.getAccountNo()));
        document.add(new Paragraph("Bank Name: "+account.getBank().getName()));
        document.add(new Paragraph("Total Balance: "+account.getBalance()));
        document.add(new Paragraph("Account Type: "+account.getAccountType()));
        Paragraph spaces = new Paragraph();
        spaces.setSpacingAfter(20);
        document.add(spaces);

        //creating  table columns
        PdfPTable table = new PdfPTable(4);

        table.setWidthPercentage(100);
        table.setWidths(new int[]{10,10,10,10});

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        PdfPCell hcell;

        hcell = new PdfPCell(new Phrase("From Account No", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("To Account No", headFont));
        table.addCell(hcell);


        hcell = new PdfPCell(new Phrase("Amount", headFont));
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Transaction Date", headFont));
        table.addCell(hcell);


        for(Transaction transaction :transactions)
        {
            table.addCell(String.valueOf(transaction.getFromAccountNo().getAccountNo()));
            table.addCell(String.valueOf(transaction.getToAccountNo().getAccountNo()));
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(transaction.getTransactionTime().getDayOfMonth()+"-"+transaction.getTransactionTime().getMonthValue()+"-"+transaction.getTransactionTime().getYear());

        }
        document.add(table);
        //document close
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

}
