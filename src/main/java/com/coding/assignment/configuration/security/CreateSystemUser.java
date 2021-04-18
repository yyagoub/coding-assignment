package com.coding.assignment.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateSystemUser implements ApplicationRunner {
    @Autowired
    private SecurityService securityService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        securityService.saveUsers(getUsers());
    }

    private List<User> getUsers(){
        List<User> users = new ArrayList<>();
        // user
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user"));
        users.add(user);
        // admin
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        users.add(admin);
        // return
        return users;
    }

}
