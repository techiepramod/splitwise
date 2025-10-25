package com.pramod.splitwise.service;

import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    @Transactional
    void saveUser(String name, String email, String oauthId);
}
