package com.coding.assignment.controller;

import com.coding.assignment.dto.CustomerDto;
import com.coding.assignment.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<CustomerDto> viewAllCustomers(){
        return customerService.findAllCustomers();
    }

    @GetMapping("/{id}")
    public CustomerDto viewACustomer(@PathVariable Long id){
        return customerService.findCustomerById(id);
    }

    @PostMapping
    public CustomerDto createACustomer(@RequestBody CustomerDto customerDto){
        return customerService.createCustomer(customerDto);
    }

     @PutMapping
    public CustomerDto updateACustomer(@RequestBody CustomerDto customerDto){
        return customerService.updateCustomer(customerDto);
    }

    @DeleteMapping("/{id}")
    public void deleteACustomer(@PathVariable Long id){
        customerService.removeCustomerById(id);
    }
}
