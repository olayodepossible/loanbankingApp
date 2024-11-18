package com.possible.loanbanking.service;

import com.possible.loanbanking.model.Customer;
import com.possible.loanbanking.model.SavingsAccount;
import com.possible.loanbanking.repository.CustomerRepository;
import com.possible.loanbanking.repository.SavingsAccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SavingsAccountServiceTest {
    @Autowired
    private SavingsAccountService savingsAccountService;

    @MockBean
    private SavingsAccountRepository savingsAccountRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    void testCreateSavingsAccount_Success() {
        Long customerId = 1L;

        Customer customer = new Customer();
        customer.setId(customerId);

        Mockito.when(customerRepository.existsById(customerId)).thenReturn(true);
        Mockito.when(savingsAccountRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(savingsAccountRepository.save(Mockito.any(SavingsAccount.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SavingsAccount account = savingsAccountService.createSavingsAccount(customerId);

        assertNotNull(account);
        assertEquals(customerId, account.getCustomer().getId());
        assertNotNull(account.getAccountNumber());
    }

    @Test
    @ExceptionHandler(value = IllegalArgumentException.class)
    void testCreateSavingsAccount_CustomerNotFound() {
        Long customerId = 1L;
        Mockito.when(customerRepository.existsById(customerId)).thenReturn(false);

        savingsAccountService.createSavingsAccount(customerId);
    }

    @Test
    @ExceptionHandler(value = IllegalArgumentException.class)
    void testCreateSavingsAccount_AlreadyExists() {
        Long customerId = 1L;

        Mockito.when(customerRepository.existsById(customerId)).thenReturn(true);
        Mockito.when(savingsAccountRepository.findByCustomerId(customerId)).thenReturn(Optional.of(new SavingsAccount()));

        savingsAccountService.createSavingsAccount(customerId);
    }
}
