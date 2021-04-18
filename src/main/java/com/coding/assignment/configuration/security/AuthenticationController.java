package com.coding.assignment.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    private SecurityService securityService;
    @PostMapping
    public UserDto postMapping(@RequestBody AuthenticationDto authenticationDto){
        return securityService.authenticate(authenticationDto);
    }
}
