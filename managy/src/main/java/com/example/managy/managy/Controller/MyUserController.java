package com.example.managy.managy.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import com.example.managy.managy.Entity.MyUser;
import com.example.managy.managy.Service.MyUserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class MyUserController {
    @Autowired
    private MyUserService myUserService;

    @Autowired 
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register/user")
    public MyUser createUser(@RequestBody MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
       return myUserService.save(user);

    }
    
}
