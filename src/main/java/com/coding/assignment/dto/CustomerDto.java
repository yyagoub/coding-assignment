package com.coding.assignment.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CustomerDto {
    private Long id;
    private String fullName;
    private String address;
    private Double balance;
    private Set<ServiceDto> serviceDtos;
}
