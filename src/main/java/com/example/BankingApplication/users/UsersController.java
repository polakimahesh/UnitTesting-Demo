package com.example.BankingApplication.users;

import com.example.BankingApplication.Dto.UserDeleteDto;
import com.example.BankingApplication.Dto.UserUpdateDto;
import com.example.BankingApplication.Dto.UsersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    @Autowired
    private UsersService usersService;
    @GetMapping("")
    public ResponseEntity<List<Users>> getAllUsers(){
        return new ResponseEntity<>(usersService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/register-user")
    public ResponseEntity<Object> createUsers(@Valid @RequestBody UsersDto usersDto){
        var users= usersService.createUser(usersDto);
        if(users!=null) {
            return new ResponseEntity<>(users, HttpStatus.CREATED);
        }else {
            HashMap<String,String> response=new HashMap<>();
            response.put("message","user not create!");
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getSingleUser(@PathVariable int id){
        var user=usersService.getSingleUser(id);
        if(Boolean.TRUE.equals(user.get("isSuccess"))){
            return ResponseEntity.ok(user.get("message"));
        }else
            return ResponseEntity.badRequest().body(user.get("message"));
    }

    @PostMapping("/update-user")
    public  ResponseEntity<Object> updateUser(@RequestBody UserUpdateDto userUpdateDto){
        var user =usersService.updateUser(userUpdateDto);
        if(Boolean.TRUE.equals(user.get("isSuccess"))){
            return ResponseEntity.ok(user.get("message"));
        }else
            return ResponseEntity.badRequest().body(user.get("message"));
    }

    @PostMapping("/delete-user")
    public  ResponseEntity<Object> deleteUser(@RequestBody UserDeleteDto userDeleteDto){
        var user=usersService.deleteUser(userDeleteDto);
        if(Boolean.TRUE.equals(user.get("isSuccess"))){
            return ResponseEntity.ok(user.get("message"));
        }else
            return ResponseEntity.badRequest().body(user.get("message"));
    }
}
