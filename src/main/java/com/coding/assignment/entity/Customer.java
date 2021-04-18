package com.coding.assignment.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fullName;
    private String address;
    private BigDecimal balance;
    private LocalDateTime createdDatetime;
    private Long createdByUserId;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "customer_service",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<Service> services;
}
