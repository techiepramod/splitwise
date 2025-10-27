package com.pramod.splitwise.controller;

import com.pramod.splitwise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public String createUser(@RequestParam String name, @RequestParam String email, @RequestParam String oauthId, @RequestParam String role) {
        userService.saveUser(name, email, oauthId, role);
        return "User Created";
    }

}
