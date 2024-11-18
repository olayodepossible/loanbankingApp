package com.possible.loanbanking.controller;

import com.possible.loanbanking.model.Loan;
import com.possible.loanbanking.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping("/request")
    public ResponseEntity<Loan> requestLoan(@RequestParam Long customerId, @RequestParam BigDecimal loanAmount) {
        return new ResponseEntity<>(loanService.requestLoan(customerId, loanAmount), HttpStatus.CREATED);
    }

    @PatchMapping("/{loanId}/approve")
    public ResponseEntity<Void> approveLoan(@PathVariable Long loanId) {
        loanService.approveLoan(loanId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{loanId}/reject")
    public ResponseEntity<Void> rejectLoan(@PathVariable Long loanId) {
        loanService.rejectLoan(loanId);
        return ResponseEntity.ok().build();
    }
}

