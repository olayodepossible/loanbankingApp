package com.possible.loanbanking.repository;

import com.possible.loanbanking.model.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByAccount_AccountNumberAndPostedDateBetween(
            String accountNumber, LocalDateTime start, LocalDateTime end, Pageable pageable
    );

    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId")
    List<Transaction> findAllTransactionByAccountId(Long accountId);




}

