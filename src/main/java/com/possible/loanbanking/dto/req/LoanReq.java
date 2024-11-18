package com.possible.loanbanking.dto.req;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
public class LoanReq {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @DecimalMin(value = "1.0", message = "Loan amount must be at least 1.0")
    private BigDecimal loanAmount;
}

