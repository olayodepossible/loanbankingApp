package com.possible.loanbanking.service;


import com.possible.loanbanking.dto.enums.LoanStatus;
import com.possible.loanbanking.dto.response.ResponseDto;
import com.possible.loanbanking.model.Loan;
import com.possible.loanbanking.repository.UserRepository;
import com.possible.loanbanking.repository.LoanRepository;
import com.possible.loanbanking.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public ResponseDto<Object> requestLoan(Long customerId, BigDecimal loanAmount) {
        List<Loan> existingLoans = loanRepository.findByCustomerIdAndStatus(customerId, LoanStatus.APPROVED);
        boolean hasOutstandingLoan = existingLoans.stream()
                .anyMatch(loan -> loan.getRemainingBalance().compareTo(BigDecimal.ZERO) > 0);

        if (hasOutstandingLoan) {
            throw new IllegalArgumentException("Customer has an outstanding loan.");
        }

        Loan loan = new Loan();
        loan.setCustomer(userRepository.getById(customerId));
        loan.setLoanAmount(loanAmount);
        loan.setRemainingBalance(loanAmount);
        loan = loanRepository.save(loan);

        return ResponseDto.builder()
                .statusCode(200)
                .responseMessage("Loan request created successfully")
                .data(loan)
                .build();
    }

    public void approveLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() ->
                new IllegalArgumentException("Loan not found."));
        loan.setStatus(LoanStatus.APPROVED);
        loanRepository.save(loan);

        // TODO: drop the money in beneficiary account
    }

    public void rejectLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() ->
                new IllegalArgumentException("Loan not found.")
        );
        loan.setStatus(LoanStatus.REJECTED);
        loanRepository.save(loan);
    }
}

