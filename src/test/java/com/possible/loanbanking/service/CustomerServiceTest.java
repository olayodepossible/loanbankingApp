package com.possible.loanbanking.service;

import com.possible.loanbanking.model.Customer;
import com.possible.loanbanking.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    void testCreateCustomer_Success() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhoneNumber("1234567890");

        Mockito.when(customerRepository.existsByEmail("john.doe@example.com")).thenReturn(false);
        Mockito.when(customerRepository.existsByPhoneNumber("1234567890")).thenReturn(false);
        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerService.createCustomer(customer);

        assertNotNull(savedCustomer);
        assertEquals("John", savedCustomer.getFirstName());
        assertEquals("Doe", savedCustomer.getLastName());
    }

    @Test
    @ExceptionHandler(value = IllegalArgumentException.class)
    void testCreateCustomer_EmailAlreadyExists() {
        Mockito.when(customerRepository.existsByEmail("existing.email@example.com")).thenReturn(true);

        Customer customer = new Customer();
        customer.setEmail("existing.email@example.com");
        customerService.createCustomer(customer);
    }
}

