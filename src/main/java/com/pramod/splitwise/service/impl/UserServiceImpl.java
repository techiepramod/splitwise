package com.pramod.splitwise.service.impl;

import com.pramod.splitwise.entity.Role;
import com.pramod.splitwise.entity.User;
import com.pramod.splitwise.repository.UserRepository;
import com.pramod.splitwise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepo;

    @Override
    @Transactional
    public void saveUser(String name, String email, String oauthId, String role){
        userRepo.save(new User(name, email, oauthId, Role.valueOf(role)));
    }
}
