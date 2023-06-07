package com.example.BankingApplication.employee;

import com.example.BankingApplication.Dto.EmployeeDto;
import com.example.BankingApplication.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private  EmployeeService employeeService;
    @PostMapping("/create-employee")
    public ResponseEntity<Object> createEmployee(@RequestBody EmployeeDto employeeDto){
        var employeePaySlip = employeeService.createEmployee(employeeDto);
        if(Boolean.TRUE.equals(employeePaySlip.get("isSuccess"))){
            return ResponseEntity.ok(employeePaySlip.get("message"));
        }else
            return ResponseEntity.badRequest().body(employeePaySlip.get("message"));
    }

    @GetMapping("/create-pdf/{id}")
    public ResponseEntity<InputStreamResource> createPdfTransaction(@PathVariable int id){
        ByteArrayInputStream pdf=employeeService.createPdfForTransaction(id);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateFormat=dateFormat.format(new Date());
        String headerValue="attachment;filename=pdf_"+currentDateFormat+".pdf";

        HttpHeaders httpHeaders =new HttpHeaders();
        httpHeaders.add("Content-Disposition",headerValue);
        return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

}
