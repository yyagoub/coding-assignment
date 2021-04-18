package com.coding.assignment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class ServiceDto {
    private Long id;
    private String Service;
    private BigDecimal price;
    private Set<CustomerDto> customerDtos;
    private Long customerId;
}
