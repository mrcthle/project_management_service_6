package de.szut.lf8_project.dtos.customerDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CustomerDTO {
    
    private Long id;
    private String name;
    private String contactCustomerSide;
}
