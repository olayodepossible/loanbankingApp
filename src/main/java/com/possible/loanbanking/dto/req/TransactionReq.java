package com.possible.loanbanking.dto.req;

import com.johnbosco.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
public class TransactionReq {

    @NotNull(message = "Account number is required")
    private String accountNumber;

    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;
}

