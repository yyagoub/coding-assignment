package com.coding.assignment.service;

import com.coding.assignment.configuration.exception.ErrorsHandler;
import com.coding.assignment.configuration.exception.UnprocessableEntityException;
import com.coding.assignment.configuration.exception.ValidationError;
import com.coding.assignment.dto.CustomerDto;
import com.coding.assignment.dto.ServiceDto;
import com.coding.assignment.entity.Customer;
import com.coding.assignment.entity.Service;
import com.coding.assignment.repository.ServiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ErrorsHandler errorsHandler;
    @Autowired
    private CustomerService customerService;

    @Transactional
    public ServiceDto createService(final ServiceDto serviceDto){
        checkServiceIdNotExists(serviceDto.getId());
        validateServiceDto(serviceDto);
        Service service = mapServiceDtoToService(serviceDto);
        service.setCustomers(customerService.mapCustomerDtosToCustomers(serviceDto.getCustomerDtos()));
        service = serviceRepository.save(service);
        return mapServiceToServiceDto(service);
    }

    @Transactional
    public ServiceDto updateService(final ServiceDto serviceDto){
        checkServiceIdExists(serviceDto.getId());
        validateServiceDto(serviceDto);
        Service service = mapServiceDtoToService(serviceDto);
        service.setCustomers(customerService.mapCustomerDtosToCustomers(serviceDto.getCustomerDtos()));
        service = serviceRepository.save(service);
        return mapServiceToServiceDto(service);
    }

    public ServiceDto findServiceById(final Long id){
        Service service = checkServiceIdExists(id);
        ServiceDto serviceDto = mapServiceToServiceDto(service);
        serviceDto.setCustomerDtos(customerService.mapCustomersToCustomerDtos(service.getCustomers()));
        return serviceDto;
    }

    public List<ServiceDto> findAllServices(){
        return serviceRepository
                .findAll()
                .stream()
                .map(service -> {
                    ServiceDto serviceDto = modelMapper.map(service, ServiceDto.class);
                    serviceDto.setCustomerDtos(customerService.mapCustomersToCustomerDtos(service.getCustomers()));
                    return serviceDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeService(final Long id){
        checkServiceIdExists(id);
        serviceRepository.deleteById(id);
    }

    private void validateServiceDto(final ServiceDto serviceDto) {
        List<ValidationError> errors = new ArrayList<>();
        if (serviceDto.getService() == null || serviceDto.getService().isEmpty())
            errorsHandler.publish("service", "field.required");
        if (serviceDto.getPrice() == null)
            errorsHandler.publish("price", "field.required");
        if (!CollectionUtils.isEmpty(errors))
            throw new UnprocessableEntityException(errors);
    }

    private Service checkServiceIdExists(final Long id){
        if(id == null)
            throw new UnprocessableEntityException(errorsHandler.publish("service", "id.not.exists"));
        Optional<Service> service = getOptionalServiceById(id);
        if(service.isPresent())
            return service.get();
        throw new UnprocessableEntityException(errorsHandler.publish("service", "id.not.exists"));
    }

    private void checkServiceIdNotExists(final Long id){
        if (id == null) return;
        Optional<Service> service = getOptionalServiceById(id);
        if(service.isPresent())
            throw new UnprocessableEntityException(errorsHandler.publish("service", "id.not.exists"));
    }

    private Optional<Service> getOptionalServiceById(final Long id){
        return serviceRepository.findById(id);
    }

    protected Service mapServiceDtoToService(final ServiceDto serviceDto) {
        return modelMapper.map(serviceDto, Service.class);
    }

    protected ServiceDto mapServiceToServiceDto(final Service serviceDto) {
        return modelMapper.map(serviceDto, ServiceDto.class);
    }

    protected Set<Service> mapServiceDtosToServices(final Set<ServiceDto> serviceDtos){
        if(CollectionUtils.isEmpty(serviceDtos)) return new HashSet<>();
        return serviceDtos.stream().map(this::mapServiceDtoToService).collect(Collectors.toSet());
    }

    protected Set<ServiceDto> mapServicesToServiceDtos(final Set<Service> services){
        if(CollectionUtils.isEmpty(services)) return new HashSet<>();
        return services.stream().map(this::mapServiceToServiceDto).collect(Collectors.toSet());
    }
}
