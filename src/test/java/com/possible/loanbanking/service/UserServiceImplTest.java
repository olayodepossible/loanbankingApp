package com.possible.loanbanking.service;

import com.possible.loanbanking.model.Customer;
import com.possible.loanbanking.repository.UserRepository;
import com.possible.loanbanking.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testCreateCustomer_Success() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhoneNumber("1234567890");

        Mockito.when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(false);
        Mockito.when(userRepository.existsByPhoneNumber("1234567890")).thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = userServiceImpl.createCustomer(customer);

        assertNotNull(savedCustomer);
        assertEquals("John", savedCustomer.getFirstName());
        assertEquals("Doe", savedCustomer.getLastName());
    }

    @Test
    @ExceptionHandler(value = IllegalArgumentException.class)
    void testCreateCustomer_EmailAlreadyExists() {
        Mockito.when(userRepository.existsByEmail("existing.email@example.com")).thenReturn(true);

        Customer customer = new Customer();
        customer.setEmail("existing.email@example.com");
        userServiceImpl.createCustomer(customer);
    }
}

