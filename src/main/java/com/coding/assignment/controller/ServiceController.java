package com.coding.assignment.controller;

import com.coding.assignment.dto.ServiceDto;
import com.coding.assignment.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public List<ServiceDto> viewAllServices(){
        return serviceService.findAllServices();
    }

    @GetMapping("/{customerId}")
    public Set<ServiceDto> viewServicesOfACustomer(@PathVariable Long customerId){
        return serviceService.findServiceByCustomerId(customerId);
    }

    @PostMapping
    public ServiceDto createAServiceForACustomer(@RequestBody ServiceDto serviceDto){
        return serviceService.createServiceForACustomer(serviceDto);
    }

    @PutMapping
    public ServiceDto updateAService(@RequestBody ServiceDto serviceDto){
        return serviceService.updateService(serviceDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAService(@PathVariable Long id){
            serviceService.removeService(id);
    }
}
