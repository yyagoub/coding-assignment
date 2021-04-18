package com.coding.assignment.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class Service implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String Service;
    private BigDecimal price;
    private LocalDateTime createdDatetime;
    private Long createdByUserId;
    @ManyToMany(mappedBy = "services", cascade = CascadeType.ALL)
    private Set<Customer> customers;
}
