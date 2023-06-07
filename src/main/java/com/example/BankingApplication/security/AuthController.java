package com.example.BankingApplication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private CustomUserDetails customUserDetails;
    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest jwtAuthRequest) throws Exception {

        System.out.println(jwtAuthRequest);
        try{
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuthRequest.getUserName(),jwtAuthRequest.getPassword()));
        }catch (UsernameNotFoundException e){
            e.printStackTrace();
            throw  new Exception("bad credential!");
        }catch (BadCredentialsException e){
            e.printStackTrace();
            throw  new Exception("bad credential!");
        }

        //fine area
        UserDetails userDetails=this.customUserDetails.loadUserByUsername(jwtAuthRequest.getUserName());
        String token=  this.jwtTokenHelper.generateToken(userDetails);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setToken(token);
        System.out.println("JWT "+token);
        return  new ResponseEntity<JwtAuthResponse>(jwtAuthResponse,HttpStatus.OK);


    }

}
