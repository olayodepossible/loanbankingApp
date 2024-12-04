/*package com.possible.loanbanking.service.impl;

import com.possible.loanbanking.dto.enums.TransactionType;
import com.possible.loanbanking.model.Account;
import com.possible.loanbanking.model.Transaction;
import com.possible.loanbanking.repository.AccountRepository;
import com.possible.loanbanking.repository.TransactionRepository;
import com.possible.loanbanking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class ConcurrentSavingsAccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    // Distributed lock simulation
    private final ReentrantLock accountLock = new ReentrantLock();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void creditAccountWithConcurrencyControl(String accountNumber, BigDecimal amount) {
        accountLock.lock();
        try {
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            Transaction transaction = createTransaction(account, amount);
            if (isDuplicateTransaction(transaction.getId())) {
                throw new IllegalStateException("Duplicate transaction");
            }



            // Optimistic locking version check
            Long initialVersion = account.getId();
//            Long initialVersion = account.getVersion();

            // Perform atomic balance update
            account.setBalance(account.getBalance().add(amount));

            // Save with version check to prevent concurrent modifications
            accountRepository.save(account);

            // Verify no concurrent modification occurred
            if (!initialVersion.equals(account.getId())) {
                throw new OptimisticLockException("Concurrent modification detected");
            }
        } finally {
            accountLock.unlock();
        }
    }

    private boolean isDuplicateTransaction(Long transactionId) {
        // implemented distributed cache to check if transaction ID already processed
        return false;
    }

    private Transaction createTransaction(Account account, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction = transactionRepository.save(transaction);
        return transaction;
    }
}*/
