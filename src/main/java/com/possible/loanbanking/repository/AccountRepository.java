package com.possible.loanbanking.repository;

import com.possible.loanbanking.dto.req.AccountType;
import com.possible.loanbanking.model.Account;
import com.possible.loanbanking.model.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUser(Long customerId);

    Optional<Account> findByAccountNumber(String accountNumber);
    @Query("SELECT a FROM Account a WHERE a.status = 'ACTIVE'")
    List<Account> findAllActiveAccounts();
    @Query("SELECT a FROM Account a WHERE a.status = 'INACTIVE'")
    List<Account> findAllInActiveAccounts();
    @Query("SELECT a FROM Account a WHERE a.accountType = :accountType")
    List<Account> findAllByAccountType(AccountType accountType);
}

