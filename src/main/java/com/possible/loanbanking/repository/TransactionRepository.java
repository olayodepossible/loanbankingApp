package com.possible.loanbanking.repository;

import com.possible.loanbanking.model.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findBySavingsAccount_AccountNumberAndTimestampBetween(
            String accountNumber, LocalDateTime start, LocalDateTime end, Pageable pageable
    );
}

