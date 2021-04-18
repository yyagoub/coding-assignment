package com.coding.assignment.service;

import com.coding.assignment.configuration.exception.ErrorsHandler;
import com.coding.assignment.configuration.exception.UnprocessableEntityException;
import com.coding.assignment.configuration.exception.ValidationError;
import com.coding.assignment.dto.CustomerDto;
import com.coding.assignment.dto.ServiceDto;
import com.coding.assignment.entity.Customer;
import com.coding.assignment.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.coding.assignment.entity.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ErrorsHandler errorsHandler;
    @Autowired
    private ServiceService serviceService;

    @Transactional
    public CustomerDto createCustomer(final CustomerDto customerDto){
        checkCustomerIdNotExists(customerDto.getId());
        validateCustomerDto(customerDto);
        Customer customer = mapCustomerDtoToCustomer(customerDto);
        customer.setServices(serviceService.mapServiceDtosToServices(customerDto.getServiceDtos()));
        customer = customerRepository.save(customer);
        return mapCustomerToCustomerDto(customer);
    }

    @Transactional
    public CustomerDto updateCustomer(final CustomerDto customerDto){
        checkCustomerIdExists(customerDto.getId());
        validateCustomerDto(customerDto);
        Customer customer = mapCustomerDtoToCustomer(customerDto);
        customer.setServices(serviceService.mapServiceDtosToServices(customerDto.getServiceDtos()));
        customer = customerRepository.save(customer);
        return mapCustomerToCustomerDto(customer);
    }

    public CustomerDto findCustomerById(final Long id){
        Customer customer = checkCustomerIdExists(id);
        CustomerDto customerDto = mapCustomerToCustomerDto(customer);
        customerDto.setServiceDtos(serviceService.mapServicesToServiceDtos(customer.getServices()));
        return customerDto;
    }

    public List<CustomerDto> findAllCustomers(){
        return customerRepository
                .findAll()
                .stream()
                .map(customer -> {
                    CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
                    customerDto.setServiceDtos(serviceService.mapServicesToServiceDtos(customer.getServices()));
                    return customerDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeCustomerById(final Long id){
        checkCustomerIdExists(id);
        customerRepository.deleteById(id);
    }

    protected CustomerDto addServiceToCustomer(ServiceDto serviceDto){
        Customer customer = checkCustomerIdExists(serviceDto.getCustomerId());
        Service service = serviceService.mapServiceDtoToService(serviceDto);
        customer.getServices().add(service);
        customer = customerRepository.save(customer);
        return mapCustomerToCustomerDto(customer);
    }

    private void validateCustomerDto(final CustomerDto customerDto){
        List<ValidationError> errors = new ArrayList<>();
        if (customerDto.getAddress() == null || customerDto.getAddress().isEmpty())
            errorsHandler.publish("address", "field.required", errors);
        if (customerDto.getBalance() == null)
            errorsHandler.publish("balance", "field.required", errors);
        if (customerDto.getFullName() == null || customerDto.getFullName().isEmpty())
            errorsHandler.publish("fullName", "field.required", errors);

        if (!CollectionUtils.isEmpty(errors))
            throw new UnprocessableEntityException(errors);
    }

    protected Customer checkCustomerIdExists(final Long id){
        if(id == null)
            throw new UnprocessableEntityException(errorsHandler.publish("customer", "id.not.exists"));
        Optional<Customer> customer = getOptionalCustomerById(id);
        if(customer.isPresent())
            return customer.get();
        throw new UnprocessableEntityException(errorsHandler.publish("customer", "id.not.exists"));
    }

    private void checkCustomerIdNotExists(final Long id){
        if (id == null) return;
        Optional<Customer> customer = getOptionalCustomerById(id);
        if(customer.isPresent())
            throw new UnprocessableEntityException(errorsHandler.publish("customer", "id.exists"));
    }

    private Optional<Customer> getOptionalCustomerById(final Long id){
        return customerRepository.findById(id);
    }

    protected Customer mapCustomerDtoToCustomer(final CustomerDto customerDto) {
        return modelMapper.map(customerDto, Customer.class);
    }

    protected CustomerDto mapCustomerToCustomerDto(final Customer customer) {
        return modelMapper.map(customer, CustomerDto.class);
    }

    protected Set<CustomerDto> mapCustomersToCustomerDtos(final Set<Customer> customers){
        if(CollectionUtils.isEmpty(customers)) return new HashSet<>();
        return customers.stream().map(this::mapCustomerToCustomerDto).collect(Collectors.toSet());
    }

    protected Set<Customer> mapCustomerDtosToCustomers(final Set<CustomerDto> customerDtos){
        if(CollectionUtils.isEmpty(customerDtos)) return new HashSet<>();
        return customerDtos.stream().map(this::mapCustomerDtoToCustomer).collect(Collectors.toSet());
    }
}
