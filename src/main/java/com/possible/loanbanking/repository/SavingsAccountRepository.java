package com.possible.loanbanking.repository;

import com.possible.loanbanking.model.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    Optional<SavingsAccount> findByCustomerId(Long customerId);
    Optional<SavingsAccount> findByAccountNumber(String accountNumber);
}

