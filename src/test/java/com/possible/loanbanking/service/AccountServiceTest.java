package com.possible.loanbanking.service;

import com.possible.loanbanking.dto.req.AppUser;
import com.possible.loanbanking.model.Account;
import com.possible.loanbanking.model.Customer;
import com.possible.loanbanking.model.SavingsAccount;
import com.possible.loanbanking.repository.UserRepository;
import com.possible.loanbanking.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testCreateSavingsAccount_Success() {
        Long customerId = 1L;

        AppUser customer = new AppUser();
        customer.setId(customerId);

        Mockito.when(userRepository.existsById(customerId)).thenReturn(true);
        Mockito.when(accountRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(accountRepository.save(Mockito.any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SavingsAccount account = accountService.createSavingsAccount(customerId);

        assertNotNull(account);
        assertEquals(customerId, account.getCustomer().getId());
        assertNotNull(account.getAccountNumber());
    }

    @Test
    @ExceptionHandler(value = IllegalArgumentException.class)
    void testCreateSavingsAccount_CustomerNotFound() {
        Long customerId = 1L;
        Mockito.when(userRepository.existsById(customerId)).thenReturn(false);

        accountService.createSavingsAccount(customerId);
    }

    @Test
    @ExceptionHandler(value = IllegalArgumentException.class)
    void testCreateSavingsAccount_AlreadyExists() {
        Long customerId = 1L;

        Mockito.when(userRepository.existsById(customerId)).thenReturn(true);
        Mockito.when(accountRepository.findByCustomerId(customerId)).thenReturn(Optional.of(new Account()));

        accountService.createSavingsAccount(customerId);
    }
}
