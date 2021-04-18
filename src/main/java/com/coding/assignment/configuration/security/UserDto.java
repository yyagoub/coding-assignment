package com.coding.assignment.configuration.security;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String token;
    private Boolean deleted;
}
