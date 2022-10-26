package de.szut.lf8_project.services;

import de.szut.lf8_project.dtos.customerDto.CustomerDTO;

import java.util.ArrayList;
import java.util.List;

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
        return (id > 3 || id < 1) ? null : customerList[(int) (id - 1)];
    }
}
