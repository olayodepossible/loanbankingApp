package com.possible.loanbanking.dto;

public enum LoanStatus {
    PENDING, // Loan is waiting for approval
    APPROVED, // Loan is approved and repayment is active
    REJECTED, // Loan was rejected
    REPAID // Loan is fully repaid
}
