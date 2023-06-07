package com.example.BankingApplication.users;

import com.example.BankingApplication.Dto.UserDeleteDto;
import com.example.BankingApplication.Dto.UserUpdateDto;
import com.example.BankingApplication.Dto.UsersDetailsDto;
import com.example.BankingApplication.Dto.UsersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Users> getAllUsers(){
        return usersRepository.findAll().stream().sorted(Comparator.comparingInt(Users::getId)).collect(Collectors.toList());
    }
    public HashMap<String,Object> createUser(UsersDto usersDto) {
        Users users = new Users();
        users.setFirstName(usersDto.getFirstName());
        users.setLastName(usersDto.getLastName());
        users.setEmail(usersDto.getEmail());
        users.setMobileNo(usersDto.getMobileNo());
        users.setDateOfBirth(usersDto.getDateOfBirth());
        String encodedPassword = passwordEncoder.encode(usersDto.getPassword());
        users.setPassword(encodedPassword);
        usersRepository.save(users);
        HashMap<String,Object> object= new HashMap<>();
        object.put("message","Users created Successfully!");
        return object;
    }

    public HashMap<String,Object> getSingleUser(int id){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();
        var users = usersRepository.findById(id).orElse(null);
        if(users==null){
            response1.put("message","incorrect User id "+id+", please enter the valid id!");
            response.put("isSuccess",false);
            response.put("message",response1);
        }else {
            UsersDetailsDto usersDetailsDto = new UsersDetailsDto();
            usersDetailsDto.setFirstName(users.getFirstName());
            usersDetailsDto.setLastName(users.getLastName());
            usersDetailsDto.setEmail(users.getEmail());
            usersDetailsDto.setMobileNo(users.getMobileNo());
            usersDetailsDto.setDateOfBirth(users.getDateOfBirth());
            response.put("isSuccess",true);
            response.put("message",usersDetailsDto);
        }

        return  response;
    }

    public HashMap<String,Object> updateUser(UserUpdateDto userUpdateDto){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();
        var users = usersRepository.findById(userUpdateDto.getUserId()).orElse(null);
        if(users==null){
            response1.put("message","incorrect User id "+userUpdateDto.getUserId()+", please enter the valid id!");
            response.put("isSuccess",false);
            response.put("message",response1);
        }else {
            users.setFirstName(userUpdateDto.getFirstName());
            users.setLastName(userUpdateDto.getLastName());
            users.setEmail(userUpdateDto.getEmail());
            users.setMobileNo(userUpdateDto.getMobileNo());
            users.setDateOfBirth(userUpdateDto.getDateOfBirth());
            String encodedPassword = passwordEncoder.encode(userUpdateDto.getPassword());
            users.setPassword(encodedPassword);
            usersRepository.save(users);
            response1.put("message","Update User id "+userUpdateDto.getUserId()+", Successfully!");
            response.put("isSuccess",true);
            response.put("message","updated User Successfully!");
        }
        return  response;
    }

    public  HashMap<String,Object> deleteUser(UserDeleteDto userDeleteDto){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();
        var users = usersRepository.findById(userDeleteDto.getUserId()).orElse(null);
        if(users==null){
            response1.put("message","incorrect User id "+userDeleteDto.getUserId()+", please enter the valid id!");
            response.put("isSuccess",false);
        }else {
            usersRepository.deleteById(userDeleteDto.getUserId());
            response1.put("message","User deleted successfully! "+userDeleteDto.getUserId());
            response.put("isSuccess",true);
        }
        response.put("message",response1);
        return  response;
    }

}
