package com.possible.loanbanking.cron;

import com.possible.loanbanking.model.Loan;
import com.possible.loanbanking.model.SavingsAccount;
import com.possible.loanbanking.repository.LoanRepository;
import com.possible.loanbanking.repository.SavingsAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoanRepaymentScheduler {
    private final LoanRepository loanRepository;
    private final SavingsAccountRepository savingsAccountRepository;

    @Scheduled(cron = "0 0 0 3 * ?") // Runs at midnight on the 3rd day of every month
    public void processLoanRepayments() {
        List<Loan> loans = loanRepository.findAll().stream()
                .filter(loan -> loan.getStatus() == LoanStatus.APPROVED &&
                        loan.getRemainingBalance().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());

        for (Loan loan : loans) {
            SavingsAccount account = savingsAccountRepository.findByCustomerId(loan.getCustomer().getId())
                    .orElseThrow(() -> new IllegalStateException("Savings account not found for customer"));

            BigDecimal repaymentAmount = loan.getLoanAmount().multiply(BigDecimal.valueOf(0.10));
            if (account.getBalance().compareTo(repaymentAmount) >= 0) {
                account.setBalance(account.getBalance().subtract(repaymentAmount));
                loan.setRemainingBalance(loan.getRemainingBalance().subtract(repaymentAmount));

                // Save updated account and loan
                savingsAccountRepository.save(account);
                loanRepository.save(loan);
            }
        }
    }
}

