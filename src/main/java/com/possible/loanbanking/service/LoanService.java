package com.possible.loanbanking.service;


import com.possible.loanbanking.dto.enums.LoanStatus;
import com.possible.loanbanking.model.Customer;
import com.possible.loanbanking.model.Loan;
import com.possible.loanbanking.repository.CustomerRepository;
import com.possible.loanbanking.repository.LoanRepository;
import com.possible.loanbanking.repository.SavingsAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final CustomerRepository customerRepository;

    public Loan requestLoan(Long customerId, BigDecimal loanAmount) {
        List<Loan> existingLoans = loanRepository.findByCustomerIdAndStatus(customerId, LoanStatus.APPROVED);
        boolean hasOutstandingLoan = existingLoans.stream()
                .anyMatch(loan -> loan.getRemainingBalance().compareTo(BigDecimal.ZERO) > 0);

        if (hasOutstandingLoan) {
            throw new IllegalArgumentException("Customer has an outstanding loan.");
        }

        Loan loan = new Loan();
        loan.setCustomer(customerRepository.getReferenceById(customerId)); // Set only the ID for simplicity
        loan.setLoanAmount(loanAmount);
        loan.setRemainingBalance(loanAmount);
        return loanRepository.save(loan);
    }

    public void approveLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() ->
                new IllegalArgumentException("Loan not found.")
        );
        loan.setStatus(LoanStatus.APPROVED);
        loanRepository.save(loan);
    }

    public void rejectLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() ->
                new IllegalArgumentException("Loan not found.")
        );
        loan.setStatus(LoanStatus.REJECTED);
        loanRepository.save(loan);
    }
}

