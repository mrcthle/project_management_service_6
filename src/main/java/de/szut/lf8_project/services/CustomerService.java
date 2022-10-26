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
    
    public CustomerDTO getCustomerByContact(String contact) {
        CustomerDTO response = null;
        for (CustomerDTO customer : customerList) {
            if(customer.getContactCustomerSide().equals(contact)) {
                response = customer;
            }
        }
        return response;
    }
}
