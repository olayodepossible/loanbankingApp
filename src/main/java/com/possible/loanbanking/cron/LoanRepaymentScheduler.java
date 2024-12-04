package com.possible.loanbanking.cron;

import com.possible.loanbanking.dto.enums.LoanStatus;
import com.possible.loanbanking.model.Account;
import com.possible.loanbanking.model.Loan;
import com.possible.loanbanking.repository.LoanRepository;
import com.possible.loanbanking.repository.AccountRepository;
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
    private final AccountRepository accountRepository;

    @Scheduled(cron = "0 0 0 3 * ?") // Runs at midnight on the 3rd day of every month
    public void processLoanRepayments() {
        List<Loan> loans = loanRepository.findAll().stream()
                .filter(loan -> loan.getStatus().name().equalsIgnoreCase(LoanStatus.APPROVED.name()) &&
                        loan.getRemainingBalance().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());

        for (Loan loan : loans) {
            Account account = accountRepository.findByUser(loan.getCustomer().getId())
                    .orElseThrow(() -> new IllegalStateException("Savings account not found for customer"));

            BigDecimal repaymentAmount = loan.getLoanAmount().multiply(BigDecimal.valueOf(0.10));
            BigDecimal customerBal = account.getBalance();
            if (customerBal.compareTo(repaymentAmount) > 0) {
                account.setBalance(account.getBalance().subtract(repaymentAmount));
                loan.setRemainingBalance(loan.getRemainingBalance().subtract(repaymentAmount));
                // TODO: send email notification
                accountRepository.save(account);
                loanRepository.save(loan);
            } else {
                // TODO: send email notification
            }
        }
    }
}

