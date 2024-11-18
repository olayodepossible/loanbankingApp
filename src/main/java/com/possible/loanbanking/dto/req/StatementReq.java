package com.possible.loanbanking.dto.req;



import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StatementReq {

    @NotNull(message = "Account number is required")
    private String accountNumber;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private int page = 0; // Default page number
    private int size = 10; // Default page size
}

