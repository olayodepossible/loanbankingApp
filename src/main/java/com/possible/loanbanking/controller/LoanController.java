package com.possible.loanbanking.controller;

import com.possible.loanbanking.dto.response.ResponseDto;
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
    public ResponseEntity<ResponseDto<Object>> requestLoan(@RequestParam Long customerId, @RequestParam BigDecimal loanAmount) {
        ResponseDto<Object> loanResp = loanService.requestLoan(customerId, loanAmount);
        return new ResponseEntity<>(loanResp, HttpStatus.CREATED);
    }

    @PatchMapping("/{loanId}/approve")
    public ResponseEntity<String> approveLoan(@PathVariable Long loanId) {
        loanService.approveLoan(loanId);
        return new ResponseEntity<>("Loan request approved", HttpStatus.OK);
    }

    @PatchMapping("/{loanId}/reject")
    public ResponseEntity<String> rejectLoan(@PathVariable Long loanId) {
        loanService.rejectLoan(loanId);
        return new ResponseEntity<>("Loan request rejected", HttpStatus.OK);
    }
}

