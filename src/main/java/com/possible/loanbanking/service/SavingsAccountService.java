package com.possible.loanbanking.service;

import com.possible.loanbanking.model.SavingsAccount;
import com.possible.loanbanking.repository.CustomerRepository;
import com.possible.loanbanking.repository.SavingsAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SavingsAccountService {
    private final SavingsAccountRepository savingsAccountRepository;
    private final CustomerRepository customerRepository;

    public SavingsAccount createSavingsAccount(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Customer does not exist.");
        }
        if (savingsAccountRepository.findByCustomerId(customerId).isPresent()) {
            throw new IllegalArgumentException("Customer already has a savings account.");
        }
        String accountNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        SavingsAccount account = new SavingsAccount();
        account.setCustomer(customerRepository.findById(customerId).orElseThrow());
        account.setAccountNumber(accountNumber);
        return savingsAccountRepository.save(account);
    }
}

