package com.possible.loanbanking.controller;

import com.possible.loanbanking.model.SavingsAccount;
import com.possible.loanbanking.service.SavingsAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class SavingsAccountController {
    private final SavingsAccountService savingsAccountService;

    @PostMapping
    public ResponseEntity<SavingsAccount> createAccount(@RequestParam Long customerId) {
        return new ResponseEntity<>(savingsAccountService.createSavingsAccount(customerId), HttpStatus.CREATED);
    }
}

