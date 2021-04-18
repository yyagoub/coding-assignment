package com.coding.assignment.controller;

import com.coding.assignment.dto.ServiceDto;
import com.coding.assignment.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public List<ServiceDto> getMapping(){
        return serviceService.findAllServices();
    }

    @GetMapping("/{id}")
    public ServiceDto getMappingById(@PathVariable Long id){
        return serviceService.findServiceById(id);
    }

    @PostMapping
    public ServiceDto postMapping(@RequestBody ServiceDto serviceDto){
        return serviceService.createService(serviceDto);
    }

    @PutMapping
    public ServiceDto putMapping(@RequestBody ServiceDto serviceDto){
        return serviceService.updateService(serviceDto);
    }

    @DeleteMapping("/{id}")
    public void deleteMapping(@PathVariable Long id){
            serviceService.removeService(id);
    }
}
