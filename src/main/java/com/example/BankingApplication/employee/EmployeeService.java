package com.example.BankingApplication.employee;

import com.example.BankingApplication.Dto.EmployeeDto;
import com.example.BankingApplication.account.Account;
import com.example.BankingApplication.account.AccountRepository;
import com.example.BankingApplication.enums.AccountType;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.allegro.finance.tradukisto.MoneyConverters;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AccountRepository accountRepository;

    public HashMap<String,Object> createEmployee(EmployeeDto employeeDto){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();
        Account account = accountRepository.findByAccountNoAndAccountType(employeeDto.getAccountNo(), AccountType.SALARY.getAccountType());
        if(account==null){
            response1.put("message","incorrect account details please enter the valid details, \n Thank you!");
            response.put("isSuccess",false);
            response.put("message",response1);
        }else {
            EmployeePaySlip employee = new EmployeePaySlip();
            employee.setName(account.getUsers().getFirstName() + " " + account.getUsers().getLastName());
            employee.setAccountNo(account.getAccountNo());
            employee.setAccountType(account.getAccountType());
            employee.setPayPeriod(LocalDate.now());
            employee.setPayAmount(employeeDto.getPayAmount());
            employee.setProfessionalTax(employeeDto.getProfessionalTax());
            employee.setGrossDeduction(employee.getProfessionalTax());
            account.setBalance(employee.getPayAmount() - employee.getProfessionalTax());
            accountRepository.save(account);
            employeeRepository.save(employee);
            response1.put("message","Employee Created Successfully!");
            response.put("isSuccess",true);
            response.put("message",response1);
        }
        return response;
    }

    public ByteArrayInputStream createPdfForTransaction(int id) {
        EmployeePaySlip paySlip = employeeRepository.findById(id).orElse(null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //create a document
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,out);
        //page footer
        HeaderFooter footer = new HeaderFooter(true, new Phrase(" page"));
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setBorderWidthBottom(0);
        document.setFooter(footer);
        //document open
        document.open();

        //set image at top
        Image image;
        try {
            image = Image.getInstance("C:/Users/MaheshPolaki/Downloads/BankingApplication/BankingApplication/src/main/resources/image/finstacklogo.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        image.scaleAbsolute(150,50);
        image.setAlignment(Element.ALIGN_CENTER);
        document.add(image);
        Font sidefont= FontFactory.getFont(FontFactory.HELVETICA_BOLD,12, Color.lightGray);
        Paragraph sideHeading = new Paragraph("Finstack Infotech Private Limited,\n308 1 st floor,Ram Nagar coimbatore",sidefont);
        sideHeading.setAlignment(Element.ALIGN_RIGHT);
        document.add(sideHeading);

        //spaces between set image top and user details
        document.add(Chunk.NEWLINE);

        Font paraFont=FontFactory.getFont(FontFactory.HELVETICA_BOLD,15);
        Paragraph paragraph = new Paragraph("Payslip-"+LocalDate.now().getMonth()+" "+LocalDate.now().getYear(),paraFont);
        paragraph.setSpacingAfter(20);
        document.add(paragraph);
        //employee details heading
        Font para=FontFactory.getFont(FontFactory.HELVETICA_BOLD,12);
        Paragraph empMsg=new Paragraph("Employee Details",para);
        empMsg.setAlignment(Element.ALIGN_CENTER);
        document.add(empMsg);

        //user details
        document.add(new Paragraph("Employee Id: "+paySlip.getId()));
        document.add(new Paragraph("Name : "+paySlip.getName()));
        document.add(new Paragraph("Account No: "+paySlip.getAccountNo()));
        document.add(new Paragraph("Package: 4LPA"));
        document.add(new Paragraph("Date of Joining: 10/10/2022"));
        document.add(new Paragraph("Location: Hyderabad"));
        document.add(new Paragraph("Designation: SoftwareDeveloper"));
        document.add(new Paragraph("Account Type: "+paySlip.getAccountType()));

        //spaces between user details and new table
        document.add(Chunk.NEWLINE);

        //create table
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{20,10});

        //font
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        //salary details
        PdfPCell lcell;
        PdfPCell rcell;
        lcell = new PdfPCell(new Phrase("Income", headFont));
        lcell.setBackgroundColor(Color.lightGray);
        lcell.setBorder(Rectangle.NO_BORDER);
        lcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        lcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(lcell);

        lcell = new PdfPCell(new Phrase());
        lcell.setBackgroundColor(Color.lightGray);
        lcell.setBorder(Rectangle.NO_BORDER);
        lcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(lcell);

        lcell = new PdfPCell(new Phrase("Basic Salary", headFont));
        lcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(lcell);
        rcell=new PdfPCell(new Phrase(String.valueOf(paySlip.getPayAmount()/10*6)));
        rcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(rcell);

        lcell = new PdfPCell(new Phrase("HRA", headFont));
        lcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(lcell);
        rcell=new PdfPCell(new Phrase(String.valueOf(paySlip.getPayAmount()/10)));
        rcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(rcell);


        lcell = new PdfPCell(new Phrase("Internet Allowance", headFont));
        lcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(lcell);
        rcell=new PdfPCell(new Phrase(String.valueOf(paySlip.getPayAmount()/10)));
        rcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(rcell);

        lcell = new PdfPCell(new Phrase("Medical Allowance", headFont));
        lcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(lcell);
        rcell=new PdfPCell(new Phrase(String.valueOf(paySlip.getPayAmount()/10)));
        rcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(rcell);

        lcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        lcell = new PdfPCell(new Phrase("Special Allowance", headFont));
        lcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(lcell);
        rcell=new PdfPCell(new Phrase(String.valueOf(paySlip.getPayAmount()/10)));
        rcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(rcell);

        lcell = new PdfPCell(new Phrase("Professional Tax", headFont));
        lcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(lcell);
        rcell=new PdfPCell(new Phrase(String.valueOf(paySlip.getProfessionalTax())));
        rcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(rcell);


        lcell = new PdfPCell(new Phrase("GrossDeduction", headFont));
        lcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(lcell);
        rcell=new PdfPCell(new Phrase(String.valueOf(paySlip.getGrossDeduction())));
        rcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(rcell);


        lcell = new PdfPCell(new Phrase("Total Salary", headFont));
        lcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(lcell);
        rcell=new PdfPCell(new Phrase(String.valueOf(paySlip.getPayAmount()-paySlip.getGrossDeduction())));
        rcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(rcell);

        lcell=new PdfPCell(new Phrase());
        lcell.addElement(Chunk.NEWLINE);
        table.addCell(lcell);
        rcell=new PdfPCell(new Phrase());
        rcell.addElement(Chunk.NEWLINE);
        table.addCell(rcell);
        MoneyConverters converters = MoneyConverters.ENGLISH_BANKING_MONEY_VALUE;


        lcell = new PdfPCell(new Phrase("Net Salary In Words"));
        lcell.setBackgroundColor(Color.gray);
        lcell.setBorder(Rectangle.NO_BORDER);
        lcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(lcell);

        lcell = new PdfPCell(new Phrase(  String.valueOf(converters.asWords(BigDecimal.valueOf(paySlip.getPayAmount()-paySlip.getGrossDeduction()))).replace("£ 00/100"," "), headFont));
        lcell.setBackgroundColor(Color.gray);
        lcell.setBorder(Rectangle.NO_BORDER);
        lcell.setHorizontalAlignment(Element.ALIGN_RIGHT);


        table.addCell(lcell);


        document.add(table);
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("** This is a computer generated payslip and does not require signature and stamp."));
        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
}
