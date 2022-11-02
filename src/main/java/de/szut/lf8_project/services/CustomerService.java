package de.szut.lf8_project.services;

import de.szut.lf8_project.dtos.customerDto.CustomerDTO;
import de.szut.lf8_project.exceptionHandling.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    
    private final static CustomerDTO[] customerList = new CustomerDTO[]{
            new CustomerDTO(1L, "Amazon", "Nelson Mandela"),
            new CustomerDTO(2L, "Nestle", "Martin Luther King"),
            new CustomerDTO(3L, "Facebook", "Edward Snowden")
    };
    
    public List<CustomerDTO> getCustomers() {
        return List.of(customerList);
    }
    
    public CustomerDTO getCustomerById(Long id) {
        if(id > customerList.length || id <= 0) {
            throw new ResourceNotFoundException("Customer with id = " + id + " not found.");
        } else {
            return customerList[(int) (id - 1)];
        }
    }
}
