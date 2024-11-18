package com.possible.loanbanking.cron;

import com.possible.loanbanking.model.Customer;
import com.possible.loanbanking.model.Loan;
import com.possible.loanbanking.model.SavingsAccount;
import com.possible.loanbanking.repository.LoanRepository;
import com.possible.loanbanking.repository.SavingsAccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LoanRepaymentSchedulerTest {
    @SpyBean
    private LoanRepaymentScheduler loanRepaymentScheduler;

    @MockBean
    private LoanRepository loanRepository;

    @MockBean
    private SavingsAccountRepository savingsAccountRepository;

    @Test
    public void testProcessLoanRepayments() {
        Customer customer = new Customer();
        customer.setId(1L);

        SavingsAccount account = new SavingsAccount();
        account.setCustomer(customer);
        account.setBalance(BigDecimal.valueOf(1000));

        Loan loan = new Loan();
        loan.setCustomer(customer);
        loan.setLoanAmount(BigDecimal.valueOf(500));
        loan.setRemainingBalance(BigDecimal.valueOf(500));
        loan.setStatus(LoanStatus.APPROVED);

        Mockito.when(loanRepository.findAll()).thenReturn(List.of(loan));
        Mockito.when(savingsAccountRepository.findByCustomerId(1L)).thenReturn(Optional.of(account));

        loanRepaymentScheduler.processLoanRepayments();

        assertEquals(BigDecimal.valueOf(950), account.getBalance());
        assertEquals(BigDecimal.valueOf(450), loan.getRemainingBalance());
    }
}

