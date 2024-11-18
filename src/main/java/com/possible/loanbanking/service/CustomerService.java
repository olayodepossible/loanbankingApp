package com.possible.loanbanking.service;

import com.possible.loanbanking.model.Customer;
import com.possible.loanbanking.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }
        if (customerRepository.existsByPhoneNumber(customer.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists.");
        }
        return customerRepository.save(customer);
    }
}

