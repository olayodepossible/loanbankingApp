package com.possible.loanbanking.repository;

import com.possible.loanbanking.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByCustomerIdAndStatus(Long customerId, LoanStatus status);
}

